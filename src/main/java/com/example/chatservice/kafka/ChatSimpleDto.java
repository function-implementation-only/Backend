package com.example.chatservice.kafka;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ChatSimpleDto {

    public Long          message_id;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
    public String         message;
    public String         message_check_status;
    public String         sender;
    public Long           room_id;
}
