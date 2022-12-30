package com.example.speedsideproject.websocket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatResDto {
    private String sender;
    private String message;
    private LocalDateTime createdAt;


    public ChatResDto(ChatReqDto chatReqDto, LocalDateTime sendTime){

        this.sender = chatReqDto.getSender();
        this.message = chatReqDto.getMessage();
        this.createdAt = sendTime;
    }
}
