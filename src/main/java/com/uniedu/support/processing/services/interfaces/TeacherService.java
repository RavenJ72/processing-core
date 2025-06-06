package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.dto.createRequests.TicketCreateRequest;
import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.models.enums.TicketStatus;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TeacherService {
    TicketDto createTicket(TicketCreateRequest ticketCreateRequest, UserDetails userDetails);
    void changeTicketStatus(Long ticketId, TicketStatus ticketStatus, UserDetails userDetails);
    List<TicketDto> getAllActiveTickets(UserDetails userDetails);
}
