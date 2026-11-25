package com.example.membermanagement.member.application.port.in;

import java.util.UUID;

import com.example.membermanagement.member.domain.model.Member;

public interface GetMemberUseCase {

    Member get(UUID memberId);
}
