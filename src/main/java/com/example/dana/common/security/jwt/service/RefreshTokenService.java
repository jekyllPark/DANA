package com.example.dana.common.security.jwt.service;

import com.example.dana.common.security.jwt.controller.request.TokenReissueRequest;
import com.example.dana.common.security.jwt.controller.response.AbstractTokenResponse;
import com.example.dana.common.security.jwt.domain.entity.RefreshToken;
import com.example.dana.member.domain.entity.Member;

public interface RefreshTokenService {
    void save(Member member, String token);
    void delete(RefreshToken refreshToken);
    AbstractTokenResponse reissueTokens(TokenReissueRequest request);
    RefreshToken findByMember(Member member);
    RefreshToken findByRefreshToken(String token);
}
