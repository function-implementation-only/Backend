package com.example.chatservice.kafka;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Payload {
    private Long          message_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String         message;
    private String         message_check_status;
    private String         sender;
    private Long           room_id;

}