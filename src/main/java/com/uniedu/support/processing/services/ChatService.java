package com.uniedu.support.processing.services;

import com.uniedu.support.processing.dto.standart.ChatMessageDto;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.ChatMessage;
import com.uniedu.support.processing.models.entities.Ticket;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.repositories.*;
import com.uniedu.support.processing.services.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final TicketRepository ticketRepository;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;

    public ChatMessageDto saveAndReturn(ChatMessageDto dto) {
        log.info("Saving new message for chat ID: {}, sender ID: {}", dto.getChatId(), dto.getSenderId());

        try {
            Chat chat = chatRepository.findById(dto.getChatId())
                    .orElseThrow(() -> {
                        log.error("Chat not found with ID: {}", dto.getChatId());
                        return new IllegalArgumentException("Chat not found");
                    });

            User sender = userRepository.findById(dto.getSenderId())
                    .orElseThrow(() -> {
                        log.error("Sender not found with ID: {}", dto.getSenderId());
                        return new IllegalArgumentException("Sender not found");
                    });

            User receiver = userRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> {
                        log.error("Receiver not found with ID: {}", dto.getReceiverId());
                        return new IllegalArgumentException("Receiver not found");
                    });

            ChatMessage message = ChatMessage.builder()
                    .chat(chat)
                    .sender(sender)
                    .receiver(receiver)
                    .content(dto.getContent())
                    .sentAt(LocalDateTime.now())
                    .isRead(false)
                    .build();

            ChatMessage saved = chatMessageRepository.save(message);
            notificationService.sendNewMessageNotification(chat.getId(),message.getId(), receiver.getId());
            log.debug("Message saved with ID: {}", saved.getId());

            return mapToDto(saved);

        } catch (Exception e) {
            log.error("Error saving message: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<ChatMessageDto> getAllMessagesForChat(Long chatId, UserDetails userDetails) {
        log.info("Fetching messages for chat ID: {}, user: {}", chatId, userDetails.getUsername());

        try {
            Ticket ticket = ticketRepository.findByChatId(chatId)
                    .orElseThrow(() -> {
                        log.error("Ticket not found for chat ID: {}", chatId);
                        return new IllegalArgumentException("Ticket not found");
                    });

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> {
                        log.error("User not found: {}", userDetails.getUsername());
                        return new IllegalArgumentException("User not found");
                    });

            if (!hasAccessToChat(ticket, user)) {
                log.warn("Access denied for user {} to chat {}", user.getId(), chatId);
                throw new IllegalArgumentException("Access denied");
            }

            List<ChatMessage> messages = chatMessageRepository.findByChatIdOrderBySentAtAsc(chatId);
            log.debug("Found {} messages for chat ID: {}", messages.size(), chatId);

            return messages.stream()
                    .map(this::mapToDto)
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching messages: {}", e.getMessage(), e);
            throw e;
        }
    }

    private boolean hasAccessToChat(Ticket ticket, User user) {
        boolean isAdmin = user.getRoles().contains(roleRepository.findByRole(UserRoleType.ADMIN));
        boolean isAssigned = ticket.getAssignedTo().getId().equals(user.getId());
        boolean isCreator = ticket.getCreator().getId().equals(user.getId());

        return isAdmin || isAssigned || isCreator;
    }

    private ChatMessageDto mapToDto(ChatMessage message) {
        return ChatMessageDto.builder()
                .chatId(message.getChat().getId())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .isRead(message.getIsRead())
                .receiverId(message.getReceiver().getId())
                .senderId(message.getSender().getId())
                .build();
    }
}
