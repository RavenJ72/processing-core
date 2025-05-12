package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ChatMessageDto extends BaseDTO {
    private String content;

    private LocalDateTime sentAt;

    private Boolean isRead;

    private Long senderId;

    private Long receiverId;

    private Long chatId;
}
