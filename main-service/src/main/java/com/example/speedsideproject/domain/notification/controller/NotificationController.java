package com.example.speedsideproject.domain.notification.controller;


import com.example.speedsideproject.domain.notification.dto.NotificationDto;
import com.example.speedsideproject.domain.notification.service.NotificationService;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.global.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;


@CrossOrigin("*")
@RequestMapping("/notifications")
@RestController
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /*SSE 구독*/
    @ApiOperation(value = "SSE 구독 ", notes = "SSE 구독 API (토큰필요)")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(userDetails.getAccount().getEmail(), lastEventId);
    }

    /*SSE test*/
    @ApiOperation(value = "알림 test api", notes = "알림 test api")
    @GetMapping(value = "/subscribe-test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeTest(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe("string", lastEventId);
    }

    @ApiOperation(value = "알림 전송", notes = "알림을 전송 합니다.(토큰 필요)")
    @PostMapping(value = "/chat-notification")
    public ResponseEntity send(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,
                               @RequestBody NotificationDto.CreateRequest createRequest) {
        notificationService.send(createRequest);
        return ResponseEntity.ok().build();
    }

    /*읽지않은 알람 list 가져오기*/
    @ApiOperation(value = "읽지 않은 알람 가져오기", notes = "읽지 않은 알람 목록을 가져옵니다(토큰 필요)")
    @GetMapping("/list")
    public ResponseDto<?> getNotificationList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(notificationService.getNotificationList(userDetails));
    }
}