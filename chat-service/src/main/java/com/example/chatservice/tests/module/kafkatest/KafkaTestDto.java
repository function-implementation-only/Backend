package com.example.chatservice.tests.module.kafkatest;

import com.example.chatservice.kafka.schema.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KafkaTestDto implements Serializable {
    private Schema schema;
    private TestPayload payload;
}
