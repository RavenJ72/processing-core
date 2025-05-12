package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.dto.base.TimestampedDTO;
import com.uniedu.support.processing.models.enums.TicketStatus;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto extends BaseDTO {

    private String title;

    private String description;

    private TicketStatus status;

    private RoomDto room;

    private ChatDto chat;

    private UserDto creator;

    private UserDto assignedTo;
}
