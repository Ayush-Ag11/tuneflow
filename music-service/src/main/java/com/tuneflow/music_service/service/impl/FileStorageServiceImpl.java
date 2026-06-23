package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.service.FileStorageService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;

    @Override
    public FileUploadResponse uploadArtistImage(MultipartFile file) {
        throw new UnsupportedOperationException(
                "Not implemented yet");
    }

    @Override
    public FileUploadResponse uploadAlbumCover(MultipartFile file) {
        throw new UnsupportedOperationException(
                "Not implemented yet");
    }

    @Override
    public FileUploadResponse uploadTrack(MultipartFile file) {
        throw new UnsupportedOperationException(
                "Not implemented yet");
    }
}
