package com.example.membermanagement.member.adapter.in.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.membermanagement.member.domain.model.Member;

public record MemberResponse(UUID memberId, String email, String nickname, Instant createdAt, Instant updatedAt) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.memberId(), member.email().value(), member.nickname().value(),
                member.createdAt(), member.updatedAt());
    }
}
