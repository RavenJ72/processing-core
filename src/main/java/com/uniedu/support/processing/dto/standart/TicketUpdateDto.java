package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.models.enums.TicketStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketUpdateDto extends BaseDTO {

    private String title;

    private String description;

    private TicketStatus status;

    private RoomDto room;


}
