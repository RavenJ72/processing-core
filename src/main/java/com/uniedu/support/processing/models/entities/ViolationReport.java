package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
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

    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
