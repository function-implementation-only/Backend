package com.example.chatservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
    public class ChatProducer {
        private KafkaTemplate<String, String> kafkaTemplate;

        List<Field> fields = Arrays.asList(
            new Field("bigint", true, "message_id"),
            new Field("datetime", true, "created_at"),
            new Field("datetime", true, "updated_at"),
            new Field("varchar", true,  "message"),
            new Field("bit", true,  "message_check_status"),
            new Field("varchar", true,  "sender"),
            new Field("bigint", true,  "room_id"));
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("chat_message")
            .build();

    @Autowired
    public ChatProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public ChatSimpleDto send(String topic, ChatSimpleDto chatSimpleDto) {
        Payload payload = Payload.builder()
                .message_id               (chatSimpleDto.getMessage_id())
                .created_at               (chatSimpleDto.getCreated_at())
                .updated_at               (chatSimpleDto.getUpdated_at())
                .message                  (chatSimpleDto.getMessage())
                .message_check_status     (chatSimpleDto.getMessage_check_status())
                .sender                   (chatSimpleDto.getSender())
                .room_id                  (chatSimpleDto.getRoom_id())
                .build();

        KafkaChatDto kafkaChatDto = new KafkaChatDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaChatDto);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer sent data from the Order microservice: " + kafkaChatDto);

        return chatSimpleDto;
    }
}
