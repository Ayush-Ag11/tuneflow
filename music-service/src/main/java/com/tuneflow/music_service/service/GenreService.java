package com.tuneflow.music_service.service;

import com.tuneflow.music_service.dto.request.CreateGenreRequest;
import com.tuneflow.music_service.dto.request.UpdateGenreRequest;
import com.tuneflow.music_service.dto.response.GenreResponse;

import java.util.List;
import java.util.UUID;

public interface GenreService {

    GenreResponse createGenre(CreateGenreRequest request);

    GenreResponse getGenreById(UUID genreId);

    List<GenreResponse> getAllGenres();

    GenreResponse updateGenre(UUID genreId,
                              UpdateGenreRequest request);

    void deleteGenre(UUID genreId);
}