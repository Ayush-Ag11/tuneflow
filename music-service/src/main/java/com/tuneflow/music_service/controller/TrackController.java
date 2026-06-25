package com.tuneflow.music_service.controller;

import com.tuneflow.music_service.dto.request.CreateTrackRequest;
import com.tuneflow.music_service.dto.request.UpdateTrackRequest;
import com.tuneflow.music_service.dto.response.ApiResponse;
import com.tuneflow.music_service.dto.response.TrackResponse;
import com.tuneflow.music_service.service.TrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @PostMapping
    public ResponseEntity<ApiResponse<TrackResponse>> createTrack(@Valid @RequestBody CreateTrackRequest request) {

        TrackResponse response = trackService.createTrack(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                HttpStatus.CREATED.value(),
                                "Track created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrackResponse>> getTrackById(@PathVariable UUID id) {

        TrackResponse response = trackService.getTrackById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track fetched successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getAllTracks() {

        List<TrackResponse> response = trackService.getAllTracks();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Tracks fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getTracksByArtist(@PathVariable UUID artistId) {

        List<TrackResponse> response = trackService.getTracksByArtist(artistId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Tracks fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getTracksByAlbum(@PathVariable UUID albumId) {

        List<TrackResponse> response = trackService.getTracksByAlbum(albumId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Tracks fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/genre/{genreId}")
    public ResponseEntity<ApiResponse<List<TrackResponse>>> getTracksByGenre(@PathVariable UUID genreId) {

        List<TrackResponse> response = trackService.getTracksByGenre(genreId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Tracks fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrackResponse>> updateTrack(@PathVariable UUID id,
                                                                  @Valid @RequestBody UpdateTrackRequest request) {

        TrackResponse response = trackService.updateTrack(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTrack(@PathVariable UUID id) {

        trackService.deleteTrack(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track deleted successfully",
                        null
                )
        );
    }

    @PostMapping("/{trackId}/track")
    public ResponseEntity<ApiResponse<TrackResponse>> uploadTrack(@PathVariable UUID trackId,
                                                                  @RequestParam("file") MultipartFile file) {

        TrackResponse response = trackService.uploadTrackAudio(trackId, file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track Audio uploaded successfully",
                        response
                )
        );
    }

    @PostMapping("/{trackId}/cover")
    public ResponseEntity<ApiResponse<TrackResponse>>
    uploadTrackCover(
            @PathVariable UUID trackId,
            @RequestParam("file")
            MultipartFile file) {

        TrackResponse response =
                trackService.uploadTrackCover(
                        trackId,
                        file);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Track cover uploaded successfully",
                        response
                )
        );
    }
}
