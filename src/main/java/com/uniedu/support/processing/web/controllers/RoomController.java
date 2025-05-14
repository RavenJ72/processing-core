package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.standart.RoomDto;
import com.uniedu.support.processing.models.enums.RoomGroup;
import com.uniedu.support.processing.services.interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Комнаты", description = "Управление кабинетами/аудиториями")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Создать новую комнату")
    @ApiResponse(responseCode = "200", description = "Комната успешно создана")
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.createRoom(roomDto));
    }

    @Operation(summary = "Обновить существующую комнату")
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoom(
            @Parameter(description = "ID комнаты") @PathVariable Long id,
            @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomDto));
    }

    @Operation(summary = "Удалить комнату")
    @ApiResponse(responseCode = "204", description = "Комната успешно удалена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @Parameter(description = "ID комнаты") @PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список комнат по группе")
    @GetMapping("/group/{group}")
    public ResponseEntity<List<RoomDto>> getRoomsByGroup(
            @Parameter(description = "Группа комнат") @PathVariable RoomGroup group) {
        return ResponseEntity.ok(roomService.getRoomsByGroup(group));
    }

    @Operation(summary = "Получить список всех комнат")
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @Operation(
            summary = "Обновить список комнат, закреплённых за пользователем",
            description = "Обновляет перечень комнат, закреплённых за указанным пользователем"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Список комнат успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/user/{userId}/rooms")
    public ResponseEntity<Void> updateUserRooms(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,

            @Parameter(
                    description = "Набор ID комнат для привязки к пользователю",
                    example = "[1, 2, 3]"
            )
            @RequestBody Set<Long> roomIds) {
        roomService.updateUserRooms(userId, roomIds);
        return ResponseEntity.noContent().build();
    }
}
