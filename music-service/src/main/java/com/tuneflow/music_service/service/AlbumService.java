package com.tuneflow.music_service.service;

import com.tuneflow.music_service.dto.request.CreateAlbumRequest;
import com.tuneflow.music_service.dto.request.UpdateAlbumRequest;
import com.tuneflow.music_service.dto.response.AlbumResponse;

import java.util.List;
import java.util.UUID;

public interface AlbumService {

    AlbumResponse createAlbum(CreateAlbumRequest request);

    AlbumResponse getAlbumById(UUID albumId);

    List<AlbumResponse> getAllAlbums();

    List<AlbumResponse> getAlbumsByArtist(UUID artistId);

    AlbumResponse updateAlbum(UUID albumId,
                              UpdateAlbumRequest request);

    void deleteAlbum(UUID albumId);
}