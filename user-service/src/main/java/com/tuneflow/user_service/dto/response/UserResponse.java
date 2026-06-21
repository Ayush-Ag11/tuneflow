package com.tuneflow.user_service.dto.response;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,

        String username,

        String email,

        String firstName,

        String lastName,

        String bio,

        String profileImageUrl,

        Boolean active,

        Instant createdAt
) {
}