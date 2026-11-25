package com.example.membermanagement.member.application.port.out;

import com.example.membermanagement.member.domain.model.Member;

public interface SaveMemberPort {

    Member save(Member member);
}
