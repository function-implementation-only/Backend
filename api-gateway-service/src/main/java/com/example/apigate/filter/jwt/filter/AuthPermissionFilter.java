package com.example.apigate.filter.jwt.filter;

import com.example.apigate.filter.jwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AuthPermissionFilter extends AbstractGatewayFilterFactory<AuthPermissionFilter.Config> {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthPermissionFilter(JwtUtil jwtUtil) {
        super(AuthPermissionFilter.Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
        //config
    }

    /*토큰 검증 필터*/
    @Override
    public GatewayFilter apply(AuthPermissionFilter.Config config) {
        return (exchange, chain) -> {
            //ServerHttpRequest
            ServerHttpRequest request = exchange.getRequest();

            List<String> accessToken = jwtUtil.getHeaderToken(request, "Access");
            List<String> refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

            /*토큰 x or 검증 -> error 반환*/
            if (accessToken == null) {
                return jwtUtil.onError(exchange, "No Access token", HttpStatus.UNAUTHORIZED);
            }
            if (!jwtUtil.tokenValidation(accessToken.get(0))) {
                return jwtUtil.onError(exchange, "AccessToken is not Valid", HttpStatus.UNAUTHORIZED);
            }

            /*정상 토큰이 존재하는 경우*/
            request.mutate().header("Auth", "true").build();
            request.mutate().header("Account-Value", jwtUtil.getEmailFromToken(accessToken.get(0))).build();
            return chain.filter(exchange);

        };
    }
}