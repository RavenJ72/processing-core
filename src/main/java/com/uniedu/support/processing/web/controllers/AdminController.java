package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.schedule.GroupedRoomScheduleResponse;
import com.uniedu.support.processing.dto.schedule.ScheduleUpdateDto;
import com.uniedu.support.processing.dto.standart.UserDto;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.services.implementations.ScheduleServiceImpl;
import com.uniedu.support.processing.services.implementations.UserServiceImpl;
import com.uniedu.support.processing.services.interfaces.UserService;
import com.uniedu.support.processing.dto.authEntities.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService<Long> userService;
    private final ScheduleServiceImpl scheduleServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(signUpRequest, userDetails));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/grouped-room-schedules")
    public ResponseEntity<List<GroupedRoomScheduleResponse>> getGroupedRoomSchedules(
            @RequestParam(value = "nextWeek", defaultValue = "false") boolean nextWeek){

        List<GroupedRoomScheduleResponse> schedules = scheduleServiceImpl.getGroupedRoomSchedulesForWeek(nextWeek);
        return ResponseEntity.ok(schedules);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-schedule")
    public ResponseEntity<Void> updateSchedule(
            @RequestBody ScheduleUpdateDto updateDto,
            @RequestHeader("Authorization") String token) {
        scheduleServiceImpl.updateSchedule(updateDto, token);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/workers")
    public ResponseEntity<List<UserDto>> getWorkers(){
        return ResponseEntity.ok(userServiceImpl.findAllByRoleAsc(UserRoleType.WORKER));
    }
}
