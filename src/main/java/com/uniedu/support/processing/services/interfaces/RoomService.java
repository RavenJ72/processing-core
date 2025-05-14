package com.uniedu.support.processing.services.interfaces;

import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.models.enums.RoomGroup;

import java.util.List;
import java.util.Set;

public interface RoomService {
    RoomDto createRoom(RoomDto roomDto);
    RoomDto updateRoom(Long id, RoomDto roomDto);
    void deleteRoom(Long id);
    List<RoomDto> getRoomsByGroup(RoomGroup roomGroup);
    List<RoomDto> getAllRooms();
    void updateUserRooms(Long userId, Set<Long> roomIds);
}