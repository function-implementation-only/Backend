package com.example.speedsideproject.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
