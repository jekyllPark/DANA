package com.example.dana.member.service;

import com.example.dana.common.exception.UserHandleException;
import com.example.dana.member.domain.entity.Member;
import com.example.dana.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.dana.member.constants.MemberErrorMessage.NOT_FOUND_MEMBER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UserHandleException(NOT_FOUND_MEMBER_EXCEPTION));
    }

    private UserDetails createUserDetails(Member member) {
        return new User(member.getEmail(), member.getPassword(), member.getAuthorities());
    }
}