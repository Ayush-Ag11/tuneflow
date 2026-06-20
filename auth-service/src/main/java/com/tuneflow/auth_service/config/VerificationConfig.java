package com.tuneflow.auth_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VerificationProperties.class)
public class VerificationConfig {
}
