
# Project Context - Member Management

## 1. Context 기본 정보

- Document Version: 1.0.0
- Project Name: Member Management
- Project Status: PLANNING
- Project Type: Harness Validation Project

본 문서는 Codex CLI 기반 멀티 에이전트
하네스의 실제 개발 능력을 검증하기 위한
회원 관리 프로젝트의 요구사항을 정의한다.

본 문서의 명시적인 프로젝트 정책은
현재 테스트 프로젝트의 목표 기준으로 사용한다.

실제 구현 상태는 소스 코드와 설정을 확인한다.

AGENTS.md 및 harness/rules.md의
필수 개발 규칙을 준수한다.

문서에 정의되지 않은 주요 비즈니스 정책은
임의로 추가하지 않는다.


## 2. 프로젝트 목적

본 프로젝트의 목적은 다음과 같다.

- 실제 Custom Subagent 호출 검증
- Planner의 요구사항 분석 및 설계 검증
- Developer의 기능 구현 검증
- Reviewer의 독립 코드 리뷰 검증
- 에이전트 간 산출물 전달 검증
- Hexagonal Architecture 준수 검증
- SOLID 원칙 준수 검증
- 테스트 기반 개발 검증
- 수정 및 재검증 루프 검증
- 독립 작업의 병렬 실행 검증
- 최종 Quality Gate 검증

서비스 기능은 간단한 회원 관리로 제한한다.

운영 서비스 배포는 수행하지 않는다.


## 3. 개발 범위

### 3.1. In Scope

- Spring Boot 프로젝트 초기화
- 회원 등록
- 회원 단건 조회
- 회원 목록 조회
- 회원 닉네임 수정
- 회원 삭제
- 입력값 검증
- 이메일 중복 방지
- 공통 예외 처리
- MySQL 데이터베이스 연동
- DB 마이그레이션
- 단위 테스트
- Web Adapter 테스트
- MySQL 통합 테스트
- ArchUnit 아키텍처 테스트
- Gradle 빌드 검증

### 3.2. Out of Scope

- 로그인
- 비밀번호 관리
- JWT 인증
- Spring Security 인증 및 인가
- 관리자 권한 관리
- OAuth
- 회원 등급
- 결제
- Redis
- Kafka
- MSA
- 프론트엔드
- 운영 서버 배포
- Kubernetes

Out of Scope 기능을 임의로 구현하지 않는다.


## 4. 기술 스택

| 항목 | 기술 | 버전 |
|---|---|---|
| Language | Java | 17 |
| Framework | Spring Boot | 3.5.16 |
| Build Tool | Gradle Wrapper | 8.14.3 |
| Build DSL | Groovy | - |
| Packaging | Jar | - |
| Web | Spring MVC | Boot 관리 버전 |
| ORM | Spring Data JPA | Boot 관리 버전 |
| Validation | Jakarta Bean Validation | Boot 관리 버전 |
| Database | MySQL | 8.0 |
| Migration | Flyway | Boot 호환 버전 |
| Unit Test | JUnit Jupiter | Boot 관리 버전 |
| Mocking | Mockito | Boot 관리 버전 |
| Web Test | MockMvc | Boot 관리 버전 |
| Integration Test | Testcontainers MySQL | 호환 버전 |
| Architecture Test | ArchUnit | 호환 안정 버전 |
| Version Control | Git | - |

Spring Boot의 의존성 관리 기능을 우선 사용한다.

ArchUnit 등 별도로 버전 지정이 필요한 도구는
Java 17 호환 안정 버전을 선정하고 기록한다.

지정 버전을 사용할 수 없는 경우
임의로 변경하지 않고 원인을 보고한다.


## 5. 프로젝트 초기 설정

- Group: com.example
- Artifact: member-management
- Name: member-management
- Version: 0.0.1-SNAPSHOT
- Base Package: com.example.membermanagement
- Main Class: MemberManagementApplication
- Java Toolchain: 17
- Application Port: 8080

프로젝트 초기 설정은 기존 하네스 파일이 있는
현재 저장소 루트를 기준으로 수행한다.

불필요하게 중첩 프로젝트를 생성하지 않는다.

기존 하네스 파일을 삭제하거나 덮어쓰지 않는다.


