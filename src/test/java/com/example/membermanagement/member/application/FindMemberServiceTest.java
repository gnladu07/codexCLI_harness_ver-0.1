package com.example.membermanagement.member.application;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.membermanagement.member.application.exception.InvalidRequestException;
import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.application.port.out.LoadMemberPagePort;
import com.example.membermanagement.member.application.port.out.LoadMemberPort;
import com.example.membermanagement.member.application.result.MemberPageResult;
import com.example.membermanagement.member.application.service.FindMemberService;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class FindMemberServiceTest {

    private LoadMemberPort loadMemberPort;
    private LoadMemberPagePort loadMemberPagePort;
    private FindMemberService service;

    @BeforeEach
    void setUp() {
        loadMemberPort = mock(LoadMemberPort.class);
        loadMemberPagePort = mock(LoadMemberPagePort.class);
        service = new FindMemberService(loadMemberPort, loadMemberPagePort);
    }

    @Test
    void getsExistingMemberWithoutChangingState() {
        Member member = member();
        when(loadMemberPort.loadById(member.memberId())).thenReturn(Optional.of(member));

        assertThat(service.get(member.memberId())).isSameAs(member);
        assertThat(member.createdAt()).isEqualTo(member.updatedAt());
    }

    @Test
    void rejectsMissingMember() {
        UUID memberId = UUID.randomUUID();
        when(loadMemberPort.loadById(memberId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(memberId)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void returnsRequestedPage() {
        MemberPageResult page = new MemberPageResult(List.of(member()), 0, 20, 1, 1);
        when(loadMemberPagePort.loadPage(0, 20)).thenReturn(page);

        assertThat(service.list(0, 20)).isSameAs(page);
        verify(loadMemberPagePort).loadPage(0, 20);
    }

    @Test
    void acceptsLargeValidPage() {
        MemberPageResult empty = new MemberPageResult(List.of(), Integer.MAX_VALUE, 20, 1, 1);
        when(loadMemberPagePort.loadPage(Integer.MAX_VALUE, 20)).thenReturn(empty);

        assertThat(service.list(Integer.MAX_VALUE, 20)).isSameAs(empty);
        verify(loadMemberPagePort).loadPage(Integer.MAX_VALUE, 20);
    }

    @Test
    void rejectsInvalidPageAndSizeBeforeQuerying() {
        int[][] invalid = {{-1, 20}, {0, 0}, {0, 101}};
        for (int[] pair : invalid) {
            assertThatThrownBy(() -> service.list(pair[0], pair[1]))
                    .isInstanceOf(InvalidRequestException.class);
        }
        verifyNoInteractions(loadMemberPagePort);
    }

    private Member member() {
        return Member.restore(UUID.randomUUID(), new Email("member@example.com"), new Nickname("회원"),
                Instant.parse("2026-11-25T00:00:00Z"), Instant.parse("2026-11-25T00:00:00Z"));
    }
}
