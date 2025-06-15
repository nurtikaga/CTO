package com.nurtikaga.CTO.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
    public void notifyClient(Long requestId, String clientPhone) {
        String message = String.format("Уведомление клиенту %s: Ваша заявка %d завершена.", clientPhone, requestId);
        log.info("Mock SMS отправлен: {}", message);
    }
}
