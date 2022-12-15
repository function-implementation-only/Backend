package com.example.speedsideproject.websocket.controller;

import com.example.speedsideproject.websocket.domain.Chat;
import com.example.speedsideproject.websocket.dto.ChatReqDto;
import com.example.speedsideproject.websocket.dto.ChatResDto;
import com.example.speedsideproject.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @MessageMapping(value = "{roomId}")
    @SendTo("/sub/{roomId}")
    public void message(@DestinationVariable Long roomId, @Valid ChatReqDto message) throws MessagingException {

        //채팅 저장
        Chat chat = chatService.createChat(roomId, message);

        ChatResDto chatResDto = new ChatResDto(message,chat.getSendDate());
        template.convertAndSend("/sub/" + roomId,chatResDto);

    }
}
