package com.example.speedsideproject.client;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin("*")
@FeignClient(name = "chat-service")
public interface ChatServiceClient {

    @GetMapping("/chat-service/chat/info")
    Object getInfo(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue);


    @GetMapping("/chat-service/chat/info2")
    ResponseDto<?> myInfo(@AuthenticationPrincipal UserDetailsImpl userDetails);

    @GetMapping(value = "/chat-service/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter subscribe(@RequestHeader("auth") String auth,
                         @RequestHeader("ACCOUNT-VALUE") String accountValue,
                         @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId);
}
