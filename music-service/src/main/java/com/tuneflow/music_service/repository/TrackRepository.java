package com.tuneflow.music_service.repository;

import com.tuneflow.music_service.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {

    Optional<Track> findByIdAndActiveTrue(UUID id);

    List<Track> findAllByActiveTrue();

    Optional<Track> findByTitleIgnoreCaseAndActiveTrue(String title);
}
