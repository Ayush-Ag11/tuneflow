package com.tuneflow.music_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UpdateTrackRequest {

    @NotBlank(message = "Track title is required")
    private String title;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than 0")
    private Integer durationInSeconds;

    @NotBlank(message = "Audio URL is required")
    private String audioUrl;

    private String coverImageUrl;

    private UUID albumId;

    @NotEmpty(message = "At least one artist is required")
    private Set<UUID> artistIds;

    @NotEmpty(message = "At least one genre is required")
    private Set<UUID> genreIds;
}