package com.example.dana.common.security.jwt.constants;

public class JwtConstants {
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORITIES_KEY = "auth";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
}
