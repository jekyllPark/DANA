package com.example.dana.common.security.config;

import com.example.dana.member.domain.entity.Member;
import com.example.dana.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class SecurityAuditingConfig implements AuditorAware<Long> {
    private final MemberRepository memberRepository;
    private final Map<String, Optional<Long>> memberCache = new ConcurrentHashMap<>();

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String email = authentication.getName();

        Optional<Long> cachedMemberId = memberCache.get(email);
        if (cachedMemberId != null) {
            return cachedMemberId;
        }

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Optional<Long> memberId = optionalMember.map(Member::getId);

        memberCache.put(email, memberId);

        return memberId;
    }
}
