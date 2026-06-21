package com.tuneflow.music_service.service.impl;

import com.tuneflow.music_service.dto.request.CreateArtistRequest;
import com.tuneflow.music_service.dto.request.UpdateArtistRequest;
import com.tuneflow.music_service.dto.response.ArtistResponse;
import com.tuneflow.music_service.entity.Artist;
import com.tuneflow.music_service.exception.DuplicateResourceException;
import com.tuneflow.music_service.exception.ResourceNotFoundException;
import com.tuneflow.music_service.repository.ArtistRepository;
import com.tuneflow.music_service.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    public ArtistResponse createArtist(CreateArtistRequest request) {

        if (artistRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Artist already exists with name: "
                    + request.name());
        }

        Artist artist = new Artist();

        artist.setName(request.name());
        artist.setBio(request.bio());
        artist.setImageUrl(request.imageUrl());

        Artist savedArtist = artistRepository.save(artist);

        return mapToResponse(savedArtist);
    }

    @Override
    public ArtistResponse getArtistById(UUID artistId) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist not found with id: " + artistId));

        return mapToResponse(artist);
    }

    @Override
    public List<ArtistResponse> getAllArtists() {

        return artistRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ArtistResponse updateArtist(UUID artistId, UpdateArtistRequest request) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist not found with id: " + artistId));

        if (request.name() != null) {
            artist.setName(request.name());
        }

        if (request.bio() != null) {
            artist.setBio(request.bio());
        }

        if (request.imageUrl() != null) {
            artist.setImageUrl(request.imageUrl());
        }

        if (request.verified() != null) {
            artist.setVerified(request.verified());
        }

        Artist updatedArtist = artistRepository.save(artist);

        return mapToResponse(updatedArtist);
    }

    @Override
    public void deactivateArtist(UUID artistId) {

        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Artist not found with id: " + artistId));

        artist.setActive(false);

        artistRepository.save(artist);
    }

    private ArtistResponse mapToResponse(Artist artist) {

        return new ArtistResponse(artist.getId(),
                artist.getName(), artist.getBio(),
                artist.getImageUrl(), artist.isVerified(),
                artist.isActive(), artist.getCreatedAt());
    }
}