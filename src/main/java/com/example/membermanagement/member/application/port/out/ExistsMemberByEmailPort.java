package com.example.membermanagement.member.application.port.out;

import com.example.membermanagement.member.domain.model.Email;

public interface ExistsMemberByEmailPort {

    boolean existsByEmail(Email email);
}
