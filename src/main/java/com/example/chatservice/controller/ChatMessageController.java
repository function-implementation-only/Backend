package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatDto.CreateRequest;
import com.example.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin("*")
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void enter(CreateRequest request) {
        log.info("메세지 생성메서드 실행");
        String roomName = chatMessageService.save(request);
        log.info("roomName:{}",roomName);
        log.info(request.getMessage());
        sendingOperations.convertAndSend("/sub/chatroom/" + roomName, request.getMessage());
    }
}

