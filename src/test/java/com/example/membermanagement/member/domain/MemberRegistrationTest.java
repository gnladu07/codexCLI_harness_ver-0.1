package com.example.membermanagement.member.domain;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.example.membermanagement.member.domain.exception.InvalidMemberInputException;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRegistrationTest {

    @Test
    void normalizesEmailAndNickname() {
        assertThat(new Email("  Member@Example.COM  ").value()).isEqualTo("member@example.com");
        assertThat(new Nickname("  회원  ").value()).isEqualTo("회원");
    }

    @Test
    void rejectsInvalidEmailAndNickname() {
        assertThatThrownBy(() -> new Email("bad-email")).isInstanceOf(InvalidMemberInputException.class);
        assertThatThrownBy(() -> new Email(" ")).isInstanceOf(InvalidMemberInputException.class);
        assertThatThrownBy(() -> new Email("a".repeat(250) + "@example.com"))
                .isInstanceOf(InvalidMemberInputException.class);
        assertThatThrownBy(() -> new Nickname(" x ")).isInstanceOf(InvalidMemberInputException.class);
        assertThatThrownBy(() -> new Nickname(" ")).isInstanceOf(InvalidMemberInputException.class);
        assertThatThrownBy(() -> new Nickname("x".repeat(31))).isInstanceOf(InvalidMemberInputException.class);
    }

    @Test
    void createsUuidAndSameMicrosecondTimestamps() {
        Member member = Member.register(new Email("member@example.com"), new Nickname("회원"),
                Instant.parse("2026-11-25T00:00:00.123456789Z"));

        assertThat(member.memberId()).isNotNull();
        assertThat(member.createdAt()).isEqualTo(Instant.parse("2026-11-25T00:00:00.123456Z"));
        assertThat(member.updatedAt()).isEqualTo(member.createdAt());
    }
}