## 6. 시스템 아키텍처

- System Architecture: Monolith
- Internal Architecture: Hexagonal Architecture
- Design Principles: SOLID, KISS, DRY, YAGNI

Domain 계층은 순수 Java로 구현한다.

Application Service에서 Spring의
@Service 및 @Transactional 사용을 허용한다.

Domain Model과 JPA Entity는 분리한다.

Controller는 Input Port를 호출한다.

Application Service는 Output Port에 의존한다.

Persistence Adapter는 Output Port를 구현한다.


## 7. 패키지 구조

Base Package:

com.example.membermanagement

기본 구조:

    com.example.membermanagement
    |
    |-- MemberManagementApplication.java
    |
    |-- member
    |   |-- domain
    |   |   |-- model
    |   |   |-- exception
    |   |
    |   |-- application
    |   |   |-- port
    |   |   |   |-- in
    |   |   |   |-- out
    |   |   |-- service
    |   |   |-- command
    |   |   |-- query
    |   |   |-- result
    |   |
    |   |-- adapter
    |       |-- in
    |       |   |-- web
    |       |       |-- dto
    |       |
    |       |-- out
    |           |-- persistence
    |               |-- entity
    |               |-- repository
    |               |-- mapper
    |
    |-- global
        |-- exception
        |-- config

현재 기능에 필요하지 않은 빈 패키지는
생성하지 않아도 된다.

구체적인 클래스 구성은 Planner가 설계한다.


## 8. 사용자 및 보안 범위

본 프로젝트는 로컬 전용 테스트 시스템이다.

별도의 사용자 인증은 구현하지 않는다.

실제 개인정보를 사용하지 않는다.

애플리케이션은 기본적으로
127.0.0.1에서만 접근 가능하도록 구성한다.

외부 네트워크에 공개하거나
운영 환경에 배포하지 않는다.

보안 기능 제외는 본 테스트 프로젝트에만 적용한다.


## 9. 도메인 정의

### DOM-001: Member

책임:

- 회원 정보 생성
- 회원 정보 관리
- 닉네임 변경
- 회원 정보의 유효성 유지

주요 속성:

- Member ID
- Email
- Nickname
- Created At
- Updated At

회원 식별자와 이메일은 변경할 수 없다.

닉네임만 수정할 수 있다.

필요한 Value Object의 구성은
Planner가 실제 설계 과정에서 결정한다.


## 10. 비즈니스 정책

### POL-001: 회원 식별자

- UUID를 사용한다.
- 데이터베이스에는 CHAR(36)으로 저장한다.
- 외부 API에는 UUID 문자열을 사용한다.
- 식별자는 애플리케이션에서 생성한다.


### POL-002: 이메일

- 이메일은 필수값이다.
- 유효한 이메일 형식이어야 한다.
- 최대 길이는 254자이다.
- 입력값의 앞뒤 공백은 제거한다.
- Locale.ROOT를 기준으로 소문자 정규화한다.
- 정규화된 이메일은 중복 등록할 수 없다.
- 데이터베이스 UNIQUE 제약조건으로
  최종 중복 등록을 방지한다.
- 이메일 변경은 허용하지 않는다.

소문자 정규화는 이번 테스트 프로젝트에서
선택한 계정 식별 정책이다.


### POL-003: 닉네임

- 닉네임은 필수값이다.
- 입력값의 앞뒤 공백은 제거한다.
- 정규화된 닉네임 길이는 2자 이상
  30자 이하여야 한다.
- 공백만 입력한 값은 허용하지 않는다.
- 닉네임 중복은 허용한다.


### POL-004: 회원 조회

- 존재하지 않는 회원은 HTTP 404를 반환한다.
- 유효하지 않은 UUID 형식은 HTTP 400을 반환한다.
- 조회 요청은 회원 상태를 변경하지 않는다.


### POL-005: 회원 수정

- 닉네임만 수정할 수 있다.
- 회원 식별자와 이메일은 유지한다.
- 생성일은 변경하지 않는다.
- 수정이 발생하면 수정일을 갱신한다.
- 동일 닉네임 요청은 변경 없는 성공으로 처리한다.
- 동일 닉네임 요청에서는 수정일을 유지한다.


### POL-006: 회원 삭제

