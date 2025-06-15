package com.nurtikaga.CTO.dto;

import com.nurtikaga.CTO.model.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusHistoryDTO {
    private RequestStatus oldStatus;
    private RequestStatus newStatus;
    private LocalDateTime changeDate;
    private String changeReason;
    private String changedBy;
}
