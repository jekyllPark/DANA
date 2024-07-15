package com.example.dana.member.controller.response;

import com.example.dana.member.constants.Role;
import com.example.dana.member.domain.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberJoinResponse {
    private String username;
    private Role role;
    public static MemberJoinResponse fromMember(Member member) {
        MemberJoinResponse response = new MemberJoinResponse();
        response.username = member.getUsername();
        response.role = member.getRole();
        return response;
    }
}
