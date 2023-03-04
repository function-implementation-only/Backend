package com.example.chatservice.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaChatDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
