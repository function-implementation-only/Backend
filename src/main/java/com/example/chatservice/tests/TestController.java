package com.example.chatservice.tests;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class    TestController {
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test2")
    public String test2(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("유저 계정{}",userDetails.getAccount());
        userDetails.getAccount();
        log.info("유저 이름{}",userDetails.getUsername());
        return userDetails.getAccount();
    }
}
