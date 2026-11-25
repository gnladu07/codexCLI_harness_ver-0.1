package com.example.membermanagement.member.application.port.in;

import com.example.membermanagement.member.application.command.RegisterMemberCommand;
import com.example.membermanagement.member.domain.model.Member;

public interface RegisterMemberUseCase {

    Member register(RegisterMemberCommand command);
}
