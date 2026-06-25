package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.dto.request.CreateTrackRequest;
import com.tuneflow.music_service.dto.request.UpdateTrackRequest;
import com.tuneflow.music_service.dto.response.ArtistSummaryResponse;
import com.tuneflow.music_service.dto.response.FileUploadResponse;
import com.tuneflow.music_service.dto.response.GenreSummaryResponse;
import com.tuneflow.music_service.dto.response.TrackResponse;
import com.tuneflow.music_service.entity.Album;
import com.tuneflow.music_service.entity.Artist;
import com.tuneflow.music_service.entity.Genre;
import com.tuneflow.music_service.entity.Track;
import com.tuneflow.music_service.exception.DuplicateResourceException;
import com.tuneflow.music_service.exception.ResourceNotFoundException;
import com.tuneflow.music_service.repository.AlbumRepository;
import com.tuneflow.music_service.repository.ArtistRepository;
import com.tuneflow.music_service.repository.GenreRepository;
import com.tuneflow.music_service.repository.TrackRepository;
import com.tuneflow.music_service.service.FileStorageService;
import com.tuneflow.music_service.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final FileStorageService fileStorageService;

    @Override
    public TrackResponse createTrack(
            CreateTrackRequest request
    ) {

        validateDuplicateTrack(request.getTitle());

        Set<Artist> artists = getActiveArtists(request.getArtistIds());

        Set<Genre> genres = getActiveGenres(request.getGenreIds());

        Album album = null;

        if (request.getAlbumId() != null) {

            album = getActiveAlbum(request.getAlbumId());

            validateAlbumArtistRelation(album, artists);
        }

        Track track = Track.builder()
                .title(request.getTitle().trim())
                .durationInSeconds(request.getDurationInSeconds())
                .audioUrl(request.getAudioUrl())
                .coverImageUrl(request.getCoverImageUrl())
                .album(album)
                .artists(artists)
                .genres(genres)
                .build();

        return mapToResponse(trackRepository.save(track));
    }

    @Override
    public TrackResponse updateTrack(UUID trackId, UpdateTrackRequest request) {

        Track track = getActiveTrack(trackId);

        validateDuplicateTrackForUpdate(trackId, request.getTitle());

        Set<Artist> artists = getActiveArtists(request.getArtistIds());

        Set<Genre> genres = getActiveGenres(request.getGenreIds());

        Album album = null;

        if (request.getAlbumId() != null) {

            album = getActiveAlbum(request.getAlbumId());

            validateAlbumArtistRelation(album, artists);
        }

        track.setTitle(request.getTitle().trim());

        track.setDurationInSeconds(request.getDurationInSeconds());

        track.setAudioUrl(request.getAudioUrl());

        track.setCoverImageUrl(request.getCoverImageUrl());

        track.setAlbum(album);

        track.setArtists(artists);

        track.setGenres(genres);

        return mapToResponse(trackRepository.save(track)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public TrackResponse getTrackById(UUID trackId) {

        return mapToResponse(getActiveTrack(trackId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponse> getAllTracks() {

        return trackRepository
                .findAllByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponse> getTracksByArtist(UUID artistId) {

        return trackRepository
                .findByArtistsIdAndActiveTrue(artistId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponse> getTracksByAlbum(UUID albumId) {

        return trackRepository.findByAlbumIdAndActiveTrue(albumId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponse> getTracksByGenre(UUID genreId) {

        return trackRepository
                .findByGenresIdAndActiveTrue(genreId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteTrack(UUID trackId) {

        Track track = getActiveTrack(trackId);

        track.setActive(false);

        trackRepository.save(track);
    }

    @Override
    public TrackResponse uploadTrackAudio(UUID trackId, MultipartFile file) {

        Track track = trackRepository.findById(trackId).orElseThrow(() ->
                new ResourceNotFoundException("Track Not Found with ID: " + trackId));

        FileUploadResponse fileUploadResponse = fileStorageService.uploadTrack(file);

        track.setAudioUrl(fileUploadResponse.fileUrl());

        Track updatedTrack = trackRepository.save(track);

        return mapToResponse(updatedTrack);
    }

    @Override
    public TrackResponse uploadTrackCover(
            UUID trackId,
            MultipartFile file) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Track not found with id: "
                                        + trackId));

        FileUploadResponse uploadResponse =
                fileStorageService.uploadTrackCover(file);

        track.setCoverImageUrl(
                uploadResponse.fileUrl());

        Track updatedTrack =
                trackRepository.save(track);

        return mapToResponse(updatedTrack);
    }

    private Track getActiveTrack(UUID trackId) {

        return trackRepository.findByIdAndActiveTrue(trackId).orElseThrow(() ->
                new ResourceNotFoundException("Track not found with id: " + trackId));
    }

    private Album getActiveAlbum(UUID albumId) {

        return albumRepository.findByIdAndActiveTrue(albumId).orElseThrow(() ->
                new ResourceNotFoundException("Album not found with id: " + albumId));
    }

    private Set<Artist> getActiveArtists(Set<UUID> artistIds) {

        Set<Artist> artists = new HashSet<>();

        for (UUID artistId : artistIds) {
            Artist artist = artistRepository.findByIdAndActiveTrue(artistId).orElseThrow(() ->
                    new ResourceNotFoundException("Artist not found with id: " + artistId));
            artists.add(artist);
        }

        return artists;
    }

    private Set<Genre> getActiveGenres(Set<UUID> genreIds) {

        Set<Genre> genres = new HashSet<>();

        for (UUID genreId : genreIds) {
            Genre genre = genreRepository.findByIdAndActiveTrue(genreId).orElseThrow(() ->
                    new ResourceNotFoundException("Genre not found with id: " + genreId));
            genres.add(genre);
        }
        return genres;
    }

    private void validateAlbumArtistRelation(Album album, Set<Artist> artists) {

        if (album == null) {
            return;
        }
        boolean containsAlbumArtist = artists.stream().anyMatch(artist ->
                artist.getId().equals(album.getArtist().getId()));

        if (!containsAlbumArtist) {
            throw new IllegalArgumentException("Track artists must contain album artist");
        }
    }

    private void validateDuplicateTrack(String title) {

        if (trackRepository.existsByTitleIgnoreCaseAndActiveTrue(title.trim())) {
            throw new DuplicateResourceException("Track already exists with title: " + title);
        }
    }

    private void validateDuplicateTrackForUpdate(UUID trackId, String title) {

        trackRepository.findByTitleIgnoreCaseAndActiveTrue(title.trim()).ifPresent(
                existingTrack -> {

            if (!existingTrack.getId().equals(trackId)) {
                throw new DuplicateResourceException("Track already exists with title: " + title);
            }
        });
    }

    private TrackResponse mapToResponse(Track track) {

        List<ArtistSummaryResponse> artists =
                track.getArtists()
                        .stream()
                        .map(artist -> new ArtistSummaryResponse(
                                artist.getId(),
                                artist.getName()
                        ))
                        .toList();

        List<GenreSummaryResponse> genres =
                track.getGenres()
                        .stream()
                        .map(genre -> new GenreSummaryResponse(
                                genre.getId(),
                                genre.getName()
                        ))
                        .toList();

        UUID albumId = null;
        String albumTitle = null;

        if (track.getAlbum() != null) {
            albumId = track.getAlbum().getId();
            albumTitle = track.getAlbum().getTitle();
        }

        return new TrackResponse(
                track.getId(),
                track.getTitle(),
                track.getDurationInSeconds(),
                track.getAudioUrl(),
                track.getCoverImageUrl(),
                track.getPlayCount(),
                albumId,
                albumTitle,
                artists,
                genres
        );
    }
}