- Hard Delete를 사용한다.
- 존재하는 회원만 삭제할 수 있다.
- 정상 삭제 시 HTTP 204를 반환한다.
- 존재하지 않는 회원 삭제 시 HTTP 404를 반환한다.
- 삭제된 회원은 조회할 수 없다.


### POL-007: 페이지네이션

- 페이지 번호는 0부터 시작한다.
- 기본 페이지 크기는 20이다.
- 최대 페이지 크기는 100이다.
- page는 0 이상이어야 한다.
- size는 1 이상 100 이하여야 한다.
- 잘못된 페이지 조건은 HTTP 400을 반환한다.
- 존재하는 데이터가 없으면 빈 목록을 반환한다.
- createdAt DESC, memberId ASC 순으로 정렬한다.


### POL-008: 날짜 및 시간

- 시간 데이터는 UTC 기준으로 관리한다.
- API 응답은 ISO-8601 UTC 형식으로 제공한다.
- DB에는 DATETIME(6)으로 저장한다.
- createdAt은 최초 등록 시 설정한다.
- updatedAt은 최초 등록 시 createdAt과 동일하다.
- 실제 상태 변경 시 updatedAt을 갱신한다.
- DB 정밀도와 Java 시간 타입 간 차이를 고려한다.


### POL-009: 입력 필드 제한

- 요청 DTO에 정의되지 않은 필드를 허용하지 않는다.
- 회원 수정 요청에 email 또는 memberId가
  포함된 경우 HTTP 400을 반환한다.
- 잘못된 JSON 요청은 HTTP 400을 반환한다.


## 11. 기능 요구사항

| Requirement ID | 기능 | 우선순위 |
|---|---|---|
| FR-001 | 회원 등록 | MUST |
| FR-002 | 회원 단건 조회 | MUST |
| FR-003 | 회원 목록 조회 | MUST |
| FR-004 | 회원 닉네임 수정 | MUST |
| FR-005 | 회원 삭제 | MUST |
| FR-006 | 입력값 검증 | MUST |
| FR-007 | 공통 예외 응답 | MUST |

위 요구사항 외의 기능을 추가하지 않는다.


## 12. 데이터베이스

### 12.1. 기본 정보

- DBMS: MySQL 8.0
- Local Database: member_management
- Local Host: 127.0.0.1
- Local Port: 3307
- Character Set: utf8mb4
- Email Comparison: utf8mb4_bin
- Migration Tool: Flyway
- Hibernate ddl-auto: validate

DB 접속 정보는 환경 변수로 관리한다.

운영 데이터베이스 및 공유 데이터베이스는
사용하지 않는다.


### 12.2. 테이블 정의

Table ID: TBL-001

Table Name: members

| Column | Type | Nullable | Constraint |
|---|---|---|---|
| member_id | CHAR(36) | NO | PRIMARY KEY |
| email | VARCHAR(254) | NO | UNIQUE |
| nickname | VARCHAR(30) | NO | - |
| created_at | DATETIME(6) | NO | - |
| updated_at | DATETIME(6) | NO | - |

추가 인덱스:

- idx_members_created_at_member_id

회원 목록의 기본 정렬 조건에 맞춰 설계한다.


### 12.3. DDL

    CREATE TABLE members (
        member_id CHAR(36) NOT NULL,
        email VARCHAR(254)
            COLLATE utf8mb4_bin NOT NULL,
        nickname VARCHAR(30) NOT NULL,
        created_at DATETIME(6) NOT NULL,
        updated_at DATETIME(6) NOT NULL,

        PRIMARY KEY (member_id),

        CONSTRAINT uk_members_email
            UNIQUE (email),

        INDEX idx_members_created_at_member_id (
            created_at DESC,
            member_id ASC
        )
    ) ENGINE=InnoDB
      DEFAULT CHARSET=utf8mb4;

위 DDL을 Flyway 초기 마이그레이션에 반영한다.

마이그레이션 파일:

src/main/resources/db/migration/V1__create_members.sql

이미 적용된 마이그레이션을 임의로 수정하지 않는다.


### 12.4. 영속성 정책

- Domain Model과 JPA Entity를 분리한다.
- JPA Entity를 API 응답으로 반환하지 않는다.
- Application Service가 JPA Repository에
  직접 의존하지 않는다.
