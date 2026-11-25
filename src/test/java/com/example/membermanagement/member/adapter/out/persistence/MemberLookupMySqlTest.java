package com.example.membermanagement.member.adapter.out.persistence;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.membermanagement.member.adapter.out.persistence.entity.MemberJpaEntity;
import com.example.membermanagement.member.adapter.out.persistence.repository.MemberJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberLookupMySqlTest {

    private static final String FIRST_ID = "00000000-0000-0000-0000-000000000001";
    private static final String SECOND_ID = "00000000-0000-0000-0000-000000000002";
    private static final String OLDER_ID = "00000000-0000-0000-0000-000000000003";

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("member_management");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberJpaRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearMembers() {
        repository.deleteAll();
    }

    @Test
    void getsRegisteredMemberWithoutChangingIt() throws Exception {
        MvcResult registered = mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"member@example.com\",\"nickname\":\"회원\"}"))
                .andReturn();
        assertThat(registered.getResponse().getStatus()).isEqualTo(201);
        JsonNode created = body(registered);
        String id = created.get("memberId").asText();
        MemberJpaEntity before = repository.findById(id).orElseThrow();

        MvcResult found = mockMvc.perform(get("/api/v1/members/{id}", id)).andReturn();

        assertThat(found.getResponse().getStatus()).isEqualTo(200);
        assertThat(body(found)).isEqualTo(created);
        MemberJpaEntity after = repository.findById(id).orElseThrow();
        assertThat(after.getCreatedAt()).isEqualTo(before.getCreatedAt());
        assertThat(after.getUpdatedAt()).isEqualTo(before.getUpdatedAt());
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void returnsNotFoundAndRejectsInvalidUuid() throws Exception {
        MvcResult missing = mockMvc.perform(get("/api/v1/members/{id}", FIRST_ID)).andReturn();
        assertThat(missing.getResponse().getStatus()).isEqualTo(404);
        assertThat(body(missing).get("code").asText()).isEqualTo("MEMBER_NOT_FOUND");

        MvcResult invalid = mockMvc.perform(get("/api/v1/members/{id}", "1-1-1-1-1")).andReturn();
        assertThat(invalid.getResponse().getStatus()).isEqualTo(400);
        assertThat(body(invalid).get("code").asText()).isEqualTo("INVALID_REQUEST");
    }

    @Test
    void pagesMembersByCreatedAtDescendingThenIdAscending() throws Exception {
        LocalDateTime older = LocalDateTime.parse("2026-11-25T00:00:00");
        LocalDateTime newer = older.plusSeconds(1);
        repository.saveAllAndFlush(java.util.List.of(
                entity(OLDER_ID, "older@example.com", older),
                entity(SECOND_ID, "second@example.com", newer),
                entity(FIRST_ID, "first@example.com", newer)));

        MvcResult firstPage = mockMvc.perform(get("/api/v1/members?page=0&size=2")).andReturn();
        assertThat(firstPage.getResponse().getStatus()).isEqualTo(200);
        JsonNode first = body(firstPage);
        assertThat(first.get("content").size()).isEqualTo(2);
        assertThat(first.get("content").get(0).get("memberId").asText()).isEqualTo(FIRST_ID);
        assertThat(first.get("content").get(1).get("memberId").asText()).isEqualTo(SECOND_ID);
        assertThat(first.get("page").asInt()).isZero();
        assertThat(first.get("size").asInt()).isEqualTo(2);
        assertThat(first.get("totalElements").asLong()).isEqualTo(3);
        assertThat(first.get("totalPages").asInt()).isEqualTo(2);

        MvcResult secondPage = mockMvc.perform(get("/api/v1/members?page=1&size=2")).andReturn();
        JsonNode second = body(secondPage);
        assertThat(second.get("content").size()).isEqualTo(1);
        assertThat(second.get("content").get(0).get("memberId").asText()).isEqualTo(OLDER_ID);

        MvcResult largePage = mockMvc.perform(get("/api/v1/members?page=2147483647&size=2")).andReturn();
        assertThat(largePage.getResponse().getStatus()).isEqualTo(200);
        JsonNode large = body(largePage);
        assertThat(large.get("content").isEmpty()).isTrue();
        assertThat(large.get("page").asInt()).isEqualTo(Integer.MAX_VALUE);
        assertThat(large.get("size").asInt()).isEqualTo(2);
        assertThat(large.get("totalElements").asLong()).isEqualTo(3);
        assertThat(large.get("totalPages").asLong()).isEqualTo(2);
        assertThat(repository.count()).isEqualTo(3);
    }

    @Test
    void returnsEmptyPageAndRejectsInvalidRangeWithoutChangingData() throws Exception {
        MvcResult empty = mockMvc.perform(get("/api/v1/members")).andReturn();
        assertThat(empty.getResponse().getStatus()).isEqualTo(200);
        assertThat(body(empty).get("content").isEmpty()).isTrue();
        assertThat(body(empty).get("page").asInt()).isZero();
        assertThat(body(empty).get("size").asInt()).isEqualTo(20);
        assertThat(body(empty).get("totalElements").asLong()).isZero();
        assertThat(body(empty).get("totalPages").asInt()).isZero();

        for (String query : java.util.List.of("page=-1", "size=0", "size=101")) {
            MvcResult invalid = mockMvc.perform(get("/api/v1/members?" + query)).andReturn();
            assertThat(invalid.getResponse().getStatus()).isEqualTo(400);
            assertThat(body(invalid).get("code").asText()).isEqualTo("INVALID_REQUEST");
        }
        assertThat(repository.count()).isZero();
    }

    private MemberJpaEntity entity(String id, String email, LocalDateTime createdAt) {
        return new MemberJpaEntity(id, email, "회원", createdAt, createdAt);
    }

    private JsonNode body(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsByteArray());
    }
}
