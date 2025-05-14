package com.uniedu.support.processing.services.notification;

import com.uniedu.support.processing.config.RabbitMQConfig;
import com.uniedu.support.processing.dto.notifications.Notification;
import com.uniedu.support.processing.dto.notifications.NotificationType;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.FileMetadata;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.entities.ViolationReport;
import com.uniedu.support.processing.models.enums.FileType;
import com.uniedu.support.processing.models.enums.TicketStatus;
import com.uniedu.support.processing.repositories.*;
import com.uniedu.support.processing.services.implementations.TeacherServiceImpl;
import com.uniedu.support.processing.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final TeacherServiceImpl teacherServiceImpl;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserService<Long> userService;
    private final ChatRepository chatRepository;
    private final TicketRepository ticketRepository;
    private final ViolationReportRepository violationReportRepository;
    private final NotificationService notificationService;
    private final FileMetadataRepository fileMetadataRepository;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATIONS_QUEUE)
    public void handleNotification(Notification notification) {
        if (Objects.requireNonNull(notification.getType()) == NotificationType.VIOLATION_REGISTERED) {
            handleViolationRegister(notification);
        }
    }

    private void handleViolationRegister(Notification notification) {
        log.info(notification.toString());

        Ticket ticket = Ticket.builder()
                .title(notification.getMessage())
                .description("Система зарегистрировала нарушение")
                .status(TicketStatus.IN_PROGRESS)
                .room(roomRepository.findByName(notification.getPayload().get("roomName").toString()).orElseThrow())
                .assignedTo(userService.getUserForTicketAssigmentByRoomName(notification.getPayload().get("roomName").toString()))
                .chat(chatRepository.save(new Chat()))
                .build();

        val savedTicket = ticketRepository.save(ticket);

        fileMetadataRepository.save(
                FileMetadata.builder()
                        .fileType(FileType.IMAGE)
                        .ticket(savedTicket)
                        .storagePath(notification.getPayload().get("storagePath").toString())
                        .fileSize(Long.parseLong(notification.getPayload().get("fileSize").toString()))
                        .originalFileName(notification.getPayload().get("originalFileName").toString()).build());

        ViolationReport violationReport = ViolationReport.builder()
                .ticket(savedTicket)
                .description(notification.getMessage())
                .build();

        notificationService.sendViolationRecordedNotification(violationReportRepository.save(violationReport).getId(), savedTicket.getAssignedTo().getId());
        notificationService.sendTicketAssignedNotification(savedTicket.getId(),savedTicket.getAssignedTo().getId());
    }
}