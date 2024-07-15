package com.example.dana.common.security.jwt.controller;

import com.example.dana.common.response.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TokenTestApiController {
    @GetMapping()
    public ResponseWrapper test() {
        return ResponseWrapper.SUCCESS("호출 성공", SecurityContextHolder.getContext().getAuthentication());
    }
}