package com.uniedu.support.processing.dto.authEntities;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    public MessageResponse(String message) {
        this.message = message;
    }
}