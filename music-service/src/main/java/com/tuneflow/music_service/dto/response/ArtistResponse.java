package com.tuneflow.music_service.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ArtistResponse(
        UUID id,

        String name,

        String bio,

        String imageUrl,

        boolean verified,

        boolean active,

        Instant createdAt
) {
}
