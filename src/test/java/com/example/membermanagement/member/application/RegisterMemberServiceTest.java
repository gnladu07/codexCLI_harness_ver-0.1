package com.example.membermanagement.member.application;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.membermanagement.member.application.command.RegisterMemberCommand;
import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.application.port.out.ExistsMemberByEmailPort;
import com.example.membermanagement.member.application.port.out.SaveMemberPort;
import com.example.membermanagement.member.application.service.RegisterMemberService;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterMemberServiceTest {

    private ExistsMemberByEmailPort existsPort;
    private SaveMemberPort savePort;
    private RegisterMemberService service;

    @BeforeEach
    void setUp() {
        existsPort = mock(ExistsMemberByEmailPort.class);
        savePort = mock(SaveMemberPort.class);
        service = new RegisterMemberService(existsPort, savePort,
                Clock.fixed(Instant.parse("2026-11-25T00:00:00Z"), ZoneOffset.UTC));
    }

    @Test
    void registersNormalizedMember() {
        when(savePort.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = service.register(new RegisterMemberCommand(" Member@Example.COM ", " 회원 "));

        verify(existsPort).existsByEmail(new Email("member@example.com"));
        verify(savePort).save(any(Member.class));
        assertThat(member.email().value()).isEqualTo("member@example.com");
        assertThat(member.nickname().value()).isEqualTo("회원");
        assertThat(member.createdAt()).isEqualTo(Instant.parse("2026-11-25T00:00:00Z"));
    }

    @Test
    void rejectsExistingEmailWithoutSaving() {
        when(existsPort.existsByEmail(new Email("member@example.com"))).thenReturn(true);

        assertThatThrownBy(() -> service.register(new RegisterMemberCommand(" MEMBER@example.com ", "회원")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(savePort, never()).save(any());
    }
}
