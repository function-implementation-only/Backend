package com.example.speedsideproject.feignclient;

import com.example.speedsideproject.client.ChatServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private ChatServiceClient chatServiceClient;


    @GetMapping(value = "/test4", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter subscribe(@RequestHeader("auth") String auth,
        @RequestHeader("ACCOUNT-VALUE") String accountValue,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        return chatServiceClient.subscribe(auth, accountValue, lastEventId);
    }
    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "test";
    }
}
