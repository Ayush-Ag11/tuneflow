package com.tuneflow.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(

        @NotBlank
        String refreshToken
) {
}
