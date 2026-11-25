package com.example.membermanagement.member.application.port.out;

import com.example.membermanagement.member.application.result.MemberPageResult;

public interface LoadMemberPagePort {

    MemberPageResult loadPage(int page, int size);
}
