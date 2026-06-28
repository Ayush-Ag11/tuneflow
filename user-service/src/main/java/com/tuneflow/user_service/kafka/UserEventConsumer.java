package com.tuneflow.user_service.kafka;

import com.tuneflow.user_service.dto.request.CreateUserProfileRequest;
import com.tuneflow.user_service.events.UserRegisteredEvent;
import com.tuneflow.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserService userService;

    @KafkaListener(
            topics = "user.registered",
            groupId = "user-service-group"
    )
    public void handleUserRegistered(
            @Payload UserRegisteredEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Consumed UserRegisteredEvent: userId={}, partition={}, offset={}",
                event.userId(), partition, offset);

        try {
            userService.createUserProfile(
                    new CreateUserProfileRequest(
                            event.userId(),
                            event.username(),
                            event.email()
                    )
            );

            log.info("User profile created for userId={}", event.userId());

        } catch (Exception ex) {
            log.error("Failed to create user profile for userId={}: {}",
                    event.userId(), ex.getMessage());
        }
    }
}