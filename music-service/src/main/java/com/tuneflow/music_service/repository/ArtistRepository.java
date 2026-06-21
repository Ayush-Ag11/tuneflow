package com.tuneflow.music_service.repository;

import com.tuneflow.music_service.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Optional<Artist> findByName(String name);

    boolean existsByName(String name);
}