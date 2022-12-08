package com.example.speedsideproject.sse;

import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
//    @GetMapping(value = "/subscribe", produces = "text/event-stream")
//    public SeeEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                @RequestHeader(required = false,defaultValue = "")
//                                String lastEventId) {
//        return
//    }

}
