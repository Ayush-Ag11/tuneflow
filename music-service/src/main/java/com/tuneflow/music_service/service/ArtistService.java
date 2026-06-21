package com.tuneflow.music_service.service;

import com.tuneflow.music_service.dto.request.CreateArtistRequest;
import com.tuneflow.music_service.dto.request.UpdateArtistRequest;
import com.tuneflow.music_service.dto.response.ArtistResponse;

import java.util.List;
import java.util.UUID;

public interface ArtistService {

    ArtistResponse createArtist(
            CreateArtistRequest request);

    ArtistResponse getArtistById(
            UUID artistId);

    List<ArtistResponse> getAllArtists();

    ArtistResponse updateArtist(
            UUID artistId,
            UpdateArtistRequest request);

    void deactivateArtist(
            UUID artistId);
}
