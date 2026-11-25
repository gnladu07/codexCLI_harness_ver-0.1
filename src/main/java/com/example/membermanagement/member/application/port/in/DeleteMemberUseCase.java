package com.example.membermanagement.member.application.port.in;

import java.util.UUID;

public interface DeleteMemberUseCase {

    void delete(UUID memberId);
}
