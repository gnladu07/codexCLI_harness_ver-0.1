package com.example.membermanagement.member.adapter.out.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.membermanagement.member.adapter.out.persistence.repository.MemberJpaRepository;
import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MemberRegistrationMySqlTest {

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
    private MemberPersistenceAdapter persistenceAdapter;

    @Autowired
    private JdbcClient jdbc;

    @BeforeEach
    void clearMembers() {
        repository.deleteAll();
    }

    @Test
    void registersViaHttpAndPersistsNormalizedValuesAndUtcTimestamps() throws Exception {
        MvcResult result = register("  Member@Example.COM  ", "  회원  ");

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getHeader("Location")).startsWith("/api/v1/members/");
        assertThat(result.getResponse().getContentAsString()).contains("member@example.com");
        assertThat(repository.count()).isEqualTo(1);
        var member = repository.findAll().get(0);
        assertThat(member.getEmail()).isEqualTo("member@example.com");
        assertThat(member.getNickname()).isEqualTo("회원");
        assertThat(member.getCreatedAt()).isEqualTo(member.getUpdatedAt());
        assertThat(member.getCreatedAt().getNano() % 1_000).isZero();
        assertThat(jdbc.sql("SELECT COUNT(*) FROM members WHERE email = 'member@example.com'")
                .query(Integer.class).single()).isEqualTo(1);
    }

    @Test
    void rejectsNormalizedDuplicateAndPreservesExistingRow() throws Exception {
        assertThat(register("member@example.com", "첫회원").getResponse().getStatus()).isEqualTo(201);
        MvcResult duplicate = register(" MEMBER@EXAMPLE.COM ", "둘째회원");

        assertThat(duplicate.getResponse().getStatus()).isEqualTo(409);
        assertThat(duplicate.getResponse().getContentAsString()).contains("DUPLICATE_EMAIL");
        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findAll().get(0).getNickname()).isEqualTo("첫회원");
    }

    @Test
    void rejectsInvalidInputWithoutPersisting() throws Exception {
        assertThat(register("invalid-email", "회원").getResponse().getStatus()).isEqualTo(400);
        assertThat(register("member@example.com", " ").getResponse().getStatus()).isEqualTo(400);
        assertThat(repository.count()).isZero();
    }

    @Test
    void translatesOnlyEmailUniqueViolationFromMySql() {
        persistenceAdapter.save(Member.register(new Email("member@example.com"), new Nickname("첫회원"), Instant.now()));

        assertThatThrownBy(() -> persistenceAdapter.save(Member.register(new Email("member@example.com"),
                new Nickname("둘째회원"), Instant.now())))
                .isInstanceOf(DuplicateEmailException.class);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void concurrentRegistrationsCreateExactlyOneMember() throws Exception {
        int requests = 2;
        CountDownLatch ready = new CountDownLatch(requests);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(requests);
        try {
            List<Future<Integer>> responses = new ArrayList<>();
            for (int index = 0; index < requests; index++) {
                final int memberNumber = index;
                Callable<Integer> registration = () -> {
                    ready.countDown();
                    start.await();
                    return register(" Same@Example.COM ", "회원" + memberNumber).getResponse().getStatus();
                };
                responses.add(executor.submit(registration));
            }
            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            List<Integer> statuses = new ArrayList<>();
            for (Future<Integer> response : responses) {
                statuses.add(response.get(30, TimeUnit.SECONDS));
            }
            assertThat(statuses).containsExactlyInAnyOrder(201, 409);
            assertThat(repository.count()).isEqualTo(1);
        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }

    private MvcResult register(String email, String nickname) throws Exception {
        return mockMvc.perform(post("/api/v1/members").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"nickname\":\"" + nickname + "\"}"))
                .andReturn();
    }
}
