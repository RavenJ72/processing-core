package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "worker_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSchedule extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // java.time.DayOfWeek (MONDAY, TUESDAY, ...)

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}