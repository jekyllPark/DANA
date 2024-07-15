package com.example.dana.common.security.jwt.domain.repository;


import com.example.dana.common.security.jwt.domain.entity.RefreshToken;
import com.example.dana.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
    Optional<RefreshToken> findByRefreshToken(String token);
}
