package com.example.apigate.filter.jwt.filter;

import com.example.apigate.filter.jwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthFilter(JwtUtil jwtUtil) {
        super(AuthFilter.Config.class);
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
        //config
    }

    /*토큰 검증 필터*/
    @Override
    public GatewayFilter apply(AuthFilter.Config config) {
        return (exchange, chain) -> {
            //ServerHttpRequest
            ServerHttpRequest request = exchange.getRequest();

            List<String> accessToken = jwtUtil.getHeaderToken(request, "Access");
            List<String> refreshToken = jwtUtil.getHeaderToken(request, "Refresh");


            if (accessToken != null && jwtUtil.tokenValidation(accessToken.get(0))) {
//                response.getHeaders().set("Auth", "true");
                request.mutate().header("Auth", "true").build();
                request.mutate().header("Account-Value", jwtUtil.getEmailFromToken(accessToken.get(0))).build();
                return chain.filter(exchange);
            }

            request.mutate().header("Auth", "false").build();
            return chain.filter(exchange);
        };
    }
}