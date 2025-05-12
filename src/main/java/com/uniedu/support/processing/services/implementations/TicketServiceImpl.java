package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.standart.CreateTicketDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.dto.standart.TicketUpdateDto;
import com.uniedu.support.processing.exceptions.RoomNotFoundException;
import com.uniedu.support.processing.exceptions.TicketNotFoundException;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.repositories.ChatRepository;
import com.uniedu.support.processing.repositories.RoomRepository;
import com.uniedu.support.processing.repositories.TicketRepository;
import com.uniedu.support.processing.services.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService<Long> {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;


    @Override
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ticket not found with id: " + id
                ));
        return modelMapper.map(ticket, TicketDto.class);
    }

    @Override
    public List<TicketDto> getAllActiveTicketsAssignedOnWorkerByWorkerId(Long id) {
        return ticketRepository.findByStatusAndAssignedToId(TicketStatus.IN_PROGRESS, id).stream().map(e-> modelMapper.map(e, TicketDto.class)).toList();
    }

    @Override
    @Transactional
    public TicketDto updateTicket(TicketUpdateDto ticketDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId())
                .orElseThrow(() -> new TicketNotFoundException(ticketDto.getId()));


        log.info("Updating ticket ID {}: {}", ticket.getId(), ticketDto);

        if (ticketDto.getTitle() != null) {
            ticket.setTitle(ticketDto.getTitle());
        }

        if (ticketDto.getDescription() != null) {
            ticket.setDescription(ticketDto.getDescription());
        }

        if (ticketDto.getStatus() != null) {
            ticket.setStatus(ticketDto.getStatus());
        }

        if (ticketDto.getRoom() != null) {
            Room room = roomRepository.findById(ticketDto.getRoom().getId())
                    .orElseThrow(() -> new RoomNotFoundException(ticketDto.getRoom().getId()));
            ticket.setRoom(room);
        }

        return modelMapper.map(ticketRepository.save(ticket), TicketDto.class);
    }

}
