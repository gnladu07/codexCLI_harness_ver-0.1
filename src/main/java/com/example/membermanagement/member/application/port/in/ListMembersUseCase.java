package com.example.membermanagement.member.application.port.in;

import com.example.membermanagement.member.application.result.MemberPageResult;

public interface ListMembersUseCase {

    MemberPageResult list(int page, int size);
}
