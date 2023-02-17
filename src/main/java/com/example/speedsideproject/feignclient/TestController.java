package com.example.speedsideproject.feignclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
public class TestController {
    private NotificationServiceClient notificationServiceClient;

    @Autowired
    public TestController(NotificationServiceClient notificationServiceClient){
        this.notificationServiceClient = notificationServiceClient;
    }

    @GetMapping(value = "/test4", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter subscribe(@RequestHeader("auth") String auth,
        @RequestHeader("ACCOUNT-VALUE") String accountValue,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        return notificationServiceClient.subscribe(auth, accountValue, lastEventId);
    }
}
