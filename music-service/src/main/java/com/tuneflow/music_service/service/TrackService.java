package com.tuneflow.music_service.service;

import com.tuneflow.music_service.dto.request.CreateTrackRequest;
import com.tuneflow.music_service.dto.request.UpdateTrackRequest;
import com.tuneflow.music_service.dto.response.TrackResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface TrackService {

    TrackResponse createTrack(CreateTrackRequest request);

    TrackResponse updateTrack(UUID trackId, UpdateTrackRequest request);

    TrackResponse getTrackById(UUID trackId);

    List<TrackResponse> getAllTracks();

    List<TrackResponse> getTracksByArtist(UUID artistId);

    List<TrackResponse> getTracksByAlbum(UUID albumId);

    List<TrackResponse> getTracksByGenre(UUID genreId);

    void deleteTrack(UUID trackId);

    TrackResponse uploadTrackAudio(UUID trackId, MultipartFile file);

    TrackResponse uploadTrackCover(UUID trackId, MultipartFile file);
}
