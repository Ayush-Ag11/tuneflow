package com.tuneflow.music_service.repository;

import com.tuneflow.music_service.entity.Artist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Optional<Artist> findByIdAndActiveTrue(UUID id);

    List<Artist> findAllByActiveTrue();

    Optional<Artist> findByNameIgnoreCase(@NotBlank @Size(max = 255) String name);
}