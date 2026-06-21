package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.dto.request.CreateGenreRequest;
import com.tuneflow.music_service.dto.request.UpdateGenreRequest;
import com.tuneflow.music_service.dto.response.GenreResponse;
import com.tuneflow.music_service.entity.Genre;
import com.tuneflow.music_service.exception.DuplicateResourceException;
import com.tuneflow.music_service.exception.ResourceNotFoundException;
import com.tuneflow.music_service.repository.GenreRepository;
import com.tuneflow.music_service.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public GenreResponse createGenre(CreateGenreRequest request) {

        genreRepository.findByNameIgnoreCase(request.getName().trim())
                .ifPresent(genre -> {
                    throw new DuplicateResourceException(
                            "Genre already exists with name: " + request.getName()
                    );
                });

        Genre genre = Genre.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .active(true)
                .build();

        Genre savedGenre = genreRepository.save(genre);

        return mapToResponse(savedGenre);
    }

    @Override
    public GenreResponse getGenreById(UUID genreId) {
        return mapToResponse(getActiveGenre(genreId));
    }

    @Override
    public List<GenreResponse> getAllGenres() {

        return genreRepository.findAllByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public GenreResponse updateGenre(UUID genreId,
                                     UpdateGenreRequest request) {

        Genre genre = getActiveGenre(genreId);

        String updatedName = request.getName().trim();

        genreRepository.findByNameIgnoreCase(updatedName)
                .ifPresent(existingGenre -> {

                    if (!existingGenre.getId().equals(genreId)) {
                        throw new DuplicateResourceException(
                                "Genre already exists with name: " + updatedName
                        );
                    }
                });

        genre.setName(updatedName);
        genre.setDescription(request.getDescription());

        Genre updatedGenre = genreRepository.save(genre);

        return mapToResponse(updatedGenre);
    }

    @Override
    public void deleteGenre(UUID genreId) {
        Genre genre = getActiveGenre(genreId);

        genre.setActive(false);

        genreRepository.save(genre);
    }

    private GenreResponse mapToResponse(Genre genre) {

        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .description(genre.getDescription())
                .active(genre.isActive())
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .build();
    }

    private Genre getActiveGenre(UUID genreId) {
        return genreRepository.findByIdAndActiveTrue(genreId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Genre not found with id: " + genreId
                        ));
    }
}
