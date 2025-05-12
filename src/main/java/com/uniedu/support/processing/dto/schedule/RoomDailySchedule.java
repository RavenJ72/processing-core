package com.uniedu.support.processing.dto.schedule;

import java.util.Map;

public record RoomDailySchedule(
        String roomNumber,
        Map<String, RoomScheduleCell> timeSlots // "08:30 - 09:50" -> RoomScheduleCell
) {}