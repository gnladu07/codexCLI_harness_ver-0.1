package com.example.membermanagement.member.adapter.in.web.dto;

import java.util.List;

import com.example.membermanagement.member.application.result.MemberPageResult;

public record MemberPageResponse(List<MemberResponse> content, int page, int size,
        long totalElements, long totalPages) {

    public static MemberPageResponse from(MemberPageResult result) {
        return new MemberPageResponse(result.content().stream().map(MemberResponse::from).toList(),
                result.page(), result.size(), result.totalElements(), result.totalPages());
    }
}
