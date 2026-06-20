package com.tuneflow.auth_service.dto.response;

import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String username
) {
}
