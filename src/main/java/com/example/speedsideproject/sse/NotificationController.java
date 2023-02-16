package com.example.speedsideproject.sse;

import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.example.speedsideproject.sse.dto.NotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getAccount().getEmail(), lastEventId);
    }

    @PostMapping(value = "/chat-notification")
    public ResponseEntity send(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody NotificationDto.CreateRequest createRequest){
         notificationService.send(createRequest);
         return  ResponseEntity.ok().build();
    }

}