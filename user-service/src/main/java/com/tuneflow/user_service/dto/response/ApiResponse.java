package com.tuneflow.user_service.dto.response;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
}
