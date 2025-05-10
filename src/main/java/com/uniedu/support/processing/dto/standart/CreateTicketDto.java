package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.models.enums.TicketStatus;
import lombok.Data;

@Data
public class CreateTicketDto {
    private String title;

    private String description;

    private String roomName;

    private Long creatorId;

}
