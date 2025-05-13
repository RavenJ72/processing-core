package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.FileMetadata;
import com.uniedu.support.processing.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByTicketId(Long ticketId);
}
