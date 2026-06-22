package com.tuneflow.music_service.dto.response;

import java.util.UUID;

public record ArtistSummaryResponse(
        UUID id,
        String name
) {
}
