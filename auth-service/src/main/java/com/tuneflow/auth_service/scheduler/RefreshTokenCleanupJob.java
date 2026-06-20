package com.tuneflow.auth_service.scheduler;

import com.tuneflow.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCleanupJob {

    private final RefreshTokenRepository
            refreshTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupRefreshTokens() {

        log.info(
                "Starting refresh token cleanup"
        );

        long expiredDeleted = refreshTokenRepository
                .deleteByExpiresAtBefore(
                        Instant.now()
                );

        long revokedDeleted = refreshTokenRepository
                .deleteByRevokedTrue();

        log.info(
                "Deleted {} expired and {} revoked refresh tokens",
                expiredDeleted,
                revokedDeleted
        );
    }
}