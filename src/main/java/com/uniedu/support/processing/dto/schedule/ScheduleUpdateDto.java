package com.uniedu.support.processing.dto.schedule;

import com.uniedu.support.processing.models.enums.RoomGroup;

public record ScheduleUpdateDto(
        RoomGroup roomGroup,
        String timeSlot,
        String dayOfWeek,
        Long workerId
) {}