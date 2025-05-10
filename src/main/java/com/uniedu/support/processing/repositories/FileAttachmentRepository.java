package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
}
