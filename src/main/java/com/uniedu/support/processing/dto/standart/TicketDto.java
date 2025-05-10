package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.TimestampedDTO;
import com.uniedu.support.processing.models.enums.TicketStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketDto extends TimestampedDTO {

    private String title;

    private String description;

    private TicketStatus status;

    private RoomDto room;

    private ChatDto chat;

    private UserDto creator;

    private UserDto assignedTo;
}
