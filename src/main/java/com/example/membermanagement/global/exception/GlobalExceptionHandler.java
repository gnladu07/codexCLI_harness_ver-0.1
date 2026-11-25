package com.example.membermanagement.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.application.exception.InvalidRequestException;
import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.domain.exception.InvalidMemberInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidMemberInputException.class, InvalidRequestException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponse> invalidRequest(Exception exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse("INVALID_REQUEST", "유효하지 않은 요청입니다."));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> duplicateEmail(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse("DUPLICATE_EMAIL", exception.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> memberNotFound(MemberNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse("MEMBER_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."));
    }
}
