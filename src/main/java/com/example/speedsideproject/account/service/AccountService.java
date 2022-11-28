package com.example.speedsideproject.account.service;


import com.example.speedsideproject.account.dto.AccountReqDto;
import com.example.speedsideproject.account.dto.LoginReqDto;
import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.account.entity.RefreshToken;
import com.example.speedsideproject.account.repository.AccountRepository;
import com.example.speedsideproject.account.repository.RefreshTokenRepository;
import com.example.speedsideproject.aws_s3.S3UploadUtil;
import com.example.speedsideproject.comment.dto.CommentResponseDto;
import com.example.speedsideproject.comment.repository.CommentRepository;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.jwt.dto.TokenDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import com.example.speedsideproject.post.PostRepository;
import com.example.speedsideproject.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.speedsideproject.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final S3UploadUtil s3UploadUtil;

    @Transactional
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

    @Transactional
    public ResponseDto<?> login(LoginReqDto loginReqDto, HttpServletResponse response) {

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

        return ResponseDto.success("Login success");
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    //내 글목록 가져오기
    public List<PostResponseDto> getMyPost(Account account) {
        return postRepository.findAllByAccount(account).stream().map(post -> new PostResponseDto(post, account)).collect(Collectors.toList());
    }

    // 내 댓글 가져오기
    public List<CommentResponseDto> getMyComment(Account account) {
        return commentRepository.findAllByAccount(account).stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    //logout 기능
    @Transactional
    public ResponseDto<?> logout(String email) {
        var refreshToken = refreshTokenRepository.findByAccountEmail(email).orElseThrow(
                () -> new CustomException(REFRESH_TOKEN_IS_EXPIRED)
        );
        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("Delete Success");
    }

    //profile edit 기능
    public ResponseDto<?> editMyInfo(Account account, UserInfoDto userInfoDto, MultipartFile imgFile) throws IOException {
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

}