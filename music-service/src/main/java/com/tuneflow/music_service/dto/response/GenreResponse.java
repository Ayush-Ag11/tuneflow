package com.tuneflow.music_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class GenreResponse {

    private UUID id;

    private String name;

    private String description;

    private boolean active;

    private Instant createdAt;

    private Instant updatedAt;
}