package com.example.speedsideproject.account.controller;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.account.dto.EmailRequestDto;
import com.example.speedsideproject.account.dto.LoginReqDto;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.account.service.AccountService;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    //회원가입
    @ApiOperation(value = "회원가입", notes = "email조건 : x, pw조건 : x")
    @PostMapping("/signup")
    public ResponseDto<?> signup(@RequestBody @Valid AccountReqDto accountReqDto) {
        return ResponseDto.success(accountService.signup(accountReqDto));
    }

    //로그인
    @ApiOperation(value = "로그인", notes = "로그인시 토큰 발급")

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {

        return ResponseDto.success(accountService.login(loginReqDto, response));
    }

//    @GetMapping("/issue/token")
//    public GlobalResDto issuedToken(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails, HttpServletResponse response) {
//        response.addHeader(JwtUtil.ACCESS_TOKEN, jwtUtil.createToken(userDetails.getAccount().getEmail(), "Access"));
//        return new GlobalResDto("Success IssuedToken", HttpStatus.OK.value());
//    }

    //이메일 인증
    @ApiOperation(value = "이메일 인증", notes = "회원가입 시 토큰 발급")
    @PostMapping("/signup/email-check")
    public String mailConfirm(@RequestBody @Valid EmailRequestDto email) throws MessagingException, UnsupportedEncodingException {
        return accountService.sendEmail(email.getEmail());
    }

    //내 포스트 불러오기
    @ApiOperation(value = "내 포스트 불러오기", notes = "내가 작성한 포스트 리스트 불러오기 최근 5개")
    @GetMapping("/mypost")
    public ResponseDto<?> getMyPost(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyPost(userDetails.getAccount()));
    }

    //내 커멘트 불러오기
    @ApiOperation(value = "내 지원 불러오기 ", notes = "내 지원 불러오기 최근 5개")
    @GetMapping("/mycomment")
    public ResponseDto<?> getMyComment(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyComment(userDetails.getAccount()));
    }
    //내 북마크 불러오기
    @ApiOperation(value = "내 북마크 불러오기 ", notes = "내 북마크 불러오기 최근 5개")
    @GetMapping("/mylikes")
    public ResponseDto<?> getMyLikes(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.getMyLikes(userDetails.getAccount()));
    }

    //로그 아웃
    @ApiOperation(value = "로그 아웃", notes = "백엔드에서 리프래시 토큰만 삭제 합니다")
    @PostMapping(value = "/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws Exception {
        return ResponseDto.success(accountService.logout(userDetails.getAccount().getEmail()));

    }

    //내 프로필 편집하기
    @ApiOperation(value = "내 프로필 편집하기", notes = "나의 프로필 편집")
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> editMyInfo(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails, @RequestPart(required = false, name = "userInfo") UserInfoDto userInfoDto, @RequestPart(required = false, name = "profileImg") MultipartFile profileImg) throws IOException {
        return accountService.editMyInfo(userDetails.getAccount(), userInfoDto, profileImg);
    }

    /*이메일 중복체크*/
    @PostMapping("/email")
    public ResponseDto<?> emailCheck(@RequestBody @Valid EmailRequestDto email) {
        return ResponseDto.success(accountService.emailCheck(email.getEmail()));
    }

    /*내 프로필 */
    @ApiOperation(value = "내 프로필", notes = "나의 프로필")
    @GetMapping("/info")
    public ResponseDto<?> myInfo(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        System.out.println("컨트롤러 시작");
        return ResponseDto.success(accountService.myInfo(userDetails));
    }
    /*비밀번호 체크 로직*/
    @ApiOperation(value = "비밀번호 일치?", notes = "비밀번호 일치?")
    @PostMapping("/password/check")
    public ResponseDto<?> checkPassword(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,@RequestBody @Valid LoginReqDto loginReqDto) throws IOException{
        return ResponseDto.success(accountService.checkPassword(userDetails,loginReqDto));
    }
    /*비밀번호 변경 로직*/
    @ApiOperation(value = "비밀번호 변경", notes = "비밀변호 변경")
    @PatchMapping("/password/change")
    public ResponseDto<?> changePassword(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,@RequestBody @Valid LoginReqDto loginReqDto) throws IOException{
        return ResponseDto.success(accountService.changePassword(userDetails,loginReqDto));
    }
}

