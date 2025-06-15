package com.nurtikaga.CTO.dto;

import com.nurtikaga.CTO.model.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusChangeDTO {
    @NotNull
    private RequestStatus newStatus;

    @NotBlank
    private String changeReason;

    @NotBlank
    private String changedBy;
}

