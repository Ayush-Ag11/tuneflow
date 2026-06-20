package com.tuneflow.auth_service.service;

import com.tuneflow.auth_service.entity.User;

public interface EmailService {

    void sendVerificationEmail(User user, String verificationToken);
}
