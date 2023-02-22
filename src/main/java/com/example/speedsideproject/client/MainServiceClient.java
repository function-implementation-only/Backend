package com.example.speedsideproject.client;



import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@CrossOrigin("*")
@FeignClient(name = "main-service")
public interface MainServiceClient {

    @GetMapping("/main-service/account/info")
    Object getInfo(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue);


    @GetMapping("/main-service/account/info")
    ResponseDto<?> myInfo(@AuthenticationPrincipal UserDetailsImpl userDetails);
}
