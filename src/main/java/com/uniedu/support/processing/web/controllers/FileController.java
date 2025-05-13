package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.standart.FileDto;
import com.uniedu.support.processing.models.entities.FileMetadata;
import com.uniedu.support.processing.models.enums.FileType;
import com.uniedu.support.processing.services.implementations.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // Загрузка файла и привязка к тикету
    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ticketId") Long ticketId,
            @RequestParam("fileType") FileType fileType) {
        FileMetadata uploadedFile = fileService.uploadFile(file, ticketId, fileType);
        return ResponseEntity.ok(uploadedFile);
    }

    // Получение файла по ID
    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    // Скачивание файла
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] fileData = fileService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileService.getFileNameById(id) + "\"")
                .body(fileData);
    }

    // Удаление файла
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    // Получение всех файлов тикета
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<FileDto>> getFilesByTicket(@PathVariable Long ticketId) {
        List<FileDto> files = fileService.getFilesByTicketId(ticketId);
        return ResponseEntity.ok(files);
    }
}