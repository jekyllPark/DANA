package com.example.dana.member.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public class MemberSuccessMessage {
    public static final String SIGN_UP_SUCCESS = "회원가입에 성공하였습니다.";
    public static final String LOGIN_SUCCESS = "로그인에 성공하였습니다.";
    public static final String LOGOUT_SUCCESS = "로그아웃에 성공하였습니다.";
}
