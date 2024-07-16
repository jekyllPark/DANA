package com.example.dana.member.fixture;

import com.example.dana.common.security.jwt.controller.response.JwtTokenResponse;
import com.example.dana.member.controller.request.LoginRequest;
import com.example.dana.member.controller.request.LogoutRequest;
import com.example.dana.member.controller.request.MemberSignUpRequest;

public class MemberFixture {
    public static MemberSignUpRequest MEMBER_SIGN_UP_REQUEST = new MemberSignUpRequest("test@example.com", null, null);
    public static LoginRequest MEMBER_LOGIN_REQUEST = new LoginRequest(null, null);
    public static LogoutRequest INVALID_LOGOUT_REQUEST = new LogoutRequest(null, null);
    public static LogoutRequest VALID_LOGOUT_REQUEST = new LogoutRequest(null, null);
    public static JwtTokenResponse VALID_JWT_TOKEN_RESPONSE = new JwtTokenResponse("accessToken", "refreshToken");
}
