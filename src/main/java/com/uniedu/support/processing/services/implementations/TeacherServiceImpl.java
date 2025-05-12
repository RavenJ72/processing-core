package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.createRequests.TicketCreateRequest;
import com.uniedu.support.processing.dto.standart.ChatDto;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.repositories.ChatRepository;
import com.uniedu.support.processing.repositories.RoomRepository;
import com.uniedu.support.processing.repositories.TicketRepository;
import com.uniedu.support.processing.repositories.UserRepository;
import com.uniedu.support.processing.services.interfaces.TeacherService;
import com.uniedu.support.processing.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    private final UserService<Long> userService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Transactional
    @Override
    public TicketDto createTicket(TicketCreateRequest ticketCreateRequest, UserDetails userDetails) {
        log.info("Teacher create ticket request. Initiator - {}", userDetails.getUsername());

        Ticket ticket = Ticket.builder()
                .title(ticketCreateRequest.getTitle())
                .description(ticketCreateRequest.getDescription())
                .status(TicketStatus.IN_PROGRESS)
                .room(roomRepository.findByName(ticketCreateRequest.getRoomName()))
                .creator(userRepository.findByUsername(userDetails.getUsername()).orElseThrow())
                .assignedTo(userService.getUserForTicketAssigmentByRoomName(ticketCreateRequest.getRoomName()))
                .chat(chatRepository.save(new Chat()))
                .build();

        val savedTicket = ticketRepository.save(ticket);

        return modelMapper.map(savedTicket, TicketDto.class);
    }

    @Override
    public void changeTicketStatus(Long ticketId, TicketStatus ticketStatus, UserDetails userDetails) {
        log.info("Teacher change ticket status request. Initiator - {}", userDetails.getUsername());
        val optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            if(ticket.getCreator().getUsername().equals(userDetails.getUsername())){
                ticket.setStatus(ticketStatus);
                ticketRepository.save(ticket);
            }else{
                throw new IllegalArgumentException(String.format("Teacher - %s dont have permission to make action with ticket %d", userDetails.getUsername(), ticketId));
            }
        }
        throw new IllegalArgumentException("Ticket not found");
    }

    @Override
    public List<TicketDto> getAllActiveTickets(UserDetails userDetails) {
        return ticketRepository.findByStatusAndCreatorId(TicketStatus.IN_PROGRESS, userRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId()).stream().map(e->modelMapper.map(e,TicketDto.class)).toList();
    }
}
