package com.tuneflow.auth_service.service.impl;

import com.tuneflow.auth_service.config.JwtProperties;
import com.tuneflow.auth_service.entity.User;
import com.tuneflow.auth_service.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateAccessToken(User user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.accessTokenExpiration()
        );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.refreshTokenExpiration()
        );

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    @Override
    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);

        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    @Override
    public String extractTokenType(String token) {
        return extractClaims(token)
                .get("type", String.class);
    }

    @Override
    public boolean isAccessToken(String token) {
        return "access".equals(
                extractTokenType(token)
        );
    }

    @Override
    public boolean isRefreshToken(String token) {
        return "refresh".equals(
                extractTokenType(token)
        );
    }

    @Override
    public Instant extractExpiration(String token) {
        return extractClaims(token)
                .getExpiration()
                .toInstant();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
