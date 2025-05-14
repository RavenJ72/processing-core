package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.authEntities.MessageResponse;
import com.uniedu.support.processing.dto.standart.TicketDto;
import com.uniedu.support.processing.models.enums.WorkerStatus;
import com.uniedu.support.processing.services.implementations.WorkerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/worker")
@Tag(name = "Сотрудник", description = "Методы, доступные пользователю с ролью WORKER")
public class WorkerController {

    private final WorkerServiceImpl workerServiceImpl;

    @PreAuthorize("hasRole('WORKER')")
    @Operation(
            summary = "Завершить выполнение тикета",
            description = "Помечает тикет как выполненный текущим сотрудником"
    )
    @ApiResponse(responseCode = "200", description = "Тикет успешно завершён")
    @GetMapping("/completeTicket/{id}")
    public ResponseEntity<?> completeTicket(
            @Parameter(description = "ID тикета для завершения") @PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        workerServiceImpl.completeTicket(id, userDetails);
        return ResponseEntity.ok(new MessageResponse("User complete ticket successfully!"));
    }

    @PreAuthorize("hasRole('WORKER')")
    @Operation(
            summary = "Изменить статус сотрудника",
            description = "Изменяет текущий статус (например, ДОСТУПЕН, ЗАНЯТ) сотрудника"
    )
    @ApiResponse(responseCode = "200", description = "Статус успешно обновлён")
    @GetMapping("/changeStatus/{status}")
    public ResponseEntity<?> changeWorkerStatus(
            @Parameter(description = "Новый статус сотрудника") @PathVariable WorkerStatus status) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        workerServiceImpl.changeWorkerStatus(status, userDetails);
        return ResponseEntity.ok(new MessageResponse("User changed own status successfully!"));
    }

    @PreAuthorize("hasRole('WORKER')")
    @Operation(
            summary = "Получить назначенные тикеты",
            description = "Возвращает список тикетов, назначенных текущему сотруднику"
    )
    @ApiResponse(responseCode = "200", description = "Список тикетов успешно возвращён")
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> getAssignedTickets() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(workerServiceImpl.getAssignedTickets(userDetails));
    }
}
