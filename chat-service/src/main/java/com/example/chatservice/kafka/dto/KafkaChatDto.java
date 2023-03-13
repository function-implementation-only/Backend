package com.example.chatservice.kafka.dto;

import com.example.chatservice.kafka.schema.Payload;
import com.example.chatservice.kafka.schema.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaChatDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
