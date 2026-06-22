package com.tuneflow.music_service.dto.response;

import java.util.UUID;

public record GenreSummaryResponse(
        UUID id,
        String name
) {
}
