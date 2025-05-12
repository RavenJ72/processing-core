package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatusAndAssignedToId(TicketStatus status, Long assignedToId);
    List<Ticket> findByStatusAndCreatorId(TicketStatus status, Long creatorId);
    Optional<Ticket> findByChatId(Long chatId);


}
