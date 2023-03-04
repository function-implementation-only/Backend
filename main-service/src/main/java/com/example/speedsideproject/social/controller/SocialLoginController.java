package com.example.speedsideproject.social.controller;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.social.service.SocialKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/socials/signup")
public class SocialLoginController {

    private final SocialKakaoService socialKakaoService;

    @GetMapping("/kakao")
    public ResponseDto<?> kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        return ResponseDto.success(socialKakaoService.kakaoLogin(code, response));
    }
}