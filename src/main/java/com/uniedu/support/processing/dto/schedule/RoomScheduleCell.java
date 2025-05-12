package com.uniedu.support.processing.dto.schedule;

import java.time.DayOfWeek;
import java.util.Map;

public record RoomScheduleCell(
        Map<DayOfWeek, String> workersByDay // "MONDAY" -> "Иванов И.И."
) {}