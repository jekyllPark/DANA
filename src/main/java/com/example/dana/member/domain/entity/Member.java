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
    @Column(nullable = false, unique = true)
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

    private Member(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Member createMember(String email, String username, String password, Role role, PasswordEncoder passwordEncoder) {
        return new Member(email, username, passwordEncoder.encode(password), role);
    }

    public static Member createUser(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request.getEmail(), request.getUsername(), request.getPassword(), USER, passwordEncoder);
    }

    public static Member createSeller(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request.getEmail(), request.getUsername(), request.getPassword(), SELLER, passwordEncoder);
    }

    public static Member createAdmin(MemberSignUpRequest request, PasswordEncoder passwordEncoder) {
        return createMember(request.getEmail(), request.getUsername(), request.getPassword(), ADMIN, passwordEncoder);
    }

}
