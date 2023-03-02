package com.example.chatservice.tests.kafkatest;

import com.example.chatservice.kafka.*;
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
public class TestProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
//            new Field("int64", false, "test_id"),
            new Field("string", true, "content"),
            new Field("string", true, "title")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("testing")
            .build();

    @Autowired
    public TestProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public String send(String topic, TestDto testDto) {
        TestPayload payload = TestPayload.builder()
//                .test_id(testDto.getTestId())
//                .test_id(26L)
                .content(testDto.getContent())
                .title(testDto.getTitle())
                .build();

        KafkaTestDto kafkaTestDto = new KafkaTestDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaTestDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }


        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer sent data from the Order microservice: " + kafkaTestDto);

//        return testDto;
        return jsonInString;
    }
}
