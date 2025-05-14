package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import com.uniedu.support.processing.models.enums.RoomGroup;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {

    private String name; // например, "101", "Аудитория 202"

    @Enumerated(EnumType.STRING)
    private RoomGroup roomGroup;
}