package com.uniedu.support.processing.dto.standart;

import com.uniedu.support.processing.dto.base.BaseDTO;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public class ChatMessageDto extends BaseDTO {
    private String content;

    private LocalDateTime sentAt;

    private Boolean isRead;

    private UserDto sender;

    private UserDto receiver;

    private ChatDto chat;
}
