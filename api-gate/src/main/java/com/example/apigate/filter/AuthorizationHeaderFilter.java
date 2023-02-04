package com.example.apigate.filter;

import com.example.apigate.jwt.util.JwtUtil;
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
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthorizationHeaderFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
        //config
    }

    /*토큰 검증 필터*/
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //ServerHttpRequest
            ServerHttpRequest request = exchange.getRequest();

            List<String> accessToken = jwtUtil.getHeaderToken(request, "Access");
            List<String> refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

            if (accessToken == null) {
                return jwtUtil.onError(exchange, "No Access token", HttpStatus.UNAUTHORIZED);
            }
            if (!jwtUtil.tokenValidation(accessToken.get(0))) {
                return jwtUtil.onError(exchange, "AccessToken is not Valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        };
    }
}
