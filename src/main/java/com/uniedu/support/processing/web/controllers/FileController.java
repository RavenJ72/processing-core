package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.standart.FileDto;
import com.uniedu.support.processing.models.entities.FileMetadata;
import com.uniedu.support.processing.models.enums.FileType;
import com.uniedu.support.processing.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Файлы", description = "Управление файлами и вложениями в системе")
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "Загрузить файл и привязать к тикету",
            description = "Загружает файл и связывает его с указанным тикетом по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileDto> uploadFile(
            @Parameter(description = "File to upload",
                    schema = @Schema(type = "string", format = "binary"))
            @RequestPart("file") MultipartFile file,
            @RequestParam Long ticketId,
            @RequestParam FileType fileType) {
        FileDto uploadedFile = fileService.uploadFile(file, ticketId, fileType);
        return ResponseEntity.ok(uploadedFile);
    }

    @Operation(
            summary = "Получить метаинформацию о файле",
            description = "Возвращает метаданные файла по его ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(
            @Parameter(description = "ID файла") @PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @Operation(
            summary = "Скачать файл",
            description = "Скачивает файл по его ID"
    )
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(
            @Parameter(description = "ID файла") @PathVariable Long id) {

        byte[] fileData = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileService.getFileNameById(id) + "\"")
                .body(fileData);
    }

    @Operation(
            summary = "Удалить файл",
            description = "Удаляет файл по его ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "ID файла") @PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить все файлы по тикету",
            description = "Возвращает список всех файлов, прикреплённых к заданному тикету"
    )
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<FileDto>> getFilesByTicket(
            @Parameter(description = "ID тикета") @PathVariable Long ticketId) {

        List<FileDto> files = fileService.getFilesByTicketId(ticketId);
        return ResponseEntity.ok(files);
    }
}