- Persistence Adapter가 Output Port를 구현한다.
- DB UNIQUE 제약조건 위반을 적절한
  Application 오류로 변환한다.

모든 DB 오류를 이메일 중복 오류로
변환하지 않는다.

동시 회원 등록에서도 중복 데이터가
생성되지 않도록 검증한다.


## 13. API 명세

### 13.1. 공통 정책

- Base Path: /api/v1
- Data Format: JSON
- 인증: NOT_APPLICABLE
- 성공 응답: 리소스 또는 목록 DTO
- 실패 응답: 공통 오류 DTO
- 공통 응답 Wrapper: 사용하지 않음

JPA Entity 및 Spring Data Page 객체를
외부 API에 직접 반환하지 않는다.


### 13.2. API 목록

| API ID | Method | URI | Requirement |
|---|---|---|---|
| API-001 | POST | /api/v1/members | FR-001 |
| API-002 | GET | /api/v1/members/{id} | FR-002 |
| API-003 | GET | /api/v1/members | FR-003 |
| API-004 | PATCH | /api/v1/members/{id} | FR-004 |
| API-005 | DELETE | /api/v1/members/{id} | FR-005 |


### 13.3. API-001: 회원 등록

POST /api/v1/members

Request Body:

    {
        "email": "member@example.com",
        "nickname": "테스트회원"
    }

Success Status:

- 201 Created

Success Header:

- Location: /api/v1/members/{memberId}

Response:

- MemberResponse

Failure Status:

- 400: 잘못된 입력
- 409: 이메일 중복


### 13.4. API-002: 회원 단건 조회

GET /api/v1/members/{id}

Success Status:

- 200 OK

Response:

- MemberResponse

Failure Status:

- 400: 잘못된 UUID 형식
- 404: 회원 없음


### 13.5. API-003: 회원 목록 조회

GET /api/v1/members?page=0&size=20

Success Status:

- 200 OK

Query Parameters:

- page: Integer, Default 0
- size: Integer, Default 20

Response:

    {
        "content": [],
        "page": 0,
        "size": 20,
        "totalElements": 0,
        "totalPages": 0
    }

content의 각 요소는 MemberResponse 구조를 사용한다.

Failure Status:

- 400: 잘못된 페이지 조건


### 13.6. API-004: 회원 수정

PATCH /api/v1/members/{id}

Request Body:

    {
        "nickname": "변경된회원"
    }

Success Status:

- 200 OK

Response:

- MemberResponse

Failure Status:

- 400: 잘못된 입력 또는 UUID
- 404: 회원 없음


### 13.7. API-005: 회원 삭제

DELETE /api/v1/members/{id}

Success Status:

- 204 No Content

Response Body:

- NONE

Failure Status:

- 400: 잘못된 UUID 형식
- 404: 회원 없음


### 13.8. MemberResponse

    {
        "memberId": "UUID 문자열",
        "email": "member@example.com",
        "nickname": "테스트회원",
        "createdAt": "2026-11-25T00:00:00Z",
        "updatedAt": "2026-11-25T00:00:00Z"
    }

위 날짜 및 식별자는 응답 구조 설명용이다.

실제 응답에서는 저장된 값에 따라 반환한다.


## 14. 예외 처리

### 14.1. 오류 응답 구조

    {
        "code": "MEMBER_NOT_FOUND",
        "message": "존재하지 않는 회원입니다."
    }

### 14.2. 오류 코드

| Error Code | HTTP Status | 설명 |
|---|---|---|
| INVALID_REQUEST | 400 | 유효하지 않은 요청 |
| MEMBER_NOT_FOUND | 404 | 존재하지 않는 회원 |
| DUPLICATE_EMAIL | 409 | 중복 이메일 |
| INTERNAL_SERVER_ERROR | 500 | 예상하지 못한 서버 오류 |

Domain Exception에는 HTTP 상태를
직접 의존시키지 않는다.

Web 계층에서 예외와 HTTP 상태를 매핑한다.

내부 Stack Trace 또는 DB 정보를
API 응답으로 노출하지 않는다.


## 15. 개발 환경

### 15.1. 로컬 실행

