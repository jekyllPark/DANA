package com.example.dana.common.security.jwt.domain.entity;

import com.example.dana.common.domain.BaseTimeEntity;
import com.example.dana.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "refresh_token_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", unique = true)
    private Member member;
    @Column(nullable = false)
    private String refreshToken;

    public static RefreshToken createRefreshToken(Member member, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.member = member;
        refreshToken.refreshToken = token;
        return refreshToken;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        this.modifiedTime = LocalDateTime.now();
    }
}
