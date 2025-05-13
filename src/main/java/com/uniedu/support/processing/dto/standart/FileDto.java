package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.models.enums.FileType;
import lombok.Data;

@Data
public class FileDto {
    private Long id;
    private String originalFileName;
    private FileType fileType;
    private Long fileSize;
    private Long ticketId;
}