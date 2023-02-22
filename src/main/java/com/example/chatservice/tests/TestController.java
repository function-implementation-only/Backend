package com.example.chatservice.tests;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.feignclient.MainServiceClient;
import com.example.chatservice.feignclient.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
public class TestController {

    private final MainServiceClient mainServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public TestController(MainServiceClient mainServiceClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.mainServiceClient = mainServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test2")
    public String test2(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("유저 계정{}", userDetails.getAccount());
        userDetails.getAccount();
        log.info("유저 이름{}", userDetails.getUsername());
        return userDetails.getAccount();
    }

    @GetMapping("/test3")
    public Object test3(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        Object myInfo = mainServiceClient.getInfo(auth, accountValue);
        return myInfo;
    }

    @GetMapping("/test4")
    public Object test4(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Object run = circuitBreaker.run(() -> mainServiceClient.getInfo(auth, accountValue));
        return run;
    }
    @GetMapping("/test5")
    public Object test5(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        /*Zipkin trace */
        log.info("trace start");
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Object run = circuitBreaker.run(() -> mainServiceClient.getInfo(auth, accountValue), throwable ->new ArrayList<>());
        log.info("trace end");
        return run;
    }

    @GetMapping("/test6")
    public Object test6() {
        /*Zipkin trace */
        log.info("trace start");
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Object run = circuitBreaker.run(() -> mainServiceClient.getInfo("true", "string"), throwable ->new ArrayList<>());
        log.info("trace end");
        return run;
    }
    @GetMapping("/test7")
    public Object test7() {
        /*Zipkin trace */
        log.info("trace start");
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        UserResponseDto run = circuitBreaker.run(() -> mainServiceClient.getInfo("true", "string"));
        log.info("trace end");
        return run;
    }
}
