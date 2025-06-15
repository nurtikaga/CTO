package com.nurtikaga.CTO.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RequestEventConsumer {

    @KafkaListener(topics = "request.events", groupId = "cto-group")
    public void handleRequestEvent(String message) {
        log.info("Получено событие: {}", message);

    }
}