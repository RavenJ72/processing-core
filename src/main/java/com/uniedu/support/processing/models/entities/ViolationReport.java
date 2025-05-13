package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationReport extends TimestampedEntity {

    private String description;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}