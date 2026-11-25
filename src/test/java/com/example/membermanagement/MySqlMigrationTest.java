package com.example.membermanagement;

import java.util.List;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MySqlMigrationTest {

    @Container
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("member_management");

    private final Flyway flyway;
    private final JdbcClient jdbc;

    @Autowired
    MySqlMigrationTest(Flyway flyway, JdbcClient jdbc) {
        this.flyway = flyway;
        this.jdbc = jdbc;
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @Test
    @DisplayName("MySQL에서 Flyway V1이 성공하고 회원 테이블 컬럼이 생성된다")
    void migratesTheMembersTable() {
        assertThat(flyway.info().applied()).anySatisfy(migration -> {
            assertThat(migration.getVersion().getVersion()).isEqualTo("1");
            assertThat(migration.getState()).isEqualTo(MigrationState.SUCCESS);
        });

        List<Column> columns = jdbc.sql("""
                SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLLATION_NAME
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'members'
                ORDER BY ORDINAL_POSITION
                """)
                .query((row, index) -> new Column(row.getString("COLUMN_NAME"), row.getString("COLUMN_TYPE"),
                        row.getString("IS_NULLABLE"), row.getString("COLLATION_NAME")))
                .list();

        assertThat(columns)
                .extracting(Column::name, Column::type, Column::nullable)
                .containsExactly(
                        tuple("member_id", "char(36)", "NO"),
                        tuple("email", "varchar(254)", "NO"),
                        tuple("nickname", "varchar(30)", "NO"),
                        tuple("created_at", "datetime(6)", "NO"),
                        tuple("updated_at", "datetime(6)", "NO"));
        assertThat(columns)
                .filteredOn(column -> column.name().equals("email"))
                .extracting(Column::collation)
                .containsExactly("utf8mb4_bin");
    }

    @Test
    @DisplayName("회원 테이블에 기본 키, 이메일 UNIQUE, 목록 정렬 인덱스가 존재한다")
    void createsTheRequiredIndexes() {
        List<IndexColumn> indexColumns = jdbc.sql("""
                SELECT INDEX_NAME, COLUMN_NAME, COLLATION, NON_UNIQUE
                FROM information_schema.STATISTICS
                WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'members'
                ORDER BY INDEX_NAME, SEQ_IN_INDEX
                """)
                .query((row, index) -> new IndexColumn(row.getString("INDEX_NAME"), row.getString("COLUMN_NAME"),
                        row.getString("COLLATION"), row.getBoolean("NON_UNIQUE")))
                .list();

        assertThat(indexColumns).containsExactly(
                new IndexColumn("idx_members_created_at_member_id", "created_at", "D", true),
                new IndexColumn("idx_members_created_at_member_id", "member_id", "A", true),
                new IndexColumn("PRIMARY", "member_id", "A", false),
                new IndexColumn("uk_members_email", "email", "A", false));
    }

    private record Column(String name, String type, String nullable, String collation) {
    }

    private record IndexColumn(String name, String column, String direction, boolean nonUnique) {
    }
}
