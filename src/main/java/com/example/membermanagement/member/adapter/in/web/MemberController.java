package com.example.membermanagement.member.adapter.in.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membermanagement.member.adapter.in.web.dto.MemberResponse;
import com.example.membermanagement.member.adapter.in.web.dto.MemberPageResponse;
import com.example.membermanagement.member.adapter.in.web.dto.RegisterMemberRequest;
import com.example.membermanagement.member.application.exception.InvalidRequestException;
import com.example.membermanagement.member.application.port.in.DeleteMemberUseCase;
import com.example.membermanagement.member.application.port.in.GetMemberUseCase;
import com.example.membermanagement.member.application.port.in.ListMembersUseCase;
import com.example.membermanagement.member.application.port.in.RegisterMemberUseCase;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private static final Pattern UUID_FORMAT = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final RegisterMemberUseCase registerMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final ListMembersUseCase listMembersUseCase;
    private final DeleteMemberUseCase deleteMemberUseCase;

    public MemberController(RegisterMemberUseCase registerMemberUseCase,
            GetMemberUseCase getMemberUseCase, ListMembersUseCase listMembersUseCase,
            DeleteMemberUseCase deleteMemberUseCase) {
        this.registerMemberUseCase = registerMemberUseCase;
        this.getMemberUseCase = getMemberUseCase;
        this.listMembersUseCase = listMembersUseCase;
        this.deleteMemberUseCase = deleteMemberUseCase;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterMemberRequest request) {
        MemberResponse response = MemberResponse.from(registerMemberUseCase.register(request.toCommand()));
        return ResponseEntity.created(URI.create("/api/v1/members/" + response.memberId())).body(response);
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable String id) {
        return MemberResponse.from(getMemberUseCase.get(parseMemberId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deleteMemberUseCase.delete(parseMemberId(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public MemberPageResponse list(@RequestParam MultiValueMap<String, String> parameters) {
        int page = parseParameter(parameters.get("page"), 0);
        int size = parseParameter(parameters.get("size"), 20);
        return MemberPageResponse.from(listMembersUseCase.list(page, size));
    }

    private UUID parseMemberId(String id) {
        if (!UUID_FORMAT.matcher(id).matches()) {
            throw new InvalidRequestException("유효하지 않은 회원 식별자입니다.");
        }
        return UUID.fromString(id);
    }

    private int parseParameter(List<String> values, int defaultValue) {
        if (values == null) {
            return defaultValue;
        }
        if (values.size() != 1) {
            throw new InvalidRequestException("페이지 조건은 하나의 값이어야 합니다.");
        }
        try {
            return Integer.parseInt(values.get(0));
        } catch (NumberFormatException exception) {
            throw new InvalidRequestException("유효하지 않은 페이지 조건입니다.");
        }
    }
}
