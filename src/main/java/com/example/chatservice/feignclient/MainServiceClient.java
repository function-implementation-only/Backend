package com.example.chatservice.feignclient;


import com.example.chatservice.config.dto.ResponseDto;
import com.example.chatservice.config.security.user.UserDetailsImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin("*")
@FeignClient(name = "main-service")
public interface MainServiceClient{

    @GetMapping("/main-service/account/info")
    Object getMyInfo(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue);


    @GetMapping("/main-service/account/info")
    UserResponseDto getInfo(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue);
}
