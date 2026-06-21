package com.tuneflow.music_service.controller;

import com.tuneflow.music_service.dto.request.CreateGenreRequest;
import com.tuneflow.music_service.dto.request.UpdateGenreRequest;
import com.tuneflow.music_service.dto.response.ApiResponse;
import com.tuneflow.music_service.dto.response.GenreResponse;
import com.tuneflow.music_service.service.GenreService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<GenreResponse>> createGenre(
            @Valid @RequestBody CreateGenreRequest request
    ) {

        GenreResponse response = genreService.createGenre(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                HttpStatus.CREATED.value(),
                                "Genre created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GenreResponse>> getGenreById(
            @PathVariable UUID id
    ) {

        GenreResponse response = genreService.getGenreById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Genre fetched successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GenreResponse>>> getAllGenres() {

        List<GenreResponse> responses = genreService.getAllGenres();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Genre fetched successfully",
                        responses
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GenreResponse>> updateGenre(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGenreRequest request
    ) {

        GenreResponse response = genreService.updateGenre(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Genre updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGenre(
            @PathVariable UUID id
    ) {

        genreService.deleteGenre(id);

        return ResponseEntity.noContent().build();
    }
}