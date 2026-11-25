package com.example.membermanagement.member.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.application.port.in.DeleteMemberUseCase;
import com.example.membermanagement.member.application.port.out.DeleteMemberPort;

@Service
public class DeleteMemberService implements DeleteMemberUseCase {

    private final DeleteMemberPort deleteMemberPort;

    public DeleteMemberService(DeleteMemberPort deleteMemberPort) {
        this.deleteMemberPort = deleteMemberPort;
    }

    @Override
    @Transactional
    public void delete(UUID memberId) {
        if (!deleteMemberPort.deleteById(memberId)) {
            throw new MemberNotFoundException();
        }
    }
}
