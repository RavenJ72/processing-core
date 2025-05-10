package com.uniedu.support.processing.models.entities;

import com.uniedu.support.processing.models.baseEntities.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat extends BaseEntity {
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();
}