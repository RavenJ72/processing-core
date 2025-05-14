package com.uniedu.support.processing.dto;  // Тот же пакет

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private final String message;
    private final int status;
    private List<String> details;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, int status, List<String> details) {
        this.message = message;
        this.status = status;
        this.details = details;
    }
}