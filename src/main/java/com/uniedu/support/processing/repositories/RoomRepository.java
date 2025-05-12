package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.enums.RoomGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);

    List<Room> findByRoomGroup(RoomGroup roomGroup);
}
