package com.example.dana.member.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LogoutRequest {
    @NotEmpty(message = "액세스 토큰 값이 비어있습니다.")
    private String accessToken;

    @NotEmpty(message = "리프레시 토큰 값이 비어있습니다.")
    private String refreshToken;
}
