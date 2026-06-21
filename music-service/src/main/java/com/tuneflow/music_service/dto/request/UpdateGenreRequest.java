package com.tuneflow.music_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateGenreRequest {

    @NotBlank(message = "Genre name is required")
    @Size(max = 100, message = "Genre name cannot exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}