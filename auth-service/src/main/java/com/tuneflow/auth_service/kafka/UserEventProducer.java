package com.tuneflow.auth_service.kafka;

import com.tuneflow.auth_service.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public void publishUserRegistered(UserRegisteredEvent event) {

        String key = event.userId().toString();

        kafkaTemplate.send("user.registered", key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish UserRegisteredEvent for userId={}: {}",
                                event.userId(), ex.getMessage());
                    } else {
                        log.info("Published UserRegisteredEvent: userId={}, partition={}, offset={}",
                                event.userId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}