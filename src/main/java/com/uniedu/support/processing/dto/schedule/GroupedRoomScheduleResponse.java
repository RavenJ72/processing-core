package com.uniedu.support.processing.dto.schedule;

import com.uniedu.support.processing.models.enums.RoomGroup;

import java.util.List;

public record GroupedRoomScheduleResponse(
        RoomGroup roomGroup,
        List<RoomDailySchedule> schedules // по кабинетам
) {}