- OS: Windows
- Terminal: Git Bash
- Application Host: 127.0.0.1
- Application Port: 8080
- Local MySQL Port: 3307
- Active Profile: local

### 15.2. 환경 변수

| Name | Required | Purpose |
|---|---|---|
| DB_URL | YES | JDBC Connection URL |
| DB_USERNAME | YES | MySQL Username |
| DB_PASSWORD | YES | MySQL Password |
| MYSQL_ROOT_PASSWORD | Compose 실행 시 | Local MySQL 초기 설정 |

실제 비밀번호를 Git에 저장하지 않는다.

.env.example은 변수명과
설명만 제공한다.

.env는 Git 추적 대상에서 제외한다.


### 15.3. 로컬 DB 실행

Docker Compose를 사용한다.

- MySQL 8.0 컨테이너
- Host Port: 3307
- Container Port: 3306
- Host Bind: 127.0.0.1
- Database: member_management

Docker Compose와 Testcontainers의
DB 실행 환경을 혼용하지 않는다.


### 15.4. 실행 명령어

    ./gradlew --version
    ./gradlew compileJava
    ./gradlew test
    ./gradlew build
    ./gradlew bootRun

Windows 환경에 따라
gradlew.bat를 사용할 수 있다.

실제 태스크 존재 여부를 확인한다.


## 16. 테스트 전략

### 16.1. Domain Unit Test

Spring Context 없이 테스트한다.

검증 대상:

- 회원 생성
- 이메일 정규화
- 이메일 유효성
- 닉네임 유효성
- 닉네임 변경
- 변경 불가능한 필드 유지
- 동일 닉네임 처리


### 16.2. Application Unit Test

JUnit 및 Mockito를 사용한다.

필요한 Output Port를 Mock 또는 Fake로 대체한다.

검증 대상:

- 회원 등록 유스케이스
- 중복 이메일 처리
- 회원 단건 조회
- 회원 목록 조회
- 회원 수정
- 회원 삭제
- 존재하지 않는 회원 예외


### 16.3. Web Adapter Test

MockMvc를 사용한다.

검증 대상:

- API 경로 및 HTTP Method
- HTTP 상태 코드
- 요청 DTO 검증
- 오류 응답
- 잘못된 UUID 처리
- 페이지네이션
- 허용되지 않은 필드 처리


### 16.4. MySQL Integration Test

Testcontainers MySQL을 사용한다.

- MySQL 8.0
- 테스트 전용 독립 컨테이너
- Flyway 마이그레이션 적용
- 실제 Repository 및 Persistence Adapter 검증
- UNIQUE 제약조건 검증
- UUID 영속성 검증
- 날짜 정밀도 검증
- 동시 등록 검증
- 전체 CRUD 연동 검증

테스트 컨테이너를 실행할 수 없으면
필수 DB 검증은 BLOCKED로 처리한다.

H2 결과를 MySQL 검증으로 보고하지 않는다.


### 16.5. Architecture Test

ArchUnit을 필수 검증 도구로 사용한다.

검증 규칙:

- Domain은 Spring에 의존하지 않는다.
- Domain은 Jakarta Persistence에 의존하지 않는다.
- Domain은 Application 및 Adapter에 의존하지 않는다.
- Application은 Adapter에 의존하지 않는다.
- Input Adapter는 Output Adapter에 직접 의존하지 않는다.
- Controller는 Spring Data Repository에
  직접 의존하지 않는다.

ArchUnit 검사 규칙은 실제 패키지 구조에 맞춰
구현한다.

검사 대상 클래스가 존재하지 않아
의미 없이 통과하는 테스트를 작성하지 않는다.


### 16.6. 테스트 실행 정책

필수 테스트는 Gradle test 또는
명시된 필수 통합 테스트 태스크에 연결한다.

최종 build 또는 별도의 필수 검증 명령에
모든 필수 테스트가 포함되어야 한다.

실패하는 테스트를 삭제하거나
비활성화하지 않는다.


## 17. 인수 조건

### AC-001: 정상 회원 등록

- HTTP 201 반환
- UUID 식별자 반환
- Location 헤더 반환
- MySQL에 회원 정보 저장

### AC-002: 중복 이메일

