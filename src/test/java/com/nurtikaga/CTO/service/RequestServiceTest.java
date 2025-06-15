package com.nurtikaga.CTO.service;

import com.nurtikaga.CTO.dto.RequestDTO;
import com.nurtikaga.CTO.dto.StatusChangeDTO;
import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.model.RequestStatus;
import com.nurtikaga.CTO.model.StatusHistory;
import com.nurtikaga.CTO.repository.RequestRepository;
import com.nurtikaga.CTO.repository.StatusHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private StatusHistoryRepository statusHistoryRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private RequestService requestService;

    @Test
    void createRequest_ShouldSaveRequest() {
        RequestDTO dto = new RequestDTO();
        dto.setClientName("Test Client");
        dto.setClientPhone("+1234567890");
        dto.setProblemDescription("Test problem");

        // Используем CompletableFuture вместо SettableListenableFuture
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(future);

        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Request result = requestService.createRequest(dto);

        assertNotNull(result);
        assertEquals(RequestStatus.CREATED, result.getStatus());
    }

    @Test
    void changeStatus_ShouldUpdateStatus() {
        Request request = new Request();
        request.setId(1L);
        request.setStatus(RequestStatus.CREATED);

        // Используем CompletableFuture вместо SettableListenableFuture
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(future);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StatusChangeDTO changeDTO = new StatusChangeDTO();
        changeDTO.setNewStatus(RequestStatus.IN_PROGRESS);
        changeDTO.setChangeReason("Test reason");
        changeDTO.setChangedBy("Test user");

        Request result = requestService.changeStatus(1L, changeDTO);

        assertEquals(RequestStatus.IN_PROGRESS, result.getStatus());
    }
}