package com.uniedu.support.processing.services.implementations;

import com.uniedu.support.processing.dto.standart.ChatMessageDto;
import com.uniedu.support.processing.models.entities.Chat;
import com.uniedu.support.processing.models.entities.ChatMessage;
import com.uniedu.support.processing.models.entities.User;
import com.uniedu.support.processing.models.enums.UserRoleType;
import com.uniedu.support.processing.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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

    public ChatMessageDto saveAndReturn(ChatMessageDto dto) {
        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat with provided id not found"));
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender with provided id not found"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver with provided id not found"));

        ChatMessage message = ChatMessage.builder()
                .chat(chat)
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .build();
        ChatMessage saved = chatMessageRepository.save(message);

        return ChatMessageDto.builder()
                .chatId(chat.getId())
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .content(saved.getContent())
                .sentAt(saved.getSentAt())
                .build();
    }


    public List<ChatMessageDto> getAllMessagesForChat(Long chatId, UserDetails userDetails) {
        val ticket  = ticketRepository.findByChatId(chatId).orElseThrow();
        val user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        if(ticket.getAssignedTo().getId().equals(user.getId()) || ticket.getCreator().getId().equals(user.getId()) || user.getRoles().contains(roleRepository.findByRole(UserRoleType.ADMIN))) {
            List<ChatMessage> messages = chatMessageRepository.findByChatIdOrderBySentAtAsc(chatId);
            return messages.stream()
                    .map(message -> ChatMessageDto.builder()
                            .chatId(chatId)
                            .content(message.getContent())
                            .sentAt(message.getSentAt())
                            .isRead(message.getIsRead())
                            .receiverId(message.getReceiver().getId())
                            .senderId(message.getSender().getId())
                            .build())
                    .toList();
        }else{
            throw new IllegalArgumentException("User dont have permission to access this messages");
        }
    }
}
