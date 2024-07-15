package com.example.dana.member.controller;

import com.example.dana.common.response.ResponseWrapper;
import com.example.dana.common.security.jwt.controller.response.JwtTokenResponse;
import com.example.dana.member.controller.request.LoginRequest;
import com.example.dana.member.controller.request.LogoutRequest;
import com.example.dana.member.controller.request.MemberSignUpRequest;
import com.example.dana.member.controller.response.MemberJoinResponse;
import com.example.dana.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.dana.member.constants.MemberSuccessMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping
    public ResponseWrapper<MemberJoinResponse> signUp(@RequestBody @Valid MemberSignUpRequest request) {
        return ResponseWrapper.SUCCESS(SIGN_UP_SUCCESS, MemberJoinResponse.fromMember(memberService.signUp(request)));
    }

    @PostMapping("/login")
    public ResponseWrapper<JwtTokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseWrapper.SUCCESS(LOGIN_SUCCESS, memberService.login(request));
    }

    @PostMapping("/logout")
    public ResponseWrapper<Void> logout(@RequestBody @Valid LogoutRequest request) {
        memberService.logout(request);
        return ResponseWrapper.SUCCESS(LOGOUT_SUCCESS, null);
    }
}
