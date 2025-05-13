package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.standart.FileDto;
import com.uniedu.support.processing.models.entities.FileMetadata;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.enums.FileType;
import com.uniedu.support.processing.repositories.FileMetadataRepository;
import com.uniedu.support.processing.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMetadataRepository fileMetadataRepository;
    private final TicketRepository ticketRepository;
    private final MinioService minioService; // Сервис для работы с MinIO
    private final ModelMapper modelMapper;

    // Загрузка файла
    @Transactional
    public FileMetadata uploadFile(MultipartFile file, Long ticketId, FileType fileType) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // 1. Загружаем файл в MinIO
        String storagePath = minioService.uploadFile(file);

        // 2. Сохраняем метаданные в БД
        FileMetadata fileMetadata = FileMetadata.builder()
                .originalFileName(file.getOriginalFilename())
                .storagePath(storagePath)
                .fileType(fileType)
                .fileSize(file.getSize())
                .ticket(ticket)
                .build();

        return fileMetadataRepository.save(fileMetadata);
    }

    // Скачивание файла
    public byte[] downloadFile(Long id) {
        FileMetadata file = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return minioService.downloadFile(file.getStoragePath());
    }

    public String getFileNameById(Long fileId){
        FileMetadata file = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return file.getOriginalFileName();
    }

    // Удаление файла
    @Transactional
    public void deleteFile(Long id) {
        FileMetadata file = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // 1. Удаляем из MinIO
        minioService.deleteFile(file.getStoragePath());

        // 2. Удаляем запись из БД
        fileMetadataRepository.delete(file);
    }

    // Получение файла по ID
    public FileDto getFileById(Long id) {
        val file = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        return modelMapper.map(file, FileDto.class);
    }

    // Получение всех файлов тикета
    public List<FileDto> getFilesByTicketId(Long ticketId) {
        return fileMetadataRepository.findByTicketId(ticketId).stream().map(file -> modelMapper.map(file, FileDto.class)).toList();
    }
}