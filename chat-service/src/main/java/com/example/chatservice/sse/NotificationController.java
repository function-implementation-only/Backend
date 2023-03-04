package com.example.chatservice.sse;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.sse.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        log.info("컨트롤러 실행");
        return  notificationService.subscribe(userDetails.getAccount(), lastEventId);
    }

    @PostMapping(value = "/chat-notification")
    public ResponseEntity send(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody NotificationDto.CreateRequest createRequest){
        log.info("컨트롤러 실행");
        notificationService.send(createRequest);
        return  ResponseEntity.ok().build();
    }

}