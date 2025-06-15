package com.nurtikaga.CTO.service;

import com.nurtikaga.CTO.dto.RequestDTO;
import com.nurtikaga.CTO.dto.StatusChangeDTO;
import com.nurtikaga.CTO.exception.RequestNotFoundException;
import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.model.RequestStatus;
import com.nurtikaga.CTO.model.StatusHistory;
import com.nurtikaga.CTO.repository.RequestRepository;
import com.nurtikaga.CTO.repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "requests")
public class RequestService {
    private static final String REQUEST_TOPIC = "request.events";

    private final RequestRepository requestRepository; // инжектированный экземпляр (с маленькой буквы)
    private final StatusHistoryRepository statusHistoryRepository;
    private final NotificationService notificationService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @CacheEvict(allEntries = true)
    public Request createRequest(RequestDTO requestDTO) {
        Request request = Request.builder()
                .clientName(requestDTO.getClientName())
                .clientPhone(requestDTO.getClientPhone())
                .problemDescription(requestDTO.getProblemDescription())
                .status(RequestStatus.CREATED)
                .build();

        Request savedRequest = requestRepository.save(request); // правильно - через экземпляр

        kafkaTemplate.send(REQUEST_TOPIC, "request.created",
                "Заявка создана: " + savedRequest.getId());

        StatusHistory history = StatusHistory.builder()
                .request(savedRequest)
                .oldStatus(null)
                .newStatus(RequestStatus.CREATED)
                .changeDate(LocalDateTime.now())
                .changeReason("Заявка создана")
                .changedBy("Система")
                .build();

        statusHistoryRepository.save(history);
        return savedRequest;
    }
    @Transactional
    public Request changeStatus(Long requestId, StatusChangeDTO statusChangeDTO) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Заявка не найдена: " + requestId));

        RequestStatus oldStatus = request.getStatus();
        RequestStatus newStatus = statusChangeDTO.getNewStatus();

        if (oldStatus == newStatus) {
            return request;
        }

        validateStatusTransition(oldStatus, newStatus);

        request.setStatus(newStatus);
        Request updatedRequest = requestRepository.save(request);

        // Отправка события в Kafka (если используется)
        if (kafkaTemplate != null) {
            kafkaTemplate.send(REQUEST_TOPIC, "request.status.changed",
                    String.format("Заявка %d: %s -> %s", requestId, oldStatus, newStatus));
        }

        // Запись истории
        StatusHistory history = StatusHistory.builder()
                .request(updatedRequest)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changeDate(LocalDateTime.now())
                .changeReason(statusChangeDTO.getChangeReason())
                .changedBy(statusChangeDTO.getChangedBy())
                .build();
        statusHistoryRepository.save(history);

        if (newStatus == RequestStatus.COMPLETED) {
            notificationService.notifyClient(requestId, request.getClientPhone());
        }

        return updatedRequest;
    }

    @Cacheable(key = "#clientPhone")
    public List<Request> getRequestsByClient(String clientPhone) {
        return requestRepository.findByClientPhone(clientPhone); // правильно - через экземпляр
    }

    @Cacheable(key = "#status.name()")
    public List<Request> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status); // правильно - через экземпляр
    }


    public List<StatusHistory> getStatusHistory(Long requestId) {
        return statusHistoryRepository.findByRequestId(requestId);
    }

    private void validateStatusTransition(RequestStatus oldStatus, RequestStatus newStatus) {
        if (oldStatus == RequestStatus.CREATED && newStatus == RequestStatus.COMPLETED) {
            throw new IllegalArgumentException("Недопустимый переход статуса: CREATED -> COMPLETED");
        }
    }
}