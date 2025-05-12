package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Room;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.entities.WorkerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerScheduleRepository extends JpaRepository<WorkerSchedule, Long> {

    @Query("SELECT ws.worker FROM WorkerSchedule ws " +
            "WHERE ws.date = :today " +
            "AND :now BETWEEN ws.startTime AND ws.endTime")
    List<User> findScheduledWorkers(@Param("today") LocalDate today, @Param("now") LocalTime now);


    @Query("SELECT ws FROM WorkerSchedule ws " +
            "JOIN FETCH ws.worker w " +
            "JOIN FETCH w.assignedRooms " +
            "WHERE ws.date BETWEEN :start AND :end")
    List<WorkerSchedule> findAllByDateBetweenWithUsersAndRooms(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Modifying
    @Query(value = "DELETE FROM worker_schedule ws " +
            "WHERE EXISTS (SELECT 1 FROM worker_rooms wr " +
            "WHERE wr.user_id = ws.worker_id AND wr.room_id = :roomId) " +
            "AND ws.shift_date = :date AND ws.start_time = :startTime",
            nativeQuery = true)
    void deleteByRoomAndDateAndStartTime(@Param("roomId") Long roomId,
                                         @Param("date") LocalDate date,
                                         @Param("startTime") LocalTime startTime);

    Optional<WorkerSchedule> findByWorkerAndDateAndStartTime(User worker, LocalDate date, LocalTime startTime);
}

