package com.tuneflow.music_service.dto.response;

public record FileUploadResponse(
        String fileName,
        String fileUrl
) {
}