package com.tuneflow.music_service.dto.response;

import com.tuneflow.music_service.enums.AlbumType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AlbumResponse {

    private UUID id;

    private String title;

    private AlbumType type;

    private LocalDate releaseDate;

    private String coverImageUrl;

    private boolean active;

    private UUID artistId;

    private String artistName;

    private Instant createdAt;

    private Instant updatedAt;
}