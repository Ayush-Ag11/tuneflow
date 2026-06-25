package com.tuneflow.music_service.service;

import com.tuneflow.music_service.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileUploadResponse uploadArtistImage(MultipartFile file);

    FileUploadResponse uploadAlbumCover(MultipartFile file);

    FileUploadResponse uploadTrack(MultipartFile file);

    FileUploadResponse uploadTrackCover(MultipartFile file);

    void deleteFile(String fileUrl);
}