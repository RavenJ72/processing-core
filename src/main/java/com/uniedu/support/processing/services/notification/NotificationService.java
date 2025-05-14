package com.uniedu.support.processing.services.notification;

import com.uniedu.support.processing.dto.notifications.Notification;
import com.uniedu.support.processing.dto.notifications.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RabbitTemplate rabbitTemplate;

    public void sendNewMessageNotification(Long chatId, Long messageId, Long recipientId) {
        Notification notification = new Notification(
                NotificationType.NEW_MESSAGE,
                recipientId.toString(),
                "У вас новое сообщение в чате",
                Map.of(
                        "chatId", chatId,
                        "messageId", messageId
                )
        );
        sendNotification(notification);
    }

    public void sendTicketAssignedNotification(Long ticketId, Long assigneeId) {
        Notification notification = new Notification(
                NotificationType.TICKET_ASSIGNED,
                assigneeId.toString(),
                "Вам назначен новый тикет",
                Map.of(
                        "ticketId", ticketId
                )
        );
        sendNotification(notification);
    }

    public void sendViolationRecordedNotification(Long violationId, Long userId) {
        Notification notification = new Notification(
                NotificationType.VIOLATION_RECORDED,
                userId.toString(),
                "Зафиксировано нарушение",
                Map.of(
                        "violationId", violationId
                )
        );
        sendNotification(notification);
    }

    private void sendNotification(Notification notification) {
        rabbitTemplate.convertAndSend(
                "notifications.exchange",
                "notification.routing.key",
                notification
        );
    }
}
