package com.tuneflow.auth_service.service;

import com.tuneflow.auth_service.dto.request.LoginRequest;
import com.tuneflow.auth_service.dto.request.LogoutRequest;
import com.tuneflow.auth_service.dto.request.RefreshTokenRequest;
import com.tuneflow.auth_service.dto.request.RegisterRequest;
import com.tuneflow.auth_service.dto.request.ResendVerificationRequest;
import com.tuneflow.auth_service.dto.response.AuthenticationResponse;
import com.tuneflow.auth_service.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void verifyEmail(String token);

    void resendVerificationEmail(ResendVerificationRequest request);
}
