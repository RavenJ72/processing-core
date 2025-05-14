package com.uniedu.support.processing.dto.schedule;

import com.uniedu.support.processing.models.enums.RoomGroup;

import java.time.LocalDate;

public record ScheduleUpdateDto(
        RoomGroup roomGroup,
        String timeSlot,
        String dayOfWeek,
        LocalDate specificDate,
        Long workerId
) {}