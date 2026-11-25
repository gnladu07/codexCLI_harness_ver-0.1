package com.example.membermanagement.member.adapter.out.persistence;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

import com.example.membermanagement.member.adapter.out.persistence.repository.MemberJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberDeletionMySqlTest {

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
    private ObjectMapper objectMapper;

    @Autowired
    private MemberJpaRepository repository;

    @BeforeEach
    void clearMembers() {
        repository.deleteAll();
    }

    @Test
    void deletesOneRegisteredMemberAndPreservesOthers() throws Exception {
        String deletedId = register("deleted@example.com");
        String preservedId = register("preserved@example.com");

        MvcResult deletion = mockMvc.perform(delete("/api/v1/members/{id}", deletedId)).andReturn();

        assertThat(deletion.getResponse().getStatus()).isEqualTo(204);
        assertThat(deletion.getResponse().getContentAsByteArray()).isEmpty();
        assertThat(repository.findById(deletedId)).isEmpty();
        assertThat(repository.findById(preservedId)).isPresent();
        assertThat(repository.count()).isEqualTo(1);
        MvcResult missing = mockMvc.perform(get("/api/v1/members/{id}", deletedId)).andReturn();
        assertThat(missing.getResponse().getStatus()).isEqualTo(404);
        MvcResult preserved = mockMvc.perform(get("/api/v1/members/{id}", preservedId)).andReturn();
        assertThat(preserved.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void missingAndInvalidIdDoNotCreateOrDeleteMembers() throws Exception {
        MvcResult missing = mockMvc.perform(delete("/api/v1/members/{id}",
                "00000000-0000-0000-0000-000000000001")).andReturn();
        assertThat(missing.getResponse().getStatus()).isEqualTo(404);
        assertThat(objectMapper.readTree(missing.getResponse().getContentAsByteArray())
                .get("code").asText()).isEqualTo("MEMBER_NOT_FOUND");

        MvcResult invalid = mockMvc.perform(delete("/api/v1/members/{id}", "invalid-id")).andReturn();
        assertThat(invalid.getResponse().getStatus()).isEqualTo(400);
        assertThat(objectMapper.readTree(invalid.getResponse().getContentAsByteArray())
                .get("code").asText()).isEqualTo("INVALID_REQUEST");
        assertThat(repository.count()).isZero();
    }

    @Test
    void concurrentDeletesReturnOneSuccessAndOneNotFound() throws Exception {
        String id = register("concurrent@example.com");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<Integer>> responses = List.of(
                    executor.submit(() -> deleteAfterStart(id, ready, start)),
                    executor.submit(() -> deleteAfterStart(id, ready, start)));
            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertThat(List.of(responses.get(0).get(30, TimeUnit.SECONDS),
                    responses.get(1).get(30, TimeUnit.SECONDS))).containsExactlyInAnyOrder(204, 404);
            assertThat(repository.count()).isZero();
        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }

    private int deleteAfterStart(String id, CountDownLatch ready, CountDownLatch start) throws Exception {
        ready.countDown();
        start.await();
        return mockMvc.perform(delete("/api/v1/members/{id}", id)).andReturn().getResponse().getStatus();
    }

    private String register(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"nickname\":\"회원\"}"))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        return objectMapper.readTree(result.getResponse().getContentAsByteArray())
                .get("memberId").asText();
    }
}
