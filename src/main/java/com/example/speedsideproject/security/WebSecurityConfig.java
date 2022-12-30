package com.example.speedsideproject.security;

import com.example.speedsideproject.jwt.filter.JwtAuthFilter;
import com.example.speedsideproject.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //소셜 로그인을 위해 임시 설정
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*", "Accept", "Access_Token", "Cache-Control", "Referer", "Refresh_Token", "User-Agent"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/account/**").permitAll()
                .antMatchers("/posts/**").permitAll()
                .antMatchers("/comment/**").permitAll()
                .antMatchers("/create/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/**").permitAll()
                //swagger
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
