package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.authEntities.SignUpRequest;
import com.uniedu.support.processing.dto.schedule.GroupedRoomScheduleResponse;
import com.uniedu.support.processing.dto.schedule.ScheduleUpdateDto;
import com.uniedu.support.processing.dto.standart.UserDto;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.services.implementations.ScheduleServiceImpl;
import com.uniedu.support.processing.services.implementations.UserServiceImpl;
import com.uniedu.support.processing.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
@Tag(name = "Администрирование", description = "Операции для администраторов системы")
public class AdminController {

    private final UserService<Long> userService;
    private final ScheduleServiceImpl scheduleServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Operation(
            summary = "Создать нового пользователя",
            description = "Регистрация пользователя с определённой ролью. Требуется авторизация администратора."
    )
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @PostMapping("/create")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(signUpRequest, userDetails));
    }

    @Operation(
            summary = "Получить расписание по кабинетам",
            description = "Возвращает сгруппированное по аудиториям расписание. По умолчанию — на текущую неделю. Можно указать следующую."
    )
    @GetMapping("/grouped-room-schedules")
    public ResponseEntity<List<GroupedRoomScheduleResponse>> getGroupedRoomSchedules(
            @Parameter(description = "Получить расписание на следующую неделю (true/false)")
            @RequestParam(value = "nextWeek", defaultValue = "false") boolean nextWeek) {

        List<GroupedRoomScheduleResponse> schedules = scheduleServiceImpl.getGroupedRoomSchedulesForWeek(nextWeek);
        return ResponseEntity.ok(schedules);
    }

    @Operation(
            summary = "Обновить расписание",
            description = "Обновление расписания смен сотрудника. Требуется JWT-токен в заголовке Authorization."
    )
    @PutMapping("/update-schedule")
    public ResponseEntity<Void> updateSchedule(
            @RequestBody ScheduleUpdateDto updateDto,
            @Parameter(description = "JWT токен администратора")
            @RequestHeader("Authorization") String token) {
        scheduleServiceImpl.updateSchedule(updateDto, token);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получить всех сотрудников",
            description = "Возвращает список пользователей с ролью 'WORKER'."
    )
    @GetMapping("/workers")
    public ResponseEntity<List<UserDto>> getWorkers() {
        return ResponseEntity.ok(userServiceImpl.findAllByRoleAsc(UserRoleType.WORKER));
    }
}
