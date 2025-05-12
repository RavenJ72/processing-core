package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.dto.standart.CreateTicketDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.dto.standart.TicketUpdateDto;
import com.uniedu.support.processing.models.enums.TicketStatus;

import java.util.List;

public interface TicketService<ID> {
    TicketDto getTicketById(ID id);
    List<TicketDto> getAllActiveTicketsAssignedOnWorkerByWorkerId(Long id);
    TicketDto updateTicket(TicketUpdateDto ticketDto);
}
