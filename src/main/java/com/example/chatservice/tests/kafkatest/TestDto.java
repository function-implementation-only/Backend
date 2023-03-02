package com.example.chatservice.tests.kafkatest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestDto {

    public Long testId;
    public String title;
    public String content;


}
