package com.example.membermanagement.member.domain.model;

import com.example.membermanagement.member.domain.exception.InvalidMemberInputException;

public record Nickname(String value) {

    public Nickname {
        if (value == null) {
            throw new InvalidMemberInputException("닉네임은 필수입니다.");
        }
        value = value.trim();
        if (value.length() < 2 || value.length() > 30) {
            throw new InvalidMemberInputException("닉네임은 2자 이상 30자 이하여야 합니다.");
        }
    }
}
