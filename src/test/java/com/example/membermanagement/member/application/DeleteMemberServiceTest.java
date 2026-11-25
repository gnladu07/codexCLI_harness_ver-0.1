package com.example.membermanagement.member.application;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.application.port.out.DeleteMemberPort;
import com.example.membermanagement.member.application.service.DeleteMemberService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteMemberServiceTest {

    private final DeleteMemberPort deleteMemberPort = mock(DeleteMemberPort.class);
    private final DeleteMemberService service = new DeleteMemberService(deleteMemberPort);

    @Test
    void deletesExistingMember() {
        UUID memberId = UUID.randomUUID();
        when(deleteMemberPort.deleteById(memberId)).thenReturn(true);

        service.delete(memberId);

        verify(deleteMemberPort).deleteById(memberId);
    }

    @Test
    void rejectsMissingMember() {
        UUID memberId = UUID.randomUUID();
        when(deleteMemberPort.deleteById(memberId)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(memberId)).isInstanceOf(MemberNotFoundException.class);
        verify(deleteMemberPort).deleteById(memberId);
    }
}
