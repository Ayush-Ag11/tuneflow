package com.tuneflow.auth_service.controller;

import com.tuneflow.auth_service.dto.request.LoginRequest;
import com.tuneflow.auth_service.dto.request.LogoutRequest;
import com.tuneflow.auth_service.dto.request.RefreshTokenRequest;
import com.tuneflow.auth_service.dto.request.RegisterRequest;
import com.tuneflow.auth_service.dto.request.ResendVerificationRequest;
import com.tuneflow.auth_service.dto.response.AuthenticationResponse;
import com.tuneflow.auth_service.dto.response.RegisterResponse;
import com.tuneflow.auth_service.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody
            RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam @NotBlank String token) {

        authService.verifyEmail(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request) {

        authService.resendVerificationEmail(request);
        return ResponseEntity.noContent().build();
    }
}
