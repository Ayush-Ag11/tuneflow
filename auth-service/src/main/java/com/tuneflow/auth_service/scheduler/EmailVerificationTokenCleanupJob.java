package com.tuneflow.auth_service.scheduler;

import com.tuneflow.auth_service.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationTokenCleanupJob {

    private final EmailVerificationTokenRepository
            emailVerificationTokenRepository;

    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Kolkata")
    @Transactional
    public void deleteExpiredTokens() {

        int deleted = emailVerificationTokenRepository
                .deleteAllExpiredTokens(Instant.now());

        log.info(
                "Email verification cleanup: deleted {} expired token(s)",
                deleted
        );
    }
}