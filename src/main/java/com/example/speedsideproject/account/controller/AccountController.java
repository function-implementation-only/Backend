package com.example.speedsideproject.account.controller;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.account.dto.LoginReqDto;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.account.service.AccountService;
import com.example.speedsideproject.global.dto.GlobalResDto;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    //회원가입
    @PostMapping("/account/signup")
    public ResponseDto<?> signup(@RequestBody @Valid AccountReqDto accountReqDto) {
        return ResponseDto.success(accountService.signup(accountReqDto));
    }
    //로그인
    @PostMapping("/account/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        return ResponseDto.success(accountService.login(loginReqDto, response));
    }
    @GetMapping("/issue/token")
    public GlobalResDto issuedToken(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails, HttpServletResponse response) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getEmail(), "Access"));
        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
    }
    //내 포스트 불러오기
    @GetMapping("/mypost")
    public ResponseDto<?> getMyPost(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyPost(userDetails.getAccount()));
    }
    //내 커멘트 불러오기
    @GetMapping("/mycomment")
    public ResponseDto<?> getMyComment(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyComment(userDetails.getAccount()));
    }
    //로그 아웃
    @PostMapping(value = "/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws Exception {
        return ResponseDto.success(accountService.logout(userDetails.getAccount().getEmail()));

    }

    //내 프로필 편집하기
    @PatchMapping
    public ResponseDto<?> editMyInfo(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails, @RequestPart(required = false,name = "userInfo") UserInfoDto userInfoDto, @RequestPart(required = false,name = "profileImg") MultipartFile profileImg) throws IOException {
        return accountService.editMyInfo(userDetails.getAccount(), userInfoDto, profileImg);
    }

}

