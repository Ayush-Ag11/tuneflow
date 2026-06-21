package com.tuneflow.music_service.dto.response;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
}
