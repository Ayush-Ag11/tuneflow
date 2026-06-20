package com.tuneflow.auth_service.service.impl;

import com.tuneflow.auth_service.entity.User;
import com.tuneflow.auth_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
@Slf4j
public class ConsoleEmailService implements EmailService {

    private static final String BASE_URL = "http://localhost:8082/api/v1/auth/verify-email";

    @Override
    public void sendVerificationEmail(User user, String verificationToken) {

        String verificationLink = BASE_URL + "?token=" + verificationToken;

        log.info("""
                
                ==========================================
                EMAIL VERIFICATION
                ==========================================
                
                To      : {}
                
                Verify  : {}
                
                ==========================================
                
                """,
                user.getEmail(),
                verificationLink
        );
    }
}
