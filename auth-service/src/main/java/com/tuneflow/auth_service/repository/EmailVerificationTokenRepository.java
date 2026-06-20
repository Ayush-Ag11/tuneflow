package com.tuneflow.auth_service.repository;

import com.tuneflow.auth_service.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByToken(String token);

    boolean existsByUserId(UUID userId);

    // Needed for resend — find existing token to delete before issuing new one
    Optional<EmailVerificationToken> findByUserId(UUID userId);

    // Needed for cleanup scheduler
    @Modifying
    @Query("""
            DELETE FROM EmailVerificationToken t
            WHERE t.expiresAt < :now
            """)
    int deleteAllExpiredTokens(@Param("now") Instant now);
}
