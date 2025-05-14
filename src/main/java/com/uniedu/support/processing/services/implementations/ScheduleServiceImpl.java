package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.schedule.GroupedRoomScheduleResponse;
import com.uniedu.support.processing.dto.schedule.RoomDailySchedule;
import com.uniedu.support.processing.dto.schedule.RoomScheduleCell;
import com.uniedu.support.processing.dto.schedule.ScheduleUpdateDto;
import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.entities.WorkerSchedule;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.repositories.RoomRepository;
import com.uniedu.support.processing.repositories.UserRepository;
import com.uniedu.support.processing.repositories.WorkerScheduleRepository;
import com.uniedu.support.processing.services.interfaces.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final RoomRepository roomRepository;
    private final WorkerScheduleRepository workerScheduleRepository;
    private final UserRepository userRepository;

    public record TimeRange(String start, String end) {
        public boolean contains(LocalTime time) {
            return !time.isBefore(LocalTime.parse(start)) && time.isBefore(LocalTime.parse(end));
        }

        public String toLabel() {
            return start + " - " + end;
        }

        public boolean isWithinRange(LocalTime shiftStart, LocalTime shiftEnd) {
            LocalTime slotStart = LocalTime.parse(start);
            LocalTime slotEnd = LocalTime.parse(end);
            return !shiftStart.isAfter(slotStart) && !shiftEnd.isBefore(slotEnd);
        }
    }

    public static final List<TimeRange> STANDARD_TIME_SLOTS = List.of(
            new TimeRange("08:30", "09:50"),
            new TimeRange("10:05", "11:25"),
            new TimeRange("11:40", "13:00"),
            new TimeRange("13:45", "15:05"),
            new TimeRange("15:20", "16:40"),
            new TimeRange("16:55", "18:15"),
            new TimeRange("18:30", "19:50"),
            new TimeRange("20:00", "21:20")
    );

    @Override
    public List<GroupedRoomScheduleResponse> getGroupedRoomSchedulesForWeek(boolean nextWeek) {
        log.info("Fetching grouped room schedules for {} week", nextWeek ? "next" : "current");

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        if (nextWeek) {
            startOfWeek = startOfWeek.plusWeeks(1);
            log.debug("Adjusted start of week to next week: {}", startOfWeek);
        }
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        List<WorkerSchedule> schedules = workerScheduleRepository.findAllByDateBetweenWithUsersAndRooms(startOfWeek, endOfWeek);
        log.debug("Found {} schedule entries for period {} to {}", schedules.size(), startOfWeek, endOfWeek);

        List<Room> rooms = roomRepository.findAll();
        log.debug("Found {} rooms in total", rooms.size());

        List<GroupedRoomScheduleResponse> result = new ArrayList<>();

        for (RoomGroup group : RoomGroup.values()) {
            log.debug("Processing room group: {}", group);

            List<Room> groupRooms = rooms.stream()
                    .filter(r -> r.getRoomGroup() == group)
                    .toList();

            List<RoomDailySchedule> roomSchedules = new ArrayList<>();

            for (Room room : groupRooms) {
                log.debug("Processing room: {}", room.getName());
                Map<String, RoomScheduleCell> timeSlotMap = new LinkedHashMap<>();

                for (TimeRange slot : STANDARD_TIME_SLOTS) {
                    Map<DayOfWeek, String> workers = new EnumMap<>(DayOfWeek.class);

                    for (DayOfWeek day : DayOfWeek.values()) {
                        LocalDate date = startOfWeek.with(day);

                        String fullName = schedules.stream()
                                .filter(s -> s.getDate().equals(date))
                                .filter(s -> s.getWorker().getAssignedRooms().contains(room))
                                .filter(s -> slot.isWithinRange(s.getStartTime(), s.getEndTime()))
                                .map(s -> shortName(s.getWorker()))
                                .findFirst()
                                .orElse(null);

                        workers.put(day, fullName);
                    }

                    timeSlotMap.put(slot.toLabel(), new RoomScheduleCell(workers));
                }

                roomSchedules.add(new RoomDailySchedule(room.getName(), timeSlotMap));
            }

            result.add(new GroupedRoomScheduleResponse(group, roomSchedules));
        }

        log.info("Successfully compiled schedules for {} room groups", result.size());
        return result;
    }

    @Override
    @Transactional
    public void updateSchedule(ScheduleUpdateDto updateDto, String token) {
        log.info("Starting schedule update process");
        validateUpdateDto(updateDto);

        User worker = resolveWorker(updateDto.workerId());
        List<Room> roomsInGroup = roomRepository.findByRoomGroup(updateDto.roomGroup());

        if (roomsInGroup.isEmpty()) {
            throw new EntityNotFoundException("No rooms found in group: " + updateDto.roomGroup());
        }

        TimeSlot timeSlot = parseTimeSlot(updateDto.timeSlot());

        // Определяем дату - приоритет у specificDate
        LocalDate date = updateDto.specificDate() != null
                ? updateDto.specificDate()
                : resolveDateForDayOfWeek(updateDto.dayOfWeek());

        log.debug("Using date: {}", date);

        if (worker != null) {
            updateWorkerAssignedRooms(worker, roomsInGroup);
        }

        updateSchedulesForRooms(worker, roomsInGroup, date, timeSlot);
        log.info("Schedule update completed successfully");
    }

    private void validateUpdateDto(ScheduleUpdateDto updateDto) {
        if (updateDto.roomGroup() == null) {
            throw new IllegalArgumentException("Room group must not be null");
        }
        if (updateDto.timeSlot() == null || updateDto.timeSlot().isBlank()) {
            throw new IllegalArgumentException("Time slot must not be empty");
        }
        // Проверяем что указан хотя бы один из вариантов
        if ((updateDto.dayOfWeek() == null || updateDto.dayOfWeek().isBlank())
                && updateDto.specificDate() == null) {
            throw new IllegalArgumentException("Either dayOfWeek or specificDate must be provided");
        }
    }

    private User resolveWorker(Long workerId) {
        if (workerId == null) {
            return null;
        }
        return userRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found with id: " + workerId));
    }

    private TimeSlot parseTimeSlot(String timeSlotStr) {
        try {
            String[] times = timeSlotStr.split(" - ");
            return new TimeSlot(
                    LocalTime.parse(times[0].trim()), // "15:20" -> парсится как 15:20
                    LocalTime.parse(times[1].trim())  // "16:40" -> парсится как 16:40
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time slot format: " + timeSlotStr +
                    ". Expected format: 'HH:mm - HH:mm'", e);
        }
    }

    private LocalDate resolveDateForDayOfWeek(String dayOfWeek) {
        try {
            DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            return LocalDate.now().with(TemporalAdjusters.nextOrSame(targetDay));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek, e);
        }
    }

    private void updateWorkerAssignedRooms(User worker, List<Room> roomsToAdd) {
        Set<Room> currentRooms = new HashSet<>(worker.getAssignedRooms());
        currentRooms.addAll(roomsToAdd);
        worker.setAssignedRooms(currentRooms);
        userRepository.save(worker);
        log.info("Updated assigned rooms for worker {}: {}", worker.getId(),
                currentRooms.stream().map(Room::getName).collect(Collectors.joining(", ")));
    }

    private void updateSchedulesForRooms(User worker, List<Room> rooms, LocalDate date, TimeSlot timeSlot) {
        rooms.forEach(room -> {
            // Удаляем все существующие расписания для этого кабинета, даты и временного слота
            workerScheduleRepository.deleteByRoomAndDateAndStartTime(room.getId(), date, timeSlot.start());

            // Если работник указан - создаем новое расписание
            if (worker != null) {
                WorkerSchedule newSchedule = WorkerSchedule.builder()
                        .worker(worker)
                        .date(date)
                        .startTime(timeSlot.start())
                        .endTime(timeSlot.end())
                        .build();
                workerScheduleRepository.save(newSchedule);
                log.info("Created new schedule for worker {} in room {}: {} {} - {}",
                        worker.getId(), room.getName(), date, timeSlot.start(), timeSlot.end());
            }
        });
    }

    // Вспомогательный класс для хранения временного слота
    private record TimeSlot(LocalTime start, LocalTime end) {}

    private LocalDate getNextDateForDayOfWeek(String dayOfWeek) {
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek);
        LocalDate date = LocalDate.now();
        while (date.getDayOfWeek() != targetDay) {
            date = date.plusDays(1);
        }
        return date;
    }

    private String shortName(User user) {
        return user.getLastName() + " " + user.getFirstName().charAt(0) + ".";
    }
}

