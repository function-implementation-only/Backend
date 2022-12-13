package com.example.speedsideproject.google;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.account.entity.RefreshToken;
import com.example.speedsideproject.account.repository.AccountRepository;
import com.example.speedsideproject.account.repository.RefreshTokenRepository;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.error.ErrorCode;
import com.example.speedsideproject.jwt.dto.TokenDto;
import com.example.speedsideproject.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;




    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                System.out.println("리다이렉트 들어갑니다");
                redirectURL = googleOauth.getOauthRedirectURL();
            }
            break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }

        response.sendRedirect(redirectURL);
    }

    public TokenDto oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException {
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);

                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                System.out.println(oAuthToken.getAccess_token());
                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                String user_id = googleUser.getEmail();

                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
                Account account = accountRepository.findByEmail(user_id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
                account.getEmail();
                TokenDto tokenDto = jwtUtil.createAllToken(account.getEmail());
                Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(user_id);

                if (refreshToken.isPresent()) {
                    refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
                } else {
                    RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), user_id);
                    refreshTokenRepository.save(newToken);
                }

               setHeader(response, tokenDto);
                System.out.println("access_token:"+tokenDto.getAccessToken());
                return tokenDto;

            }

            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }
    }
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}