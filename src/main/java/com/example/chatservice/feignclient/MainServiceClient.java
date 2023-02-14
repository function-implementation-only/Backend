package com.example.chatservice.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
@CrossOrigin("*")
@FeignClient(name = "main-service")
public interface MainServiceClient{

    @GetMapping("/main-service/account/info")
    Object getInfo(@RequestHeader("auth") String auth,@RequestHeader("ACCOUNT-VALUE") String accountValue);
}
