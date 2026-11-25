package com.example.membermanagement.member.adapter.in.web;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.application.exception.InvalidRequestException;
import com.example.membermanagement.member.application.exception.MemberNotFoundException;
import com.example.membermanagement.member.application.port.in.DeleteMemberUseCase;
import com.example.membermanagement.member.application.port.in.GetMemberUseCase;
import com.example.membermanagement.member.application.port.in.ListMembersUseCase;
import com.example.membermanagement.member.application.port.in.RegisterMemberUseCase;
import com.example.membermanagement.member.application.result.MemberPageResult;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterMemberUseCase useCase;

    @MockitoBean
    private GetMemberUseCase getMemberUseCase;

    @MockitoBean
    private ListMembersUseCase listMembersUseCase;

    @MockitoBean
    private DeleteMemberUseCase deleteMemberUseCase;

    @Test
    void returnsCreatedMemberAndLocation() throws Exception {
        Member member = Member.restore(UUID.randomUUID(), new Email("member@example.com"), new Nickname("회원"),
                Instant.parse("2026-11-25T00:00:00Z"), Instant.parse("2026-11-25T00:00:00Z"));
        when(useCase.register(any())).thenReturn(member);

        mockMvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"member@example.com\",\"nickname\":\"회원\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/members/" + member.memberId()))
                .andExpect(jsonPath("$.memberId").value(member.memberId().toString()))
                .andExpect(jsonPath("$.email").value("member@example.com"))
                .andExpect(jsonPath("$.nickname").value("회원"))
                .andExpect(jsonPath("$.createdAt").value("2026-11-25T00:00:00Z"))
                .andExpect(jsonPath("$.updatedAt").value("2026-11-25T00:00:00Z"));
    }

    @Test
    void returnsConflictForDuplicateEmail() throws Exception {
        when(useCase.register(any())).thenThrow(new DuplicateEmailException());

        mockMvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"member@example.com\",\"nickname\":\"회원\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"));
    }

    @Test
    void rejectsMissingFieldsUnknownFieldsAndMalformedJson() throws Exception {
        String[] invalidBodies = {
                "{\"email\":\"bad\",\"nickname\":\"\"}",
                "{\"email\":\"member@example.com\",\"nickname\":\"회원\",\"memberId\":\"x\"}",
                "{\"email\":"
        };
        for (String body : invalidBodies) {
            mockMvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
    }

    @Test
    void returnsMemberById() throws Exception {
        Member member = member();
        when(getMemberUseCase.get(member.memberId())).thenReturn(member);

        mockMvc.perform(get("/api/v1/members/{id}", member.memberId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.memberId().toString()))
                .andExpect(jsonPath("$.email").value("member@example.com"));
    }

    @Test
    void returnsNotFoundForMissingMember() throws Exception {
        UUID id = UUID.randomUUID();
        when(getMemberUseCase.get(id)).thenThrow(new MemberNotFoundException());

        mockMvc.perform(get("/api/v1/members/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MEMBER_NOT_FOUND"));
    }

    @Test
    void rejectsMalformedUuid() throws Exception {
        mockMvc.perform(get("/api/v1/members/{id}", "1-1-1-1-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void returnsDefaultMemberPage() throws Exception {
        Member member = member();
        when(listMembersUseCase.list(0, 20))
                .thenReturn(new MemberPageResult(List.of(member), 0, 20, 1, 1));

        mockMvc.perform(get("/api/v1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].memberId").value(member.memberId().toString()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(listMembersUseCase).list(0, 20);
    }

    @Test
    void returnsEmptyPageWithRequestedSize() throws Exception {
        when(listMembersUseCase.list(2, 5))
                .thenReturn(new MemberPageResult(List.of(), 2, 5, 0, 0));

        mockMvc.perform(get("/api/v1/members?page=2&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.size").value(5));
    }

    @Test
    void returnsEmptyPageForLargeValidPageNumber() throws Exception {
        when(listMembersUseCase.list(Integer.MAX_VALUE, 20))
                .thenReturn(new MemberPageResult(List.of(), Integer.MAX_VALUE, 20, 3, 1));

        mockMvc.perform(get("/api/v1/members?page=2147483647&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").value(Integer.MAX_VALUE))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void rejectsMalformedOrDuplicatePageParameters() throws Exception {
        for (String query : List.of("page=abc", "page=2147483648", "size=", "page=0&page=1",
                "size=1&size=2")) {
            mockMvc.perform(get("/api/v1/members?" + query))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        }
    }

    @Test
    void mapsInvalidPagePolicyToBadRequest() throws Exception {
        when(listMembersUseCase.list(-1, 20)).thenThrow(new InvalidRequestException("유효하지 않은 페이지 조건입니다."));

        mockMvc.perform(get("/api/v1/members?page=-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void deletesExistingMemberWithoutResponseBody() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/members/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        verify(deleteMemberUseCase).delete(id);
    }

    @Test
    void returnsNotFoundWhenDeletingMissingMember() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new MemberNotFoundException()).when(deleteMemberUseCase).delete(id);

        mockMvc.perform(delete("/api/v1/members/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("MEMBER_NOT_FOUND"));
    }

    @Test
    void rejectsInvalidUuidBeforeDeleting() throws Exception {
        mockMvc.perform(delete("/api/v1/members/{id}", "1-1-1-1-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
        verifyNoInteractions(deleteMemberUseCase);
    }

    private Member member() {
        return Member.restore(UUID.randomUUID(), new Email("member@example.com"), new Nickname("회원"),
                Instant.parse("2026-11-25T00:00:00Z"), Instant.parse("2026-11-25T00:00:00Z"));
    }
}
