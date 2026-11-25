package com.example.membermanagement.member.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.example.membermanagement.member.domain.model.Member;

public interface LoadMemberPort {

    Optional<Member> loadById(UUID memberId);
}
