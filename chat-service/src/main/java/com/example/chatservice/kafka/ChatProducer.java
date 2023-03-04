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
            new Field("int64", false, "created_at"){public String name="org.apache.kafka.connect.data.Timestamp";public int version =1;},
            new Field("int64", false, "updated_at"){public String name="org.apache.kafka.connect.data.Timestamp";public int version =1;},
            new Field("string", true, "message"),
            new Field("int8", false, "message_check_status"),
            new Field("string", true, "sender"),
            new Field("int64", true, "room_id"));
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
                .created_at(System.currentTimeMillis())
                .updated_at(System.currentTimeMillis())
                .message(chatSimpleDto.getMessage())
                .message_check_status((byte) 0)
                .sender(chatSimpleDto.getSender())
                .room_id(chatSimpleDto.getRoom_id())
                .build();

        KafkaChatDto kafkaChatDto = new KafkaChatDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaChatDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Chat Producer sent data from the chat-service: " + kafkaChatDto);

        return chatSimpleDto;
    }
}
