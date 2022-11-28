package com.example.speedsideproject.social.service;

import com.example.speedsideproject.account.dto.UserInfoDto;
import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.account.entity.RefreshToken;
import com.example.speedsideproject.account.repository.AccountRepository;
import com.example.speedsideproject.account.repository.RefreshTokenRepository;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.error.ErrorCode;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.jwt.dto.TokenDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.example.speedsideproject.social.dto.SocialUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialKakaoService {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("%{spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String user_info_uri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String token_uri;

    private final AccountRepository accountRepository;
    public final RefreshTokenRepository refreshTokenRepository;
    public final JwtUtil jwtUtil;

    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        //인가코드를 통해 access_token 발급받기
        String accessToken = issuedAccessToken(code);

        //access_token을 통해 사용자 정보가져오기
        SocialUserInfoDto socialUserInfoDto = getKakaoUserInfo(accessToken);

        //사용자정보를 토대로 가입진행하기(일단 DB에 저장이 되어있는지 확인후)
        Account account = saveAccount(socialUserInfoDto);

        //강제 로그인 처리
        forceLoginUser(account);

        //토큰 발급후 response
        createToken(account,response);

        UserInfoDto userInfoDto = new UserInfoDto(account);

        return ResponseDto.success("kakao signup success");
    }

    private String issuedAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);
        body.add("client_secret", kakaoClientSecret);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                token_uri,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }


    //access_token을 통해 사용자 정보가져오기
    private SocialUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                user_info_uri,
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String userImgUrl = jsonNode.get("properties")
                .get("profile_image").asText();

        return new SocialUserInfoDto(id, nickname, email, userImgUrl);
    }

    private Account saveAccount(SocialUserInfoDto socialUserInfoDto) {
        Account kakaoMember = accountRepository.findByEmail("k_"+socialUserInfoDto.getEmail()).orElse(null);

        if (kakaoMember == null) {
            Account account = Account.builder()
                    .email("k_" + socialUserInfoDto.getEmail())
                    .nickname(socialUserInfoDto.getNickname())
                    .imgUrl(socialUserInfoDto.getUserImgUrl())
                    .isAccepted(false)
                    .isDeleted(false)
                    .build();
            accountRepository.save(account);
            return account;
        }
        return kakaoMember;
    }
    private void forceLoginUser(Account account) {
        UserDetails userDetails = new UserDetailsImpl(account);
        if (account.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private void createToken(Account account, HttpServletResponse response) {
        TokenDto tokenDto = jwtUtil.createAllToken(account.getEmail());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(account.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), account.getEmail());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
