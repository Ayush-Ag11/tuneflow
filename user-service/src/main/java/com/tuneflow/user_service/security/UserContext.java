package com.tuneflow.user_service.security;

import com.tuneflow.user_service.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserContext {

    private final HttpServletRequest request;

    public UUID getCurrentUserId() {

        String userId = request.getHeader("X-User-Id");

        if (userId == null || userId.isBlank()) {
            throw new UnauthorizedException("Missing X-User-Id header");
        }

        return UUID.fromString(userId);
    }

    public String getCurrentUserName() {
        return request.getHeader("X-User-Name");
    }

    public String getCurrentUserEmail() {
        return request.getHeader("X-User-Email");
    }
}
