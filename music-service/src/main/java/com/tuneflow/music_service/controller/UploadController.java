package com.tuneflow.music_service.controller;

import com.tuneflow.music_service.dto.response.ApiResponse;
import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/artists")
    public ResponseEntity<ApiResponse<FileUploadResponse>>
    uploadArtistImage(@RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileStorageService.uploadArtistImage(file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Artist image uploaded successfully",
                        response
                )
        );
    }

    @PostMapping("/albums")
    public ResponseEntity<ApiResponse<FileUploadResponse>>
    uploadAlbumCover(@RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileStorageService.uploadAlbumCover(file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Album cover uploaded successfully",
                        response
                )
        );
    }

    @PostMapping("/tracks")
    public ResponseEntity<ApiResponse<FileUploadResponse>>
    uploadTrack(@RequestParam("file") MultipartFile file) {

        FileUploadResponse response = fileStorageService.uploadTrack(file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track uploaded successfully",
                        response
                )
        );
    }
}