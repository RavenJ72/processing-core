package com.uniedu.support.processing.services.interfaces;


import com.uniedu.support.processing.dto.schedule.GroupedRoomScheduleResponse;
import com.uniedu.support.processing.dto.schedule.ScheduleUpdateDto;

import java.util.List;

public interface ScheduleService {
    List<GroupedRoomScheduleResponse> getGroupedRoomSchedulesForWeek(boolean nextWeek);
    void updateSchedule(ScheduleUpdateDto updateDto, String token);
}