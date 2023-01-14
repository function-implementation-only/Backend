package com.example.speedsideproject.account.service;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.account.dto.LoginReqDto;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.account.entity.RefreshToken;
import com.example.speedsideproject.account.repository.AccountRepository;
import com.example.speedsideproject.account.repository.RefreshTokenRepository;
import com.example.speedsideproject.applyment.dto.ApplymentResponseDto;
import com.example.speedsideproject.applyment.repository.ApplymentRepository;
import com.example.speedsideproject.aws_s3.S3UploadUtil;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.jwt.dto.TokenDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import com.example.speedsideproject.post.PostRepository;
import com.example.speedsideproject.post.PostResponseDto2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.speedsideproject.error.ErrorCode.*;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    private final ApplymentRepository applymentRepository;
    private final S3UploadUtil s3UploadUtil;


    //mailSender 인증용
    private final JavaMailSender emailSender;
    private String authNum;


    public ResponseDto<?> signup(AccountReqDto accountReqDto) {
        // email 중복 검사
        if (accountRepository.findByEmail(accountReqDto.getEmail()).isPresent()) {
            throw new CustomException(OVERLAP_CHECK);
        }

        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
        Account account = new Account(accountReqDto);

        accountRepository.save(account);
        return ResponseDto.success("Signup success");
    }


    public TokenDto login(LoginReqDto loginReqDto, HttpServletResponse response) {

        Account account = accountRepository.findByEmail(loginReqDto.getEmail()).orElseThrow(() -> new RuntimeException("Not found Account"));

        if (!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new CustomException(NOT_MATCHED_PASSWORD);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);

        return new TokenDto(tokenDto){
            public Long accountId =account.getId();
        };
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    //내 글목록 가져오기
    public List<PostResponseDto2> getMyPost(Account account) {
        return postRepository.findAllByAccount(account).stream().map(post -> new PostResponseDto2(post, account)).collect(Collectors.toList());
    }

    // 내 댓글 가져오기
    public List<ApplymentResponseDto> getMyComment(Account account) {
        return applymentRepository.findAllByAccount(account).stream().map(ApplymentResponseDto::new).collect(Collectors.toList());
    }

    //logout 기능

    public ResponseDto<?> logout(String email) {
        var refreshToken = refreshTokenRepository.findByAccountEmail(email).orElseThrow(
                () -> new CustomException(REFRESH_TOKEN_IS_EXPIRED)
        );
        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("Delete Success");
    }

    //profile edit 기능
    public ResponseDto<?> editMyInfo(Account account, UserInfoDto userInfoDto, MultipartFile imgFile) throws IOException {
        System.out.println("서비스 시작");
        // 아이디 존재 여부 체크
        Account editMyAccount = accountRepository.findById(account.getId()).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        // Account repo에 url 및 key 저장
        if (!(imgFile == null)) {
            var r = s3UploadUtil.upload(imgFile, "profile-img");
            editMyAccount.update(userInfoDto, r);
        } else {
            editMyAccount.update(userInfoDto);
        }
        return ResponseDto.success("Profile edited");
    }

    //Methods
    public void accountCheck(Account account) {
        accountRepository.findById(account.getId()).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }


    /*랜덤 인증 코드 생성*/
    public void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    /*메일 양식 작성*/
    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = "chem.en.9273@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "Veloce 팀빌딩 서비스 회원가입 인증 번호"; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText("<div style=\"margin:100px;\">" +
                "<h1> Veloce 인증번호 입니다.</h1>\n<br>" +
                "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요.</p>\n<br>" +
                "<h2>인증번호 : " + authNum + "</h2>" +
                "<br/>\n" +
                "</div>", "utf-8", "html");

        return message;
    }

    /*실제 메일 전송*/
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        //실제 메일 전송
        emailSender.send(emailForm);
        return authNum; //인증 코드 반환
    }

    /*email 중복 체크*/
    public boolean emailCheck(String email) {
        log.info(email);
        return !accountRepository.existsByEmail(email);
    }
}