package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.config.MinioProperties;
import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.exception.FileStorageException;
import com.tuneflow.music_service.service.FileStorageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    private static final Set<String> IMAGE_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private static final Set<String> AUDIO_CONTENT_TYPES =
            Set.of(
                    "audio/mpeg",
                    "audio/mp3",
                    "audio/wav",
                    "audio/x-wav"
            );

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
    public FileUploadResponse uploadArtistImage(
            MultipartFile file) {

        validateContentType(
                file,
                IMAGE_CONTENT_TYPES);

        return uploadFile(
                file,
                minioProperties.getBuckets().getArtists());
    }

    @Override
    public FileUploadResponse uploadAlbumCover(
            MultipartFile file) {

        validateContentType(
                file,
                IMAGE_CONTENT_TYPES);

        return uploadFile(
                file,
                minioProperties.getBuckets().getAlbums());
    }

    @Override
    public FileUploadResponse uploadTrack(
            MultipartFile file) {

        validateContentType(
                file,
                AUDIO_CONTENT_TYPES);

        return uploadFile(
                file,
                minioProperties.getBuckets().getTracks());
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

    private void validateContentType(
            MultipartFile file,
            Set<String> allowedTypes) {

        String contentType =
                file.getContentType();

        if (contentType == null
                || !allowedTypes.contains(contentType)) {

            throw new FileStorageException(
                    "Unsupported file type: "
                            + contentType);
        }
    }

    private String generateFileName(
            String originalFileName) {

        String extension = "";

        if (originalFileName != null
                && originalFileName.contains(".")) {

            extension =
                    originalFileName.substring(
                            originalFileName.lastIndexOf("."));
        }

        return UUID.randomUUID() + extension;
    }

    private FileUploadResponse uploadFile(
            MultipartFile file,
            String bucketName) {

        try {

            String objectName =
                    generateFileName(
                            file.getOriginalFilename());

            minioClient.putObject(
                    io.minio.PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1)
                            .contentType(
                                    file.getContentType())
                            .build());

            String fileUrl =
                    buildFileUrl(
                            bucketName,
                            objectName);

            return new FileUploadResponse(
                    objectName,
                    fileUrl);

        } catch (Exception ex) {

            throw new FileStorageException(
                    "Failed to upload file",
                    ex);
        }
    }

    private String buildFileUrl(
            String bucketName,
            String objectName) {

        return minioProperties.getPublicUrl()
                + "/"
                + bucketName
                + "/"
                + objectName;
    }
}
