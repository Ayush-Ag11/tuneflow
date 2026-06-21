package com.tuneflow.music_service.repository;

import com.tuneflow.music_service.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findByIdAndActiveTrue(UUID uuid);

    List<Genre> findAllByActiveTrue();

    Optional<Genre> findByNameIgnoreCase(String name);
}
