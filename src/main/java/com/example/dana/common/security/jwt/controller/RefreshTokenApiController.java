package com.example.dana.common.security.jwt.controller;

import com.example.dana.common.response.ResponseWrapper;
import com.example.dana.common.security.jwt.controller.request.TokenReissueRequest;
import com.example.dana.common.security.jwt.controller.response.AbstractTokenResponse;
import com.example.dana.common.security.jwt.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh-tokens")
public class RefreshTokenApiController {
    private final RefreshTokenService refreshTokenService;
    @PostMapping
    public ResponseWrapper<AbstractTokenResponse> reissueTokens(@RequestBody @Valid TokenReissueRequest request) {
        return ResponseWrapper.SUCCESS(HttpStatus.CREATED.name(), refreshTokenService.reissueTokens(request));
    }
}