package com.example.membermanagement.member.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.membermanagement.member.application.exception.InvalidRequestException;
import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.application.port.in.GetMemberUseCase;
import com.example.membermanagement.member.application.port.in.ListMembersUseCase;
import com.example.membermanagement.member.application.port.out.LoadMemberPagePort;
import com.example.membermanagement.member.application.port.out.LoadMemberPort;
import com.example.membermanagement.member.application.result.MemberPageResult;
import com.example.membermanagement.member.domain.model.Member;

@Service
public class FindMemberService implements GetMemberUseCase, ListMembersUseCase {

    private static final int MAX_PAGE_SIZE = 100;

    private final LoadMemberPort loadMemberPort;
    private final LoadMemberPagePort loadMemberPagePort;

    public FindMemberService(LoadMemberPort loadMemberPort, LoadMemberPagePort loadMemberPagePort) {
        this.loadMemberPort = loadMemberPort;
        this.loadMemberPagePort = loadMemberPagePort;
    }

    @Override
    @Transactional(readOnly = true)
    public Member get(UUID memberId) {
        return loadMemberPort.loadById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberPageResult list(int page, int size) {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidRequestException("유효하지 않은 페이지 조건입니다.");
        }
        return loadMemberPagePort.loadPage(page, size);
    }
}
