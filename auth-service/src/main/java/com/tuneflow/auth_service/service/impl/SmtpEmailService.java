package com.tuneflow.auth_service.service.impl;

import com.tuneflow.auth_service.entity.User;
import com.tuneflow.auth_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendVerificationEmail(User user, String verificationToken) {

        String verificationLink = baseUrl
                + "/api/v1/auth/verify-email?token="
                + verificationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getEmail());
        message.setSubject("Verify your TuneFlow account");
        message.setText("""
                Hi %s,
                
                Please verify your TuneFlow account by clicking the link below:
                
                %s
                
                This link expires in 24 hours.
                
                If you did not create an account, you can safely ignore this email.
                
                — The TuneFlow Team
                """.formatted(user.getUsername(), verificationLink));

        mailSender.send(message);

        log.info("Verification email sent to {}", user.getEmail());
    }
}