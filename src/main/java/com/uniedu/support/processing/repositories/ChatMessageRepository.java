package com.uniedu.support.processing.repositories;

import com.uniedu.support.processing.models.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatIdOrderBySentAtAsc(Long chatId);
}
