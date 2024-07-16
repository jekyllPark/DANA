package com.example.dana.member.domain.entity;

import com.example.dana.member.constants.Role;
import com.example.dana.member.controller.request.MemberSignUpRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

import static com.example.dana.member.constants.Role.*;

@Getter @ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    private static Member createMember(MemberSignUpRequest request, PasswordEncoder passwordEncoder, Role role) {
        Member member = new Member();
        member.email = request.getEmail();
        member.username = request.getUsername();
        member.password = passwordEncoder.encode(request.getPassword());
        member.role = role;
        return member;
    }

    public static Member createUser(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request, passwordEncoder, USER);
    }

    public static Member createSeller(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request, passwordEncoder, SELLER);
    }

    public static Member createAdmin(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request, passwordEncoder, ADMIN);
    }

}
