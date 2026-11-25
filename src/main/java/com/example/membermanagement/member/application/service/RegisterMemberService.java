package com.example.membermanagement.member.application.service;

import java.time.Clock;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.membermanagement.member.application.command.RegisterMemberCommand;
import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.application.port.in.RegisterMemberUseCase;
import com.example.membermanagement.member.application.port.out.ExistsMemberByEmailPort;
import com.example.membermanagement.member.application.port.out.SaveMemberPort;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

@Service
public class RegisterMemberService implements RegisterMemberUseCase {

    private final ExistsMemberByEmailPort existsMemberByEmailPort;
    private final SaveMemberPort saveMemberPort;
    private final Clock clock;

    public RegisterMemberService(ExistsMemberByEmailPort existsMemberByEmailPort,
            SaveMemberPort saveMemberPort, Clock clock) {
        this.existsMemberByEmailPort = existsMemberByEmailPort;
        this.saveMemberPort = saveMemberPort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Member register(RegisterMemberCommand command) {
        Objects.requireNonNull(command, "command");
        Email email = new Email(command.email());
        Nickname nickname = new Nickname(command.nickname());
        if (existsMemberByEmailPort.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
        return saveMemberPort.save(Member.register(email, nickname, clock.instant()));
    }
}
