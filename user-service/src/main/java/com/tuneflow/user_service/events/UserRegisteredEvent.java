package com.tuneflow.user_service.events;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String username,
        String email,
        Instant occurredAt
) {
}
