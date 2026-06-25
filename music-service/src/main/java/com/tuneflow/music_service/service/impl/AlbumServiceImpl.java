package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.dto.request.CreateAlbumRequest;
import com.tuneflow.music_service.dto.request.UpdateAlbumRequest;
import com.tuneflow.music_service.dto.response.AlbumResponse;
import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.entity.Album;
import com.tuneflow.music_service.entity.Artist;
import com.tuneflow.music_service.exception.DuplicateResourceException;
import com.tuneflow.music_service.exception.ResourceNotFoundException;
import com.tuneflow.music_service.repository.AlbumRepository;
import com.tuneflow.music_service.repository.ArtistRepository;
import com.tuneflow.music_service.service.AlbumService;
import com.tuneflow.music_service.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final FileStorageService fileStorageService;

    @Override
    public AlbumResponse createAlbum(CreateAlbumRequest request) {

        Artist artist =
                getActiveArtist(request.getArtistId());

        validateDuplicateAlbum(request.getTitle(), request.getArtistId());

        Album album = Album.builder()
                .title(request.getTitle().trim())
                .type(request.getType())
                .releaseDate(request.getReleaseDate())
                .coverImageUrl(request.getCoverImageUrl())
                .artist(artist)
                .build();

        return mapToResponse(
                albumRepository.save(album)
        );
    }

    @Override
    public AlbumResponse getAlbumById(UUID albumId) {

        return mapToResponse(
                getActiveAlbum(albumId)
        );
    }

    @Override
    public List<AlbumResponse> getAllAlbums() {

        return albumRepository
                .findAllByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AlbumResponse> getAlbumsByArtist(UUID artistId) {

        getActiveArtist(artistId);

        return albumRepository
                .findByArtistIdAndActiveTrue(artistId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AlbumResponse updateAlbum(
            UUID albumId,
            UpdateAlbumRequest request
    ) {

        Album album = getActiveAlbum(albumId);

        Artist artist =
                getActiveArtist(request.getArtistId());

        validateDuplicateAlbumForUpdate(
                albumId,
                request.getTitle(),
                request.getArtistId()
        );

        album.setTitle(request.getTitle().trim());
        album.setType(request.getType());
        album.setReleaseDate(request.getReleaseDate());
        album.setCoverImageUrl(request.getCoverImageUrl());
        album.setArtist(artist);

        return mapToResponse(
                albumRepository.save(album)
        );
    }

    @Override
    public void deleteAlbum(UUID albumId) {

        Album album = getActiveAlbum(albumId);

        album.setActive(false);

        albumRepository.save(album);
    }

    @Override
    public AlbumResponse updateAlbumCover(UUID albumId, MultipartFile file) {

        Album album = albumRepository.findById(albumId).orElseThrow(() ->
                new ResourceNotFoundException("Album Not Found with ID: " + albumId)
        );

        String oldCoverUrl = album.getCoverImageUrl();

        FileUploadResponse uploadResponse =
                fileStorageService.uploadAlbumCover(file);

        album.setCoverImageUrl(uploadResponse.fileUrl());

        Album updatedAlbum = albumRepository.save(album);

        fileStorageService.deleteFile(oldCoverUrl);

        return mapToResponse(updatedAlbum);
    }

    private Artist getActiveArtist(UUID artistId) {

        return artistRepository
                .findByIdAndActiveTrue(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Artist not found with id: " + artistId
                        ));
    }

    private Album getActiveAlbum(UUID albumId) {

        return albumRepository
                .findByIdAndActiveTrue(albumId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Album not found with id: " + albumId
                        ));
    }

    private AlbumResponse mapToResponse(Album album) {

        return AlbumResponse.builder()
                .id(album.getId())
                .title(album.getTitle())
                .type(album.getType())
                .releaseDate(album.getReleaseDate())
                .coverImageUrl(album.getCoverImageUrl())
                .active(album.isActive())
                .artistId(album.getArtist().getId())
                .artistName(album.getArtist().getName())
                .createdAt(album.getCreatedAt())
                .updatedAt(album.getUpdatedAt())
                .build();
    }

    private void validateDuplicateAlbum(
            String title,
            UUID artistId
    ) {

        albumRepository
                .findByTitleIgnoreCaseAndArtistIdAndActiveTrue(
                        title.trim(),
                        artistId
                )
                .ifPresent(album -> {
                    throw new DuplicateResourceException(
                            "Album already exists with title: " + title
                    );
                });
    }

    private void validateDuplicateAlbumForUpdate(
            UUID albumId,
            String title,
            UUID artistId
    ) {

        albumRepository
                .findByTitleIgnoreCaseAndArtistIdAndActiveTrue(
                        title.trim(),
                        artistId
                )
                .ifPresent(existingAlbum -> {

                    if (!existingAlbum.getId().equals(albumId)) {
                        throw new DuplicateResourceException(
                                "Album already exists with title: " + title
                        );
                    }
                });
    }
}
