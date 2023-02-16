package com.example.chatservice.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "main-service")
public interface MainServiceClient{

    @GetMapping("/main-service/account/info")
    Object getInfo(@RequestHeader("auth") String auth,@RequestHeader("ACCOUNT-VALUE") String accountValue);

    @GetMapping("/main-service/subscribe")
    Object getSSE(@RequestHeader("auth") String auth,@RequestHeader("ACCOUNT-VALUE") String accountValue,
    @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId);
}
