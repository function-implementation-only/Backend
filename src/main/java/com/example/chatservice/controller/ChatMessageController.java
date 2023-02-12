package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatDto;
import com.example.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/hello")
    public void enter(ChatDto dto) {
        log.info("메세지 생성메서드 실행");
        chatMessageService.save(dto);

//        sendingOperations.convertAndSend("/sub/channel/" + dto.getRoom().getId(), dto);
    }
}

