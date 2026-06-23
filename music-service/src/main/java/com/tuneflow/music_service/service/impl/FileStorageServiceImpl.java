package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.config.MinioProperties;
import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.service.FileStorageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @PostConstruct
    public void initializeBuckets() {

        createBucketIfNotExists(
                minioProperties.getBuckets().getArtists());

        createBucketIfNotExists(
                minioProperties.getBuckets().getAlbums());

        createBucketIfNotExists(
                minioProperties.getBuckets().getTracks());
    }

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

    private void createBucketIfNotExists(
            String bucketName) {

        try {

            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());

            if (exists) {

                log.info("Bucket already exists: {}", bucketName);
                return;
            }

            minioClient.makeBucket(MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());

            log.info("Created bucket: {}", bucketName);

        } catch (Exception ex) {

            log.error("Failed to initialize bucket: {}", bucketName, ex);
            throw new RuntimeException("Failed to initialize MinIO bucket: " + bucketName, ex);
        }
    }
}
