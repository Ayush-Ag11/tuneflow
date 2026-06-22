package com.tuneflow.music_service.dto.request;

import com.tuneflow.music_service.enums.AlbumType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateAlbumRequest {

    @NotBlank(message = "Album title is required")
    @Size(max = 200, message = "Album title cannot exceed 200 characters")
    private String title;

    @NotNull(message = "Album type is required")
    private AlbumType type;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    private String coverImageUrl;

    @NotNull(message = "Artist id is required")
    private UUID artistId;
}