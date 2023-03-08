package com.example.speedsideproject.sse;


import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.example.speedsideproject.sse.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@CrossOrigin("*")
@RequestMapping("/notifications")
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
        return notificationService.subscribe(userDetails.getAccount().getEmail(), lastEventId);
    }

    @GetMapping(value = "/subscribe-test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeTest(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe("string", lastEventId);
    }

    @PostMapping(value = "/chat-notification")
    public ResponseEntity send(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               @RequestBody NotificationDto.CreateRequest createRequest) {
        notificationService.send(createRequest);
        return ResponseEntity.ok().build();
    }

    /*읽지않은 알람 list 가져오기*/
    @GetMapping("/list")
    public ResponseDto<?> getNotificationList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(notificationService.getNotificationList(userDetails));
    }
/*
* /notifications/list*/
}