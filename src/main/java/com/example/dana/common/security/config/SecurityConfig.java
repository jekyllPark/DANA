package com.example.dana.common.security.config;

import com.example.dana.common.security.jwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.dana.member.constants.Role.USER;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 시큐리티 체이닝 설정
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /** Cross Site Request Forgery 차단 */
                .csrf(AbstractHttpConfigurer::disable)
                /** Jwt 사용을 위해 httpBasic 차단 */
                .httpBasic(HttpBasicConfigurer::disable)
                /** 폼 로그인 차단 */
                .formLogin(AbstractHttpConfigurer::disable)

                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                /** UsernamePasswordAuthenticationFilter 의 선행 처리 */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                /** API 엔드포인트에 대한 권한 설정 */
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/", "/error", "/api/member/**", "/api/refresh-tokens/**").permitAll()
                        .requestMatchers("/api/test").hasAuthority(USER.getAuthority())
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
