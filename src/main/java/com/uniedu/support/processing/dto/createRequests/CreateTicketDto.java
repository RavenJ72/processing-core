package com.uniedu.support.processing.dto.createRequests;

import lombok.Data;

@Data
public class CreateTicketDto {
    private String title;

    private String description;

    private String roomName;

    private Long creatorId;

}
