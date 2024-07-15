package com.example.dana.member.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberErrorMessage {
    public static final String DUPLICATE_MEMBER_EXCEPTION = "존재하는 회원입니다.";
    public static final String NOT_FOUND_MEMBER_EXCEPTION = "존재하지 않는 회원입니다.";
    public static final String NOT_CORRECT_MEMBER_PASSWORD_EXCEPTION = "비밀번호가 다릅니다.";
}
