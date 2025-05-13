package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import com.uniedu.support.processing.models.enums.FileType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "file_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata extends BaseEntity {

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String storagePath;

    @Column(nullable = false)
    private FileType fileType;

    private Long fileSize;

    @ManyToOne
    private Ticket ticket;
}
