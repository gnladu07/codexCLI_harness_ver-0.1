package com.example.membermanagement.member.application.result;

import java.util.List;

import com.example.membermanagement.member.domain.model.Member;

public record MemberPageResult(List<Member> content, int page, int size, long totalElements, long totalPages) {

    public MemberPageResult {
        content = List.copyOf(content);
    }
}
