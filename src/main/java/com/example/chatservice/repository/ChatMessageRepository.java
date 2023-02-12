package com.example.chatservice.repository;

import com.example.chatservice.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long > {
}
