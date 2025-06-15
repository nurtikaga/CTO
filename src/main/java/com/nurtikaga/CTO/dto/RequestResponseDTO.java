package com.nurtikaga.CTO.dto;

import com.nurtikaga.CTO.model.Request;
import com.nurtikaga.CTO.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponseDTO {
    private Long id;
    private String clientName;
    private String clientPhone;
    private String problemDescription;
    private RequestStatus status; // Оставляем как enum
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RequestResponseDTO fromEntity(Request request) {
        return new RequestResponseDTO(
                request.getId(),
                request.getClientName(),
                request.getClientPhone(),
                request.getProblemDescription(),
                request.getStatus(), // Оставляем enum
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}
