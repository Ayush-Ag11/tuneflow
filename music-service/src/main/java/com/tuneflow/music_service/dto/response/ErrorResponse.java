package com.tuneflow.music_service.dto.response;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message
) {
}
