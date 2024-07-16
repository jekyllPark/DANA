package com.example.dana.member.service;

import com.example.dana.common.exception.UserHandleException;
import com.example.dana.common.security.jwt.component.TokenProvider;
import com.example.dana.common.security.jwt.controller.response.JwtTokenResponse;
import com.example.dana.common.security.jwt.domain.entity.RefreshToken;
import com.example.dana.common.security.jwt.service.RefreshTokenService;
import com.example.dana.member.domain.entity.Member;
import com.example.dana.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.dana.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationManager authenticationManager;

    void setAuthentication() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
    }

    @DisplayName("회원가입에 성공한다.")
    @Test
    void 회원가입_성공() {
        // given
        Member member = Member.createUser(MEMBER_SIGN_UP_REQUEST, passwordEncoder);

        // when
        when(memberRepository.existsByEmail(MEMBER_SIGN_UP_REQUEST.getEmail())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        Member result = memberService.signUp(MEMBER_SIGN_UP_REQUEST);

        // then
        assertThat(result).isNotNull();
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @DisplayName("중복된 이메일이 있는 경우 회원가입에 실패한다.")
    @Test
    void 회원가입_실패() {
        // given & when
        when(memberRepository.existsByEmail(MEMBER_SIGN_UP_REQUEST.getEmail())).thenReturn(true);

        // then
        assertThrows(UserHandleException.class, () -> memberService.signUp(MEMBER_SIGN_UP_REQUEST));
    }

    @DisplayName("비밀번호가 다를 시 로그인에 실패한다.")
    @Test
    void 로그인_실패() {
        // given
        Member member = Member.createUser(MEMBER_SIGN_UP_REQUEST, passwordEncoder);

        // when
        when(memberRepository.findByEmail(MEMBER_LOGIN_REQUEST.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(MEMBER_LOGIN_REQUEST.getPassword(), member.getPassword())).thenReturn(false);

        // then
        assertThrows(UserHandleException.class, () -> memberService.login(MEMBER_LOGIN_REQUEST));
    }

    @DisplayName("로그인 시 토큰을 반환 및 DB에 저장한다.")
    @Test
    void 로그인_성공() {
        // given
        Member member = Member.createUser(MEMBER_SIGN_UP_REQUEST, passwordEncoder);
        setAuthentication();

        // when
        when(memberRepository.findByEmail(MEMBER_LOGIN_REQUEST.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(MEMBER_LOGIN_REQUEST.getPassword(), member.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessAndRefreshTokens(authentication)).thenReturn(VALID_JWT_TOKEN_RESPONSE);
        JwtTokenResponse result = memberService.login(MEMBER_LOGIN_REQUEST);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        verify(refreshTokenService, times(1)).save(member, result.getRefreshToken());
    }

    @DisplayName("로그아웃 중 토큰이 비정상일 경우 예외가 발생한다.")
    @Test
    void 로그아웃_실패() {
        // given & when
        when(tokenProvider.validateToken(INVALID_LOGOUT_REQUEST.getAccessToken())).thenReturn(false);

        // then
        assertThrows(UserHandleException.class, () -> memberService.logout(INVALID_LOGOUT_REQUEST));
    }

    @DisplayName("로그아웃에 성공하면 리프레시 토큰을 삭제한다.")
    @Test
    void 로그아웃_성공() {
        // given
        Member member = Member.createUser(MEMBER_SIGN_UP_REQUEST, passwordEncoder);
        RefreshToken refreshToken = RefreshToken.createRefreshToken(member, null);

        // when
        when(tokenProvider.validateToken(VALID_LOGOUT_REQUEST.getAccessToken())).thenReturn(true);
        when(tokenProvider.getAuthentication(VALID_LOGOUT_REQUEST.getAccessToken())).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(refreshTokenService.findByMember(member)).thenReturn(refreshToken);
        memberService.logout(VALID_LOGOUT_REQUEST);

        // then
        verify(refreshTokenService, times(1)).delete(refreshToken);
    }
}
