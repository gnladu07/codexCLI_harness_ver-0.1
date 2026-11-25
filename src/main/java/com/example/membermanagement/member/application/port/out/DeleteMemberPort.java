package com.example.membermanagement.member.application.port.out;

import java.util.UUID;

public interface DeleteMemberPort {

    boolean deleteById(UUID memberId);
}
