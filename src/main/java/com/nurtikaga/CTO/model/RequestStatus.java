package com.nurtikaga.CTO.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {
    CREATED("CREATED"),
    IN_PROGRESS("IN_PROGRESS"),
    REPAIR("REPAIR"),
    COMPLETED("COMPLETED");

    private final String code;

    RequestStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static RequestStatus fromCode(String code) {
        for (RequestStatus status : values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
