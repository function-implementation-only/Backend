package com.example.speedsideproject.websocket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChatReqDto
{
    @NotNull
    private String sender;
    @NotBlank
    private String message;
}
