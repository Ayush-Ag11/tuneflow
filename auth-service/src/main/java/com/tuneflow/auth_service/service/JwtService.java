package com.tuneflow.auth_service.service;

import com.tuneflow.auth_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.UUID;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String extractSubject(String token);

    String extractUsername(String token);

    String extractEmail(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String extractTokenType(String token);

    boolean isAccessToken(String token);

    boolean isRefreshToken(String token);

    Instant extractExpiration(String token);
}
