package com.uniedu.support.processing.models.baseEntities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter

@MappedSuperclass
public abstract class TimestampedEntity extends BaseEntity {
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modified;
}
