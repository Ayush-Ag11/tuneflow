package com.tuneflow.music_service.controller;

import com.tuneflow.music_service.dto.request.CreateAlbumRequest;
import com.tuneflow.music_service.dto.request.UpdateAlbumRequest;
import com.tuneflow.music_service.dto.response.AlbumResponse;
import com.tuneflow.music_service.dto.response.ApiResponse;
import com.tuneflow.music_service.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<ApiResponse<AlbumResponse>> createAlbum(
            @Valid @RequestBody CreateAlbumRequest request
    ) {

        AlbumResponse response = albumService.createAlbum(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                HttpStatus.CREATED.value(),
                                "Album created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumResponse>> getAlbumById(
            @PathVariable UUID id
    ) {

        AlbumResponse response = albumService.getAlbumById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Album fetched successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AlbumResponse>>> getAllAlbums() {

        List<AlbumResponse> response = albumService.getAllAlbums();

        return ResponseEntity.ok(
               new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Albums fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<AlbumResponse>>> getAlbumsByArtist(
            @PathVariable UUID artistId
    ) {

        List<AlbumResponse> response =
                albumService.getAlbumsByArtist(artistId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Albums fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumResponse>> updateAlbum(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAlbumRequest request
    ) {

        AlbumResponse response =
                albumService.updateAlbum(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Album updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAlbum(
            @PathVariable UUID id
    ) {

        albumService.deleteAlbum(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Album deleted successfully",
                        null
                )
        );
    }
}