package com.example.chatservice.chat.repository;

import com.example.chatservice.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long > {
}
