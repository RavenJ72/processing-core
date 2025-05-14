package com.uniedu.support.processing.web.controllers;

import com.uniedu.support.processing.dto.standart.ChatMessageDto;
import com.uniedu.support.processing.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-messages")
public class ChatMessageRestController {

    private final ChatService chatMessageService;


    @GetMapping("/{chatId}")
    public List<ChatMessageDto> getMessagesByChatId(@PathVariable Long chatId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return chatMessageService.getAllMessagesForChat(chatId, userDetails);
    }
}

