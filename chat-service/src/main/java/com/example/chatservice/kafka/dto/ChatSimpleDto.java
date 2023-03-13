package com.example.chatservice.kafka.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ChatSimpleDto {

//    public Long          message_id;
    public Long created_at;
    public Long updated_at;
    public String         message;
    public byte         message_check_status;
    public String         sender;
    public Long           room_id;
}
