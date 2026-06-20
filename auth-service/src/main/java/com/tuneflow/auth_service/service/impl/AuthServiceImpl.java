package com.tuneflow.auth_service.service.impl;

import com.tuneflow.auth_service.config.VerificationProperties;
import com.tuneflow.auth_service.dto.request.LoginRequest;
import com.tuneflow.auth_service.dto.request.LogoutRequest;
import com.tuneflow.auth_service.dto.request.RefreshTokenRequest;
import com.tuneflow.auth_service.dto.request.RegisterRequest;
import com.tuneflow.auth_service.dto.request.ResendVerificationRequest;
import com.tuneflow.auth_service.dto.response.AuthenticationResponse;
import com.tuneflow.auth_service.dto.response.RegisterResponse;
import com.tuneflow.auth_service.entity.EmailVerificationToken;
import com.tuneflow.auth_service.entity.RefreshToken;
import com.tuneflow.auth_service.entity.User;
import com.tuneflow.auth_service.exception.AccountAlreadyVerifiedException;
import com.tuneflow.auth_service.exception.EmailAlreadyExistsException;
import com.tuneflow.auth_service.exception.EmailVerificationException;
import com.tuneflow.auth_service.exception.InvalidCredentialsException;
import com.tuneflow.auth_service.exception.UsernameAlreadyExistsException;
import com.tuneflow.auth_service.repository.EmailVerificationTokenRepository;
import com.tuneflow.auth_service.repository.RefreshTokenRepository;
import com.tuneflow.auth_service.repository.UserRepository;
import com.tuneflow.auth_service.security.CustomUserDetails;
import com.tuneflow.auth_service.service.AuthService;
import com.tuneflow.auth_service.service.EmailService;
import com.tuneflow.auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailService emailService;
    private final VerificationProperties verificationProperties;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException(request.username());
        }

        User user = new User(
                request.email(),
                request.username(),
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);

        String verificationToken =
                UUID.randomUUID().toString();

        EmailVerificationToken emailVerificationToken =
                new EmailVerificationToken(
                        verificationToken,
                        savedUser,
                        Instant.now().plusMillis(
                                verificationProperties
                                        .getVerificationTokenExpiration()
                        )
                );

        emailVerificationTokenRepository.save(
                emailVerificationToken
        );

        emailService.sendVerificationEmail(
                savedUser,
                verificationToken
        );

        return new RegisterResponse(savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername());

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Email not verified");
        }

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshTokenEntity =
                new RefreshToken(
                        refreshToken,
                        user,
                        jwtService.extractExpiration(refreshToken)
                );

        refreshTokenRepository.save(refreshTokenEntity);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    @Override
    public AuthenticationResponse refreshToken(
            RefreshTokenRequest request
    ) {

        String token = request.refreshToken();

        if (!jwtService.isRefreshToken(token)) {
            throw new BadCredentialsException(
                    "Invalid refresh token"
            );
        }

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new BadCredentialsException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        User user = refreshToken.getUser();

        if (!jwtService.isTokenValid(
                token,
                new CustomUserDetails(user)
        )) {

            throw new BadCredentialsException(
                    "Invalid refresh token"
            );
        }

        refreshToken.revoke();

        refreshTokenRepository.save(refreshToken);

        String accessToken =
                jwtService.generateAccessToken(user);

        String newRefreshToken =
                jwtService.generateRefreshToken(user);

        RefreshToken newRefreshTokenEntity =
                new RefreshToken(
                        newRefreshToken,
                        user,
                        jwtService.extractExpiration(
                                newRefreshToken
                        )
                );

        refreshTokenRepository.save(
                newRefreshTokenEntity
        );

        return new AuthenticationResponse(
                accessToken,
                newRefreshToken
        );
    }

    @Transactional
    @Override
    public void logout(
            LogoutRequest request
    ) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(
                                request.refreshToken())
                        .orElseThrow(() ->
                                new BadCredentialsException("Invalid refresh token"));
        if (refreshToken.isRevoked()) {
            return;
        }

        refreshToken.revoke();

        refreshTokenRepository.save(
                refreshToken
        );
    }

    @Transactional
    @Override
    public void verifyEmail(String token) {

        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new EmailVerificationException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new EmailVerificationException("Verification token already used");
        }

        if (verificationToken.isExpired()) {
            throw new EmailVerificationException("Verification token expired");
        }

        User user = verificationToken.getUser();

        user.enable();

        verificationToken.markAsUsed();
    }

    @Transactional
    @Override
    public void resendVerificationEmail(ResendVerificationRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        ));

        // Account not found — return silently to prevent user enumeration
        if (user == null) {
            return;
        }

        // Account already verified — tell the user explicitly
        if (user.isEnabled()) {
            throw new AccountAlreadyVerifiedException();
        }

        // Delete any existing token before issuing a fresh one.
        // The table has a unique constraint on user_id, so we must
        // remove the old record first.
        emailVerificationTokenRepository
                .findByUserId(user.getId())
                .ifPresent(emailVerificationTokenRepository::delete);

        String verificationToken = UUID.randomUUID().toString();

        EmailVerificationToken emailVerificationToken =
                new EmailVerificationToken(
                        verificationToken,
                        user,
                        Instant.now().plusMillis(
                                verificationProperties
                                        .getVerificationTokenExpiration()
                        )
                );

        emailVerificationTokenRepository.save(emailVerificationToken);

        emailService.sendVerificationEmail(user, verificationToken);
    }

}