- 정규화된 동일 이메일의 중복 등록 차단
- HTTP 409 반환
- 기존 데이터 유지

### AC-003: 입력 검증

- 잘못된 이메일 또는 닉네임 차단
- HTTP 400 반환
- 유효하지 않은 데이터 미저장

### AC-004: 회원 단건 조회

- 존재하는 회원 조회 시 HTTP 200
- 저장된 회원 정보 반환

### AC-005: 존재하지 않는 회원 조회

- HTTP 404 반환
- MEMBER_NOT_FOUND 오류 코드 반환

### AC-006: 회원 목록

- HTTP 200 반환
- 페이지 정보 제공
- 정렬 조건 준수
- 빈 데이터는 빈 목록 반환

### AC-007: 잘못된 페이지 조건

- page가 음수이면 HTTP 400
- size가 1 미만 또는 100 초과이면 HTTP 400

### AC-008: 회원 수정

- HTTP 200 반환
- 닉네임 변경
- 이메일과 회원 ID 유지
- 생성일 유지
- 실제 변경 시 수정일 갱신

### AC-009: 동일 닉네임 수정

- HTTP 200 반환
- 기존 데이터 유지
- 수정일 유지

### AC-010: 회원 삭제

- HTTP 204 반환
- 응답 본문 없음
- 실제 데이터 삭제
- 삭제 이후 조회 시 HTTP 404

### AC-011: 존재하지 않는 회원 수정 및 삭제

- HTTP 404 반환
- 새로운 데이터가 생성되지 않음

### AC-012: 잘못된 식별자

- 잘못된 UUID 형식 요청 시 HTTP 400

### AC-013: 허용되지 않은 필드

- 이메일 변경 요청 등 차단
- HTTP 400 반환
- 기존 데이터 유지

### AC-014: 동시 중복 등록

- 동일 이메일에 대한 동시 등록 요청 처리
- 정확히 하나의 회원 데이터만 생성
- 정상 요청 1건 성공
- 나머지 중복 요청은 정해진 충돌 오류 반환

### AC-015: 아키텍처

- Hexagonal Architecture 준수
- SOLID 원칙 준수
- 필수 ArchUnit 테스트 통과


## 18. 기술적 제약사항

- Java 17 유지
- Spring Boot 지정 버전 유지
- Hexagonal Architecture 준수
- SOLID 원칙 준수
- 생성자 주입 사용
- Domain과 JPA Entity 분리
- 운영 데이터베이스 사용 금지
- 민감정보 하드코딩 금지
- 인증 및 인가 기능 추가 금지
- 불필요한 외부 시스템 도입 금지
- 기존 하네스 설정 무단 변경 금지
- 사용자 승인 없는 Git push 및 배포 금지


## 19. 개발 단계

| Stage | 작업 |
|---|---|
| STAGE-001 | 프로젝트 기본 설정 |
| STAGE-002 | 회원 등록 |
| STAGE-003 | 단건 및 목록 조회 |
| STAGE-004 | 회원 닉네임 수정 |
| STAGE-005 | 회원 삭제 |
| STAGE-006 | 통합 테스트 및 아키텍처 검증 |
| STAGE-007 | 수정 루프 검증 |
| STAGE-008 | 최종 Quality Gate |

각 개발 요청은 독립적인 하네스 작업으로 관리한다.

실제 작업 상태 및 수정 횟수는
harness/workflow.md를 따른다.

이전 작업이 완료되었다고 가정하지 않고
실제 소스 코드와 검증 결과를 확인한다.


## 20. 최종 완료 기준

- FR-001부터 FR-007까지 구현
- AC-001부터 AC-015까지 검증
- 실제 Planner 호출
- 실제 Developer 호출
- 실제 Reviewer 호출
- 최종 Reviewer PASS
- 컴파일 성공
- 단위 테스트 성공
- Web Adapter 테스트 성공
- MySQL 통합 테스트 성공
- ArchUnit 테스트 성공
- 최종 Gradle 빌드 성공
- 미해결 차단 결함 없음

필수 검증을 실행할 수 없다면
COMPLETED로 처리하지 않는다.

최종 작업 판단은 AGENTS.md,
harness/rules.md 및 harness/workflow.md의
기준을 모두 충족해야 한다.
