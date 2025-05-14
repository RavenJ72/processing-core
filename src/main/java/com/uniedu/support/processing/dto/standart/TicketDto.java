package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.dto.base.TimestampedDTO;
import com.uniedu.support.processing.models.enums.TicketStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private RoomDto room;

    private ChatDto chat;

    private UserDto creator;

    private UserDto assignedTo;
}
