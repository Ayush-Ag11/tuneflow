package com.tuneflow.auth_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8, max = 100)
        String password
) {
}
