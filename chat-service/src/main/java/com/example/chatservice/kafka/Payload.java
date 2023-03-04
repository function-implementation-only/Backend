package com.example.chatservice.kafka;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Payload {
//    private Long          message_id;
    private Long           created_at;
    private Long           updated_at;
    private String         message;
    private byte       message_check_status;
    private String         sender;
    private Long           room_id;

}