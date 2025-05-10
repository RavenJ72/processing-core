package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatusAndAssignedToId(TicketStatus status, Long assignedToId);


}
