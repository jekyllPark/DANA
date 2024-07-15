package com.example.dana.common.security.jwt.service;

import com.example.dana.common.exception.UserHandleException;
import com.example.dana.common.security.jwt.component.TokenProvider;
import com.example.dana.common.security.jwt.controller.request.TokenReissueRequest;
import com.example.dana.common.security.jwt.controller.response.AbstractTokenResponse;
import com.example.dana.common.security.jwt.controller.response.JwtTokenResponse;
import com.example.dana.common.security.jwt.controller.response.ReissueTokenResponse;
import com.example.dana.common.security.jwt.domain.entity.RefreshToken;
import com.example.dana.common.security.jwt.domain.repository.RefreshTokenRepository;
import com.example.dana.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.dana.common.security.jwt.constants.JwtErrorMessage.*;
import static com.example.dana.common.security.jwt.domain.entity.RefreshToken.createRefreshToken;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    @Override
    public void save(Member member, String token) {
        refreshTokenRepository.save(createRefreshToken(member, token));
    }

    @Transactional
    @Override
    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    @Override
    public AbstractTokenResponse reissueTokens(TokenReissueRequest request) {
        String refreshToken = request.getRefreshToken();
        validateRefreshToken(refreshToken);

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());
        RefreshToken findToken = findByRefreshToken(request.getRefreshToken());

        validateSameRefreshTokens(refreshToken, findToken);

        String reissuedAccessToken = tokenProvider.createAccessToken(authentication);

        if (isRefreshTokenExpired(refreshToken)) {
            log.info("리프레시 토큰이 만료되어 토큰을 새로 발급받아 DB에 업데이트 합니다.");
            String reissuedRefreshToken = tokenProvider.createRefreshToken();
            findToken.updateRefreshToken(reissuedRefreshToken);
            return new JwtTokenResponse(reissuedAccessToken, reissuedRefreshToken);
        }

        return new ReissueTokenResponse(reissuedAccessToken);
    }

    private boolean isRefreshTokenExpired(String refreshToken) {
        return tokenProvider.isTokenExpired(refreshToken);
    }

    private void validateSameRefreshTokens(String refreshToken, RefreshToken findToken) {
        if (!findToken.getRefreshToken().equals(refreshToken)) {
            throw new UserHandleException(NOT_CORRECT_REFRESH_TOKEN);
        }
    }

    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UserHandleException(INVALID_TOKEN_EXCEPTION);
        }
    }

    @Override
    public RefreshToken findByMember(Member member) {
        return refreshTokenRepository.findByMember(member).orElseThrow(() -> new UserHandleException(NOT_FOUND_REFRESH_TOKEN));
    }

    @Override
    public RefreshToken findByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token).orElseThrow(() -> new UserHandleException(NOT_FOUND_REFRESH_TOKEN));
    }
}