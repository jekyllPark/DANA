package com.example.dana.member.constants;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    SELLER,
    ADMIN
    ;
    @Override
    public String getAuthority() {
        return name();
    }
}
