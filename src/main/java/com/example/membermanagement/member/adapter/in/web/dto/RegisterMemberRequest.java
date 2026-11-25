package com.example.membermanagement.member.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

import com.example.membermanagement.member.application.command.RegisterMemberCommand;

public record RegisterMemberRequest(@NotBlank String email, @NotBlank String nickname) {

    public RegisterMemberCommand toCommand() {
        return new RegisterMemberCommand(email, nickname);
    }
}
