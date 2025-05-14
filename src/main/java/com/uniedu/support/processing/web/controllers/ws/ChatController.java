package com.uniedu.support.processing.web.controllers.ws;

import com.uniedu.support.processing.dto.standart.ChatMessageDto;

import com.uniedu.support.processing.services.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Tag(name = "WebSocket chat", description = "Необходимые ручки для обмена сообщениями по протоколу web-socket")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto message) {
        ChatMessageDto saved = chatService.saveAndReturn(message);
        String destination = "/topic/chat." + message.getChatId();
        messagingTemplate.convertAndSend(destination, saved);
    }
}
