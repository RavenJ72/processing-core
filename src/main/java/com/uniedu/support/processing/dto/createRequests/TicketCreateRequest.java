package com.uniedu.support.processing.dto.createRequests;

import com.uniedu.support.processing.models.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketCreateRequest {
    private String title;
    private String description;
    private String room;
}
