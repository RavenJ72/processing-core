package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.repositories.TicketRepository;
import com.uniedu.support.processing.repositories.UserRepository;
import com.uniedu.support.processing.services.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @CacheEvict(value = "workerTickets", key = "#userDetails.username")
    public void completeTicket(Long id, UserDetails userDetails) {
        log.info("Worker close ticket request. Initiator - {}", userDetails.getUsername());
        val optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()){
            Ticket ticket = optionalTicket.get();
            if(ticket.getAssignedTo().getUsername().equals(userDetails.getUsername())){
                ticket.setStatus(TicketStatus.COMPLETED);
                ticketRepository.save(ticket);
            }else{
                throw new IllegalArgumentException(String.format("Worker - %s dont have permission to make action with ticket %d", userDetails.getUsername(), id));
            }
        }else{
            throw new IllegalArgumentException("Ticket not found");
        }
    }

    @Override
    public void changeWorkerStatus(WorkerStatus status, UserDetails userDetails) {
        log.info("Worker change own status. Initiator - {}", userDetails.getUsername());
        val user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        user.setIsActive(status);
        userRepository.save(user);
        log.info("Worker change own status successfully");
    }

    @Override
    @Cacheable(value = "workerTickets", key = "#userDetails.username")
    public List<TicketDto> getAssignedTickets(UserDetails userDetails) {
        log.info("Worker get all assigned tickets. Initiator - {}", userDetails.getUsername());
        val user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ticketRepository.findByStatusAndAssignedToId(TicketStatus.IN_PROGRESS, user.getId()).stream().map(e-> modelMapper.map(e, TicketDto.class)).collect(Collectors.toList());
    }
}
