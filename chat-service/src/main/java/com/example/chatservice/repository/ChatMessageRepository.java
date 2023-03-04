package com.example.chatservice.repository;

import com.example.chatservice.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long > {
}
