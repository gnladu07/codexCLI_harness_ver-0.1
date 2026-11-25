package com.example.membermanagement.member.domain.exception;

public class InvalidMemberInputException extends IllegalArgumentException {

    public InvalidMemberInputException(String message) {
        super(message);
    }
}
