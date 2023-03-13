package com.example.speedsideproject.modules.social.google.controller;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.modules.social.google.dto.GoogleUser;
import com.example.speedsideproject.modules.social.google.service.OAuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SocialController {

    private final OAuthService oAuthService;

    @ApiOperation(value = "구글 간편 로그인 api주소 ", notes = "{socialLoginType}에 GOOGLE 입력")
    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath) throws IOException {
        GoogleUser.SocialLoginType socialLoginType = GoogleUser.SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.request(socialLoginType);
    }
    @ApiOperation(value = "구글 간편 로그인 api주소 ", notes = "{socialLoginType}에 GOOGLE 입력")
    @GetMapping(value = "/{socialLoginType}/test")
    public ResponseDto<?> callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :" + code);
        GoogleUser.SocialLoginType socialLoginType = GoogleUser.SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        return ResponseDto.success(oAuthService.oAuthLogin(socialLoginType, code));
    }

}
