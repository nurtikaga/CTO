package com.nurtikaga.CTO.controller;

import com.nurtikaga.CTO.dto.RequestDTO;
import com.nurtikaga.CTO.dto.RequestResponseDTO;
import com.nurtikaga.CTO.dto.StatusChangeDTO;
import com.nurtikaga.CTO.dto.StatusHistoryDTO;
import com.nurtikaga.CTO.exception.RequestNotFoundException;
import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.model.RequestStatus;
import com.nurtikaga.CTO.model.StatusHistory;
import com.nurtikaga.CTO.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j // Добавляем логгирование
public class RequestController {
    private final RequestService requestService;


    @PostMapping
    public ResponseEntity<RequestResponseDTO> createRequest(@Valid @RequestBody RequestDTO requestDTO) {
        log.info("Creating request for client: {}", requestDTO.getClientPhone());
        Request request = requestService.createRequest(requestDTO);
        return ResponseEntity.ok(RequestResponseDTO.fromEntity(request));
    }

    @GetMapping("/client/{clientPhone}")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByClient(@PathVariable String clientPhone) {
        List<Request> requests = requestService.getRequestsByClient(clientPhone);
        List<RequestResponseDTO> response = requests.stream()
                .map(this::mapToRequestResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByStatus(@RequestParam RequestStatus status) {
        List<Request> requests = requestService.getRequestsByStatus(status);
        List<RequestResponseDTO> response = requests.stream()
                .map(this::mapToRequestResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestBody StatusChangeDTO statusChangeDTO) {
        log.info("Changing status for request {} to {}", id, statusChangeDTO.getNewStatus());

        try {
            Request request = requestService.changeStatus(id, statusChangeDTO);
            return ResponseEntity.ok(mapToRequestResponseDTO(request));
        } catch (RequestNotFoundException e) {
            log.error("Request not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Invalid status transition: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error changing status", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Error changing status",
                            "details", e.getMessage()
                    ));
        }
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StatusHistoryDTO>> getStatusHistory(@PathVariable Long id) {
        List<StatusHistory> history = requestService.getStatusHistory(id);
        List<StatusHistoryDTO> response = history.stream()
                .map(this::mapToStatusHistoryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private RequestResponseDTO mapToRequestResponseDTO(Request request) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setId(request.getId());
        dto.setClientName(request.getClientName());
        dto.setClientPhone(request.getClientPhone());
        dto.setProblemDescription(request.getProblemDescription());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }

    private StatusHistoryDTO mapToStatusHistoryDTO(StatusHistory history) {
        StatusHistoryDTO dto = new StatusHistoryDTO();
        dto.setOldStatus(history.getOldStatus());
        dto.setNewStatus(history.getNewStatus());
        dto.setChangeDate(history.getChangeDate());
        dto.setChangeReason(history.getChangeReason());
        dto.setChangedBy(history.getChangedBy());
        return dto;
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<String> handleRequestNotFound(RequestNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

