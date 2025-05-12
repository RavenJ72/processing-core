package com.uniedu.support.processing.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private NotificationType type;
    private String recipientId;
    private String message;
    private Map<String, Object> payload;
}