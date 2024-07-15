package com.example.dana.member.service;

import com.example.dana.common.exception.UserHandleException;
import com.example.dana.common.security.jwt.component.TokenProvider;
import com.example.dana.common.security.jwt.controller.response.JwtTokenResponse;
import com.example.dana.common.security.jwt.domain.entity.RefreshToken;
import com.example.dana.common.security.jwt.service.RefreshTokenService;
import com.example.dana.member.controller.request.LoginRequest;
import com.example.dana.member.controller.request.LogoutRequest;
import com.example.dana.member.controller.request.MemberSignUpRequest;
import com.example.dana.member.domain.entity.Member;
import com.example.dana.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dana.common.security.jwt.constants.JwtErrorMessage.INVALID_TOKEN_EXCEPTION;
import static com.example.dana.member.constants.MemberErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    @Override
    public Member signUp(MemberSignUpRequest request) {
        if (isExistsEmail(request.getEmail())) {
            throw new UserHandleException(DUPLICATE_MEMBER_EXCEPTION);
        }
        return memberRepository.save(Member.createUser(request, passwordEncoder));
    }

    @Transactional
    @Override
    public JwtTokenResponse login(LoginRequest request) {
        Member member = findByMemberEmail(request.getEmail());
        validatePassword(request.getPassword(), member);

        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(request.toAuthentication());
        JwtTokenResponse token = tokenProvider.createAccessAndRefreshTokens(authentication);
        refreshTokenService.save(member, token.getRefreshToken());
        return token;
    }

    @Transactional
    @Override
    public void logout(LogoutRequest request) {
        if (!tokenProvider.validateToken(request.getAccessToken())) {
            throw new UserHandleException(INVALID_TOKEN_EXCEPTION);
        }
        Authentication auth = tokenProvider.getAuthentication(request.getAccessToken());

        Member member = findByMemberEmail(auth.getName());
        RefreshToken refreshToken = refreshTokenService.findByMember(member);
        refreshTokenService.delete(refreshToken);
    }

    @Override
    public Member findByMemberEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UserHandleException(NOT_FOUND_MEMBER_EXCEPTION));
    }

    private void validatePassword(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UserHandleException(NOT_CORRECT_MEMBER_PASSWORD_EXCEPTION);
        }
    }

    public boolean isExistsEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
