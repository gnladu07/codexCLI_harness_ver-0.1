package com.example.membermanagement.member.domain.model;

import java.util.Locale;
import java.util.regex.Pattern;

import com.example.membermanagement.member.domain.exception.InvalidMemberInputException;

public record Email(String value) {

    private static final int MAX_LENGTH = 254;
    private static final Pattern FORMAT = Pattern.compile(
            "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                    + "@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\\.[A-Za-z0-9]"
                    + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$");

    public Email {
        if (value == null) {
            throw new InvalidMemberInputException("이메일은 필수입니다.");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
        if (value.isEmpty() || value.length() > MAX_LENGTH || !FORMAT.matcher(value).matches()) {
            throw new InvalidMemberInputException("유효한 이메일을 입력해 주세요.");
        }
    }
}
