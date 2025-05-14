package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.repositories.RoomRepository;
import com.uniedu.support.processing.repositories.UserRepository;
import com.uniedu.support.processing.services.interfaces.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(value = "availableRooms")
    public RoomDto createRoom(RoomDto roomDto) {
        log.info("Creating new room: {}", roomDto.getName());

        if (roomRepository.existsByName(roomDto.getName())) {
            log.warn("Room with name {} already exists", roomDto.getName());
            throw new IllegalArgumentException("Room with this name already exists");
        }

        Room room = Room.builder()
                .name(roomDto.getName())
                .roomGroup(roomDto.getRoomGroup())
                .build();

        Room saved = roomRepository.save(room);
        log.debug("Room created with ID: {}", saved.getId());

        return mapToDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "availableRooms")
    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        log.info("Updating room ID: {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Room not found with ID: {}", id);
                    return new EntityNotFoundException("Room not found");
                });

        if (!room.getName().equals(roomDto.getName()) &&
                roomRepository.existsByName(roomDto.getName())) {
            log.warn("Room with name {} already exists", roomDto.getName());
            throw new IllegalArgumentException("Room with this name already exists");
        }

        room.setName(roomDto.getName());
        room.setRoomGroup(roomDto.getRoomGroup());

        Room updated = roomRepository.save(room);
        log.debug("Room updated: {}", updated);

        return mapToDto(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "availableRooms")
    public void deleteRoom(Long id) {
        log.info("Deleting room ID: {}", id);

        if (!roomRepository.existsById(id)) {
            log.error("Room not found with ID: {}", id);
            throw new EntityNotFoundException("Room not found");
        }

        // Проверка, что кабинет не назначен пользователям
        List<User> usersWithRoom = userRepository.findByAssignedRooms_Id(id);
        if (!usersWithRoom.isEmpty()) {
            log.warn("Cannot delete room ID: {} - it's assigned to {} users", id, usersWithRoom.size());
            throw new IllegalStateException("Cannot delete room assigned to users");
        }

        roomRepository.deleteById(id);
        log.debug("Room deleted successfully");
    }

    @Override
    public List<RoomDto> getRoomsByGroup(RoomGroup roomGroup) {
        log.debug("Fetching rooms for group: {}", roomGroup);
        return roomRepository.findByRoomGroup(roomGroup).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Cacheable(value = "availableRooms", key = "'allRooms'")
    public List<RoomDto> getAllRooms() {
        log.debug("Fetching all rooms");
        return roomRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserRooms(Long userId, Set<Long> roomIds) {
        log.info("Updating rooms for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new EntityNotFoundException("User not found");
                });

        Set<Room> newRooms = new HashSet<>(roomRepository.findAllById(roomIds));

        // Проверка, что все запрошенные комнаты существуют
        if (newRooms.size() != roomIds.size()) {
            Set<Long> foundIds = newRooms.stream().map(Room::getId).collect(Collectors.toSet());
            Set<Long> missingIds = roomIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            log.warn("Some rooms not found: {}", missingIds);
            throw new EntityNotFoundException("Some rooms not found: " + missingIds);
        }

        user.setAssignedRooms(newRooms);
        userRepository.save(user);
        log.debug("User {} now has {} assigned rooms", userId, newRooms.size());
    }

    private RoomDto mapToDto(Room room) {
        val roomDto = RoomDto.builder()
                .name(room.getName())
                .roomGroup(room.getRoomGroup())
                .build();
        roomDto.setId(room.getId());
        return roomDto;
    }
}