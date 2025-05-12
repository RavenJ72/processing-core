package com.uniedu.support.processing.dto.createRequests;

import lombok.Data;

@Data
public class TicketCreateRequest {
    private String title;
    private String description;
    private String roomName;
}
