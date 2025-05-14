package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.dto.standart.TicketUpdateDto;

import java.util.List;

public interface TicketService<ID> {
    TicketDto getTicketById(ID id);
    List<TicketDto> getAllActiveTicketsAssignedOnWorkerByWorkerId(Long id);
    TicketDto updateTicket(TicketUpdateDto ticketDto);
    List<RoomDto> getAvailableRooms();
}
