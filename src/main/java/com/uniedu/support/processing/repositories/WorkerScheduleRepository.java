package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.entities.WorkerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface WorkerScheduleRepository extends JpaRepository<WorkerSchedule, Long> {

    @Query("SELECT ws.worker FROM WorkerSchedule ws " +
            "WHERE ws.dayOfWeek = :today " +
            "AND :now BETWEEN ws.startTime AND ws.endTime")
    List<User> findScheduledWorkers(@Param("today") DayOfWeek today, @Param("now") LocalTime now);

}

