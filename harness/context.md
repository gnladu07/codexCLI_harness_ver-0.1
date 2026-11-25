
# Project Context

## 1. Context 운영 원칙

### 1.1. 목적

본 문서는 프로젝트의 목적, 요구사항, 기술 환경,
비즈니스 정책 및 설계 정보를 정의한다.

모든 에이전트는 동일한 프로젝트 정보를 기반으로
설계, 구현 및 검증을 수행해야 한다.

적용 대상:

- Orchestrator
- Planner Agent
- Developer Agent
- Reviewer Agent

본 문서는 AGENTS.md 및 harness/rules.md와
함께 사용한다.


### 1.2. 문서 기본 정보

- Document Name: Project Context
- Document Version: 1.0.0
- Project Name: TBD
- Last Updated: TBD
- Author: TBD
- Project Status: NOT_STARTED

Project Status:

- NOT_STARTED: 개발 시작 전
- PLANNING: 기획 및 설계
- IN_DEVELOPMENT: 개발 진행 중
- TESTING: 테스트 진행 중
- RELEASED: 배포 완료
- MAINTENANCE: 유지보수


### 1.3. 정보 상태 정의

프로젝트 정보는 다음 상태로 구분한다.

- CONFIRMED: 확정된 정보
- PROPOSED: 제안된 정보
- TBD: 미확정 정보
- DEPRECATED: 폐기된 정보
- NOT_APPLICABLE: 적용 대상이 아닌 정보

TBD 항목을 확정된 정보로 간주하지 않는다.

예시 또는 참고 자료를 실제 요구사항으로
임의 해석하지 않는다.

PROPOSED 항목은 승인되기 전까지
확정된 정책으로 적용하지 않는다.


### 1.4. 정보 확인 및 충돌 처리

다음 정보를 작업 목적에 따라 확인한다.

- 사용자의 현재 작업 요청
- 승인된 프로젝트 요구사항
- 승인된 기술 및 설계 문서
- 실제 프로젝트 소스 코드
- 실제 빌드 및 환경 설정
- 데이터베이스 스키마
- API 계약
- context.md

현재 구현 상태는 실제 코드와 설정을 기준으로
확인한다.

목표 구현 상태는 승인된 요구사항 및
설계 문서를 기준으로 판단한다.

실제 구현과 요구사항이 서로 다르면
이를 임의로 동일한 상태라고 판단하지 않는다.

중대한 정보 충돌이 발생하면
Orchestrator에게 해당 내용을 보고한다.

AGENTS.md 및 rules.md의 필수 규칙을
context.md만으로 임의 해제하지 않는다.


### 1.5. 미확정 정보 처리

미확정 정보는 다음과 같이 처리한다.

구현에 영향이 적은 항목:

- 기존 프로젝트 관례를 우선 확인한다.
- 안전하고 단순한 기본값을 고려한다.
- 위임받은 범위에서 합리적으로 결정한다.
- 선택한 기본값과 결정 근거를 기록한다.

구현에 중대한 영향을 주는 항목:

- 임의로 확정하지 않는다.
- 관련 요구사항과 문서를 추가 확인한다.
- 필요한 경우 사용자에게 확인한다.
- 해결할 수 없는 경우 BLOCKED로 보고한다.

다음 항목은 특히 주의한다.

- 인증 및 인가 정책
- 개인정보 처리 정책
- 결제 및 환불 정책
- 데이터 삭제 정책
- 운영 데이터베이스 변경
- 외부 유료 서비스 도입
- 시스템 아키텍처 변경
- 기존 API 계약의 파괴적 변경


## 2. 프로젝트 개요

### 2.1. 프로젝트 기본 정보

| 항목 | 내용 | 상태 |
|---|---|---|
| 프로젝트명 | TBD | TBD |
| 서비스 유형 | 웹 또는 앱 백엔드 | PROPOSED |
| 프로젝트 목적 | TBD | TBD |
| 주요 사용자 | TBD | TBD |
| 개발 단계 | 신규 프로젝트 | PROPOSED |
| 개발 기간 | TBD | TBD |
| 목표 배포 환경 | TBD | TBD |


### 2.2. 프로젝트 목적

본 프로젝트가 해결하려는 문제:

- TBD

사용자에게 제공하는 핵심 가치:

- TBD

프로젝트의 최종 목표:

- TBD


### 2.3. 서비스 설명

서비스의 주요 동작 방식:

- TBD

핵심 사용자 흐름:

1. TBD
2. TBD
3. TBD


### 2.4. 개발 범위

In Scope:

- TBD

Out of Scope:

- TBD

현재 개발 범위를 벗어나는 기능은
사용자 승인 없이 추가하지 않는다.


### 2.5. 개발 단계

Phase 1:

- MVP 핵심 기능
- 세부 내용: TBD

Phase 2:

- 추가 기능 및 개선
- 세부 내용: TBD

Phase 3:

- 성능 및 운영 고도화
- 세부 내용: TBD

현재 개발 대상이 아닌 기능을
선행 구현하지 않는다.


## 3. 사용자 및 역할

### 3.1. 사용자 유형

| Role ID | 사용자 유형 | 설명 | 상태 |
|---|---|---|---|
| ROLE-001 | TBD | TBD | TBD |
| ROLE-002 | TBD | TBD | TBD |


### 3.2. 사용자별 주요 기능

ROLE-001:

- TBD

ROLE-002:

- TBD


### 3.3. 권한 매트릭스

| 기능 | ROLE-001 | ROLE-002 |
|---|---|---|
| 기능 1 | TBD | TBD |
| 기능 2 | TBD | TBD |
| 기능 3 | TBD | TBD |

권한 상태:

- ALLOW: 접근 허용
- DENY: 접근 금지
- CONDITIONAL: 특정 조건에서 허용
- TBD: 미확정

TBD 권한을 ALLOW로 간주하지 않는다.

보호 대상 리소스는 승인된 권한 정책에
따라서만 접근할 수 있도록 구현한다.


### 3.4. 사용자 계정 정책

- 회원가입 대상: TBD
- 관리자 계정 생성 방식: TBD
- 계정 활성화 방식: TBD
- 계정 정지 정책: TBD
- 탈퇴 정책: TBD
- 다중 역할 허용 여부: TBD


## 4. 기술 스택

### 4.1. Backend

| 구분 | 기술 | 버전 및 상태 |
|---|---|---|
| Language | Java | 17 / CONFIRMED |
| Framework | Spring Boot | 버전 TBD |
| Build Tool | Gradle | 버전 TBD |
| Web | Spring MVC | PROPOSED |
| ORM | Spring Data JPA | PROPOSED |
| Validation | Jakarta Bean Validation | PROPOSED |
| Security | Spring Security | 적용 범위 TBD |

Java 17과 호환되는 안정적인 버전을 사용한다.

구체적인 프레임워크 및 라이브러리 버전은
사용자 요구사항과 실제 빌드 설정을 확인한다.


### 4.2. Database

| 구분 | 기술 | 상태 |
|---|---|---|
| RDBMS | MySQL | 기본 개발 표준 |
| Database Version | TBD | TBD |
| ORM | Spring Data JPA | 기본 개발 표준 |
| Migration | TBD | TBD |
| Cache | TBD | TBD |

캐시, 메시지 브로커 및 추가 데이터베이스는
실제 필요성과 승인된 기술 결정을 기준으로 적용한다.


### 4.3. Testing

| 구분 | 기술 | 상태 |
|---|---|---|
| Unit Test | JUnit 5 | CONFIRMED |
| Mocking | Mockito | 기본 개발 표준 |
| Web Test | MockMvc | PROPOSED |
| Integration Test | Spring Boot Test | PROPOSED |
| Database Test | Testcontainers | OPTIONAL |
| Architecture Test | ArchUnit | OPTIONAL |
| Coverage | JaCoCo | OPTIONAL |

OPTIONAL은 선택적 도구를 의미한다.

선택적 도구를 설치된 상태로 간주하지 않는다.


### 4.4. Infrastructure

| 구분 | 기술 | 상태 |
|---|---|---|
| OS | TBD | TBD |
| Cloud | TBD | TBD |
| Container | TBD | TBD |
| CI/CD | TBD | TBD |
| Monitoring | TBD | TBD |
| Logging | TBD | TBD |
| Version Control | Git | CONFIRMED |


### 4.5. 기술 스택 변경 정책

기존 기술 스택을 우선한다.

새로운 기술이 필요한 경우 다음을 검토한다.

- 도입 목적
- 기존 기술로 대체 가능 여부
- 호환성
- 유지보수 비용
- 보안 영향
- 운영 복잡도
- 대체 방안

중대한 기술 스택 변경은 승인 후 적용한다.


## 5. 시스템 아키텍처

### 5.1. 외부 시스템 구조

System Architecture: TBD

선택 가능한 구조:

- Monolith
- Modular Monolith
- Microservices Architecture

프로젝트 요구사항과 규모를 기준으로 결정한다.

Hexagonal Architecture를 적용한다는 이유만으로
MSA를 자동 선택하지 않는다.

이미 확정된 시스템 구조가 있는 경우
해당 구조를 우선한다.


### 5.2. 내부 아키텍처

Internal Architecture:

- Hexagonal Architecture
- Status: CONFIRMED

Design Principles:

- SOLID
- KISS
- DRY
- YAGNI
- Separation of Concerns

상세한 계층 및 의존성 규칙은
harness/rules.md를 따른다.


### 5.3. 기본 애플리케이션 구조

    Client
        |
    Input Adapter
        |
    Input Port
        |
    Application Service
        |
    Domain / Output Port
        |
    Output Adapter
        |
    External System

위 구조는 개념적인 실행 흐름을 표현한다.

소스 코드 의존성 방향은
harness/rules.md의 아키텍처 규칙을 따른다.


### 5.4. 프로젝트 패키지

Base Package: TBD

기본 패키지 구성:

    {base-package}
    |
    |-- {domain}
    |   |-- domain
    |   |-- application
    |   |   |-- port
    |   |   |   |-- in
    |   |   |   |-- out
    |   |   |-- service
    |   |   |-- command
    |   |   |-- query
    |   |
    |   |-- adapter
    |       |-- in
    |       |-- out
    |
    |-- global
        |-- config
        |-- exception
        |-- security

현재 기능에 필요한 패키지만 생성한다.

TBD 또는 Placeholder를 실제 패키지명으로
그대로 사용하지 않는다.


### 5.5. 모듈 및 서비스 구성

| Module ID | 모듈 또는 서비스명 | 책임 | 상태 |
|---|---|---|---|
| MOD-001 | TBD | TBD | TBD |
| MOD-002 | TBD | TBD | TBD |

모듈 간 의존성:

- TBD

서비스 간 통신 방식:

- TBD

데이터 소유권:

- TBD

시스템 구조가 확정되기 전까지
불필요한 서비스 분리나 분산 통신을 구현하지 않는다.


## 6. 도메인 정의

### 6.1. 도메인 목록

| Domain ID | 도메인명 | 주요 책임 | 우선순위 | 상태 |
|---|---|---|---|---|
| DOM-001 | TBD | TBD | TBD | TBD |
| DOM-002 | TBD | TBD | TBD | TBD |


### 6.2. 도메인 상세 정의 템플릿

Domain ID: DOM-001

Domain Name: TBD

Description:

- TBD

Responsibility:

- TBD

Aggregate:

- TBD

Domain Entity:

- TBD

Value Object:

- TBD

Domain Service:

- TBD

Business Policy:

- TBD

Domain Event:

- TBD

Related Domain:

- TBD

Data Ownership:

- TBD

Out of Scope:

- TBD

실제 도메인 요구사항에 필요한 구성요소만
설계하고 구현한다.


### 6.3. 도메인 용어 사전

| 용어 | 영문명 | 비즈니스 의미 |
|---|---|---|
| TBD | TBD | TBD |

동일한 비즈니스 개념에 대해
서로 다른 용어를 무분별하게 사용하지 않는다.

클래스명, API명 및 데이터베이스 명칭은
확정된 도메인 용어를 우선한다.


### 6.4. 도메인 간 관계

| Source Domain | Target Domain | 관계 및 목적 | 연동 방식 |
|---|---|---|---|
| TBD | TBD | TBD | TBD |

다른 도메인의 내부 구현에 직접 의존하지 않는다.

도메인 간 순환 의존성을 방지한다.


## 7. 비즈니스 정책

### 7.1. 정책 목록

| Policy ID | 도메인 | 정책명 | 내용 | 상태 |
|---|---|---|---|---|
| POL-001 | TBD | TBD | TBD | TBD |
| POL-002 | TBD | TBD | TBD | TBD |


### 7.2. 정책 상세 템플릿

Policy ID: POL-001

Policy Name: TBD

Purpose:

- TBD

Precondition:

- TBD

Business Rule:

- TBD

Success Condition:

- TBD

Failure Condition:

- TBD

Exception Policy:

- TBD

Related Requirement:

- TBD

Related Test:

- TBD


### 7.3. 상태 및 상태 전이

| Domain | Current Status | Next Status | Condition |
|---|---|---|---|
| TBD | TBD | TBD | TBD |

허용되지 않은 상태 변경은
비즈니스 규칙에 따라 방지한다.


### 7.4. 데이터 처리 정책

- 중복 데이터 처리: TBD
- 삭제 정책: TBD
- 데이터 복구 정책: TBD
- 데이터 보존 기간: TBD
- 개인정보 처리 정책: TBD
- 데이터 변경 이력 정책: TBD


### 7.5. 시간 및 숫자 정책

- 서비스 기준 시간대: TBD
- 데이터 저장 시간대: TBD
- 날짜 표현 방식: TBD
- 기본 화폐 단위: TBD
- 금액 반올림 정책: TBD

적용되지 않는 항목은
NOT_APPLICABLE로 명시한다.


## 8. 기능 요구사항

### 8.1. 기능 요구사항 목록

| Requirement ID | 도메인 | 기능명 | Priority | 상태 |
|---|---|---|---|---|
| FR-001 | TBD | TBD | MUST | TBD |
| FR-002 | TBD | TBD | SHOULD | TBD |

Priority:

- MUST: 반드시 구현해야 하는 기능
- SHOULD: 구현이 권장되는 기능
- COULD: 선택적으로 구현 가능한 기능
- WONT: 현재 개발 범위에서 제외되는 기능

요구사항의 중요도와 구현 진행 상태를
서로 혼동하지 않는다.


### 8.2. 기능 상세 명세 템플릿

Requirement ID: FR-001

Feature Name: TBD

Domain: TBD

Priority: MUST

Requirement Status: TBD

Actor:

- TBD

Description:

- TBD

Precondition:

- TBD

Trigger:

- TBD

Main Flow:

1. TBD
2. TBD
3. TBD

Alternative Flow:

1. TBD

Exception Flow:

1. TBD

Postcondition:

- TBD

Related Policy:

- TBD

Related API:

- TBD

Related Database:

- TBD

Acceptance Criteria:

- TBD


### 8.3. 요구사항 변경 규칙

새로운 요구사항은 기존 기능과
충돌하는지 먼저 확인한다.

기존 요구사항의 변경 이력을 유지한다.

요구사항 변경으로 영향을 받는 항목을 확인한다.

- Domain
- API
- Database
- Security
- Test
- External System
- Existing Feature


### 8.4. 현재 작업 범위

Current Milestone: TBD

Target Requirements:

- TBD

Excluded Requirements:

- TBD

실제 작업 범위는 사용자의 현재 요청과
승인된 개발 계획을 기준으로 확정한다.


## 9. 비기능 요구사항

### 9.1. 성능

| Requirement ID | 항목 | 목표 | 상태 |
|---|---|---|---|
| NFR-001 | 예상 사용자 수 | TBD | TBD |
| NFR-002 | 동시 접속자 수 | TBD | TBD |
| NFR-003 | API 응답 시간 | TBD | TBD |
| NFR-004 | 초당 요청 수 | TBD | TBD |
| NFR-005 | 일일 요청 수 | TBD | TBD |
| NFR-006 | 예상 데이터 규모 | TBD | TBD |

성능 목표는 실제 서비스 요구사항에 따라 결정한다.

부하 테스트나 측정 결과 없이
목표 성능을 달성했다고 보고하지 않는다.


### 9.2. 안정성

- 목표 가용성: TBD
- 장애 복구 목표 시간(RTO): TBD
- 데이터 복구 목표 시점(RPO): TBD
- 장애 대응 방식: TBD
- 백업 정책: TBD


### 9.3. 보안

- 인증 방식: TBD
- 접근 제어 방식: TBD
- 민감정보 보호 정책: TBD
- 데이터 암호화 범위: TBD
- 보안 감사 정책: TBD


### 9.4. 확장성

- 수평 확장 필요 여부: TBD
- 수직 확장 필요 여부: TBD
- 서비스 분리 계획: TBD
- 데이터 증가 대응 방식: TBD

확장성을 이유로 현재 필요하지 않은
복잡한 인프라를 선행 도입하지 않는다.


### 9.5. 유지보수성

- Hexagonal Architecture 준수
- SOLID 원칙 준수
- 일관된 코드 컨벤션
- 테스트 가능한 구조
- 주요 비즈니스 정책의 명확한 표현
- 기술 결정 및 변경 이력 관리

세부 개발 표준은 harness/rules.md를 따른다.


## 10. 데이터베이스 컨텍스트

### 10.1. 데이터베이스 기본 정보

| 항목 | 내용 | 상태 |
|---|---|---|
| DBMS | MySQL | 기본 개발 표준 |
| Version | TBD | TBD |
| Database Name | TBD | TBD |
| Character Set | TBD | TBD |
| Collation | TBD | TBD |
| Time Zone | TBD | TBD |
| Identifier Strategy | TBD | TBD |
| Migration Tool | TBD | TBD |


### 10.2. 데이터베이스 참고 자료

- ERD: TBD
- DDL: TBD
- 테이블 정의서: TBD
- 마이그레이션 경로: TBD
- 시드 데이터 경로: TBD

이미 존재하는 ERD와 DDL을 우선 확인한다.

승인된 스키마를 임의로 변경하지 않는다.


### 10.3. 테이블 목록

| Table ID | 테이블명 | 도메인 | 용도 | 상태 |
|---|---|---|---|---|
| TBL-001 | TBD | TBD | TBD | TBD |
| TBL-002 | TBD | TBD | TBD | TBD |


### 10.4. 테이블 상세 템플릿

Table ID: TBL-001

Table Name: TBD

Domain: TBD

Description:

- TBD

Columns:

| Column | Type | Nullable | Key | Default | Description |
|---|---|---|---|---|---|
| TBD | TBD | TBD | TBD | TBD | TBD |

Primary Key:

- TBD

Foreign Key:

- TBD

Unique Constraint:

- TBD

Index:

- TBD

Check Constraint:

- TBD

Related Table:

- TBD

Delete Policy:

- TBD


### 10.5. 데이터베이스 명명 규칙

- 테이블명 규칙: TBD
- 컬럼명 규칙: TBD
- PK 명명 규칙: TBD
- FK 명명 규칙: TBD
- Index 명명 규칙: TBD

확정된 프로젝트 규칙이 없는 경우에는
기존 스키마 관례와 일반적인 SQL 명명 규칙을
검토하여 제안한다.


### 10.6. 데이터 변경 정책

- 신규 테이블 추가 정책: TBD
- 기존 컬럼 변경 정책: TBD
- 마이그레이션 실행 정책: TBD
- 초기 데이터 적재 정책: TBD
- 테스트 데이터 관리 정책: TBD
- 운영 데이터 접근 정책: TBD

실제 운영 데이터베이스의 조회 및 변경은
승인된 작업 범위와 권한 정책을 따른다.

운영 데이터를 개발 및 테스트 데이터로
무단 복제하지 않는다.


## 11. API 컨텍스트

### 11.1. API 기본 정보

| 항목 | 내용 | 상태 |
|---|---|---|
| API Style | REST API | PROPOSED |
| Base Path | /api/v1 | PROPOSED |
| Data Format | JSON | PROPOSED |
| Authentication | TBD | TBD |
| API Documentation | TBD | TBD |
| Error Format | TBD | TBD |
| Pagination | TBD | TBD |

기존 API 계약이 존재한다면
해당 계약을 우선 확인한다.


### 11.2. API 목록

| API ID | Method | URI | 기능 | 관련 요구사항 |
|---|---|---|---|---|
| API-001 | TBD | TBD | TBD | FR-001 |
| API-002 | TBD | TBD | TBD | FR-002 |


### 11.3. API 상세 명세 템플릿

API ID: API-001

API Name: TBD

Domain: TBD

Method: TBD

URI: TBD

Related Requirement: TBD

Authentication: TBD

Authorization: TBD

Description:

- TBD

Path Variable:

| Name | Type | Required | Description |
|---|---|---|---|
| TBD | TBD | TBD | TBD |

Query Parameter:

| Name | Type | Required | Description |
|---|---|---|---|
| TBD | TBD | TBD | TBD |

Request Header:

| Name | Required | Description |
|---|---|---|
| TBD | TBD | TBD |

Request Body:

| Field | Type | Required | Validation |
|---|---|---|---|
| TBD | TBD | TBD | TBD |

Success Response:

- HTTP Status: TBD
- Response Header: TBD
- Response Body: TBD

Error Response:

| HTTP Status | Error Code | Description |
|---|---|---|
| TBD | TBD | TBD |

Business Rule:

- TBD

Acceptance Criteria:

- TBD


### 11.4. API 응답 정책

- 성공 응답 구조: TBD
- 실패 응답 구조: TBD
- 공통 응답 Wrapper 사용 여부: TBD
- 오류 코드 명명 규칙: TBD
- 날짜 및 시간 표현: TBD

공통 응답 Wrapper의 사용 여부는
기존 프로젝트 API 정책에 따라 결정한다.

모든 응답을 임의의 공통 구조로
일괄 변경하지 않는다.


### 11.5. API 호환성

기존 클라이언트가 사용하는 API 계약을
사용자 승인 없이 변경하지 않는다.

Breaking Change가 필요한 경우
영향 범위와 전환 방안을 기록한다.


## 12. 인증 및 인가 컨텍스트

### 12.1. 인증 정책

- Authentication Type: TBD
- Login Method: TBD
- Password Policy: TBD
- Token Policy: TBD
- Session Policy: TBD
- Login Failure Policy: TBD
- Account Lock Policy: TBD


### 12.2. 인가 정책

- Role Model: TBD
- Permission Model: TBD
- Resource Ownership Policy: TBD
- Administrator Policy: TBD

사용자가 전달한 역할 또는 사용자 ID를
검증 없이 권한 판단에 사용하지 않는다.


### 12.3. 인증 및 인가 흐름

    Client
        |
    Authentication
        |
    Authorization
        |
    Application Use Case

세부 인증 및 권한 검증 방식은
승인된 보안 정책과 시스템 구조를 기준으로 한다.


### 12.4. 토큰 정책

JWT 또는 토큰 기반 인증을 사용하는 경우 작성한다.

- Access Token Expiration: TBD
- Refresh Token Expiration: TBD
- Token Rotation: TBD
- Token Revocation: TBD
- Token Storage: TBD
- Signing Algorithm: TBD
- Issuer: TBD
- Audience: TBD

토큰 기반 인증을 사용하지 않는 경우
NOT_APPLICABLE로 표시한다.


### 12.5. 개인정보 및 보안 정책

- 개인정보 수집 범위: TBD
- 개인정보 조회 권한: TBD
- 개인정보 보존 기간: TBD
- 개인정보 삭제 정책: TBD
- 데이터 마스킹 정책: TBD
- 감사 로그 정책: TBD

실제 비밀번호, 토큰 및 암호화 키를
context.md에 작성하지 않는다.


## 13. 외부 시스템 연동

### 13.1. 연동 대상 목록

| Integration ID | 시스템명 | 목적 | 연동 방식 | 상태 |
|---|---|---|---|---|
| INT-001 | TBD | TBD | TBD | TBD |


### 13.2. 외부 시스템 상세 템플릿

Integration ID: INT-001

System Name: TBD

Purpose:

- TBD

Protocol:

- TBD

Endpoint Reference:

- TBD

Authentication Method:

- TBD

Request Contract:

- TBD

Response Contract:

- TBD

Connection Timeout:

- TBD

Read Timeout:

- TBD

Retry Policy:

- TBD

Failure Handling:

- TBD

Rate Limit:

- TBD

Idempotency Policy:

- TBD

Test Environment:

- TBD


### 13.3. 이벤트 및 메시징

이벤트 기반 통신을 사용하는 경우 작성한다.

- Message Broker: TBD
- Event Format: TBD
- Delivery Policy: TBD
- Retry Policy: TBD
- Dead Letter Policy: TBD
- Duplicate Handling: TBD
- Outbox Policy: TBD

Event 목록:

| Event ID | Event Name | Producer | Consumer |
|---|---|---|---|
| EVT-001 | TBD | TBD | TBD |

메시지 브로커가 필요하지 않은 프로젝트는
NOT_APPLICABLE로 표시한다.


### 13.4. 외부 서비스 사용 제약

- 승인되지 않은 유료 API를 호출하지 않는다.
- 실제 운영 외부 API를 임의로 테스트하지 않는다.
- 외부 서비스의 이용 제한을 우회하지 않는다.
- 민감정보를 승인되지 않은 시스템에 전달하지 않는다.


## 14. 개발 환경

### 14.1. 기본 환경

| 항목 | 내용 |
|---|---|
| Development OS | Windows |
| Terminal | Git Bash |
| Java | 17 |
| Build Tool | Gradle |
| IDE | TBD |
| Repository | TBD |
| Base Package | TBD |

Windows 및 Git Bash는 현재 하네스 구축
환경의 기본값이다.

다른 개발 환경에서는 실제 환경에 맞게 변경한다.


### 14.2. 프로젝트 실행 환경

- Local Application Port: TBD
- Local Database Port: TBD
- Local Database Name: TBD
- Active Profile: TBD
- External Dependencies: TBD

실제 프로젝트 설정을 확인하기 전까지
예시 포트 및 접속 정보를 확정값으로 사용하지 않는다.


### 14.3. 환경 변수 목록

| Variable | Purpose | Required | Default |
|---|---|---|---|
| DB_URL | DB 접속 주소 | TBD | NONE |
| DB_USERNAME | DB 사용자명 | TBD | NONE |
| DB_PASSWORD | DB 비밀번호 | TBD | NONE |
| JWT_SECRET | JWT 서명용 비밀값 | TBD | NONE |

위 환경 변수는 예시이다.

실제 환경 변수명은 application.yml 및
프로젝트 설정을 확인하여 결정한다.

JWT를 사용하지 않는 프로젝트에는
JWT_SECRET이 필요하지 않다.

실제 민감정보 값은 기록하지 않는다.


### 14.4. 환경별 설정

Local:

- 개발자 로컬 환경
- 별도의 개발용 데이터 사용

Test:

- 테스트 전용 환경
- 운영 시스템과 분리

Development:

- 개발 서버 환경
- 상세 설정 TBD

Production:

- 실제 운영 환경
- 접근 및 변경 정책 TBD


### 14.5. 개발 명령어

Java 버전 확인:

    java -version

Git 상태 확인:

    git status

Gradle 버전 확인:

    ./gradlew --version

애플리케이션 실행:

    ./gradlew bootRun

컴파일:

    ./gradlew compileJava

테스트:

    ./gradlew test

빌드:

    ./gradlew build

위 명령어는 Gradle Wrapper와 관련 플러그인이
설정된 Spring Boot 프로젝트를 기준으로 한다.

Windows PowerShell 또는 CMD에서는
gradlew.bat를 사용할 수 있다.

존재하지 않는 명령어 및 태스크를
실행했다고 주장하지 않는다.


### 14.6. 인프라 및 배포

- Cloud Provider: TBD
- Server Environment: TBD
- Container Environment: TBD
- Deployment Strategy: TBD
- CI/CD Pipeline: TBD
- Deployment Approval Policy: TBD

배포는 승인된 운영 정책에 따라 수행한다.


## 15. 기술적 제약사항

### 15.1. 필수 기술 제약

- Java 17 호환성을 유지한다.
- Hexagonal Architecture를 준수한다.
- SOLID 원칙을 준수한다.
- 기존 프로젝트의 기술 및 코드 규칙을 유지한다.
- Domain 계층에 외부 기술을 직접 의존시키지 않는다.
- 필수 테스트 및 검증을 수행한다.


### 15.2. 변경 제한

다음 항목은 승인 없이 변경하지 않는다.

- Java 버전
- Spring Boot 주요 버전
- 시스템 아키텍처
- 내부 아키텍처
- 기존 데이터베이스 스키마
- 인증 및 인가 정책
- 기존 공개 API 계약
- 운영 인프라 설정

사용자가 명시적으로 요청하고 승인한 변경은
영향 범위를 확인한 후 수행할 수 있다.


### 15.3. 기술 도입 제한

다음 기술은 실제 필요성과
승인된 기술 결정을 기준으로 도입한다.

- Redis
- Kafka
- RabbitMQ
- Elasticsearch
- Kubernetes
- 추가 데이터베이스
- 분산 락
- CQRS
- Event Sourcing

기술의 사용 자체를 프로젝트 목표로 삼지 않는다.


### 15.4. 프로젝트별 추가 제약

| Constraint ID | 제약사항 | 이유 | 상태 |
|---|---|---|---|
| CST-001 | TBD | TBD | TBD |

프로젝트별 제약사항은
승인된 요구사항에 따라 작성한다.


### 15.5. 보안 및 비용 제약

- 외부 서비스 사용 예산: TBD
- 클라우드 운영 예산: TBD
- 라이선스 제한: TBD
- 개인정보 처리 제한: TBD
- 운영 환경 접근 제한: TBD

승인되지 않은 비용을 발생시키지 않는다.


## 16. 테스트 컨텍스트

### 16.1. 테스트 목표

- 핵심 비즈니스 규칙 검증
- 기능 요구사항 검증
- 실패 및 예외 경로 검증
- 데이터 무결성 검증
- 인증 및 인가 검증
- 기존 기능 회귀 방지


### 16.2. 테스트 환경

| 항목 | 내용 |
|---|---|
| Unit Test Framework | JUnit 5 |
| Mocking Framework | Mockito |
| Test Database | TBD |
| External API Test | TBD |
| Test Profile | TBD |
| Architecture Test | TBD |
| Coverage Threshold | TBD |

커버리지 기준이 확정되지 않은 경우
임의의 수치를 필수 조건으로 지정하지 않는다.


### 16.3. 테스트 데이터 정책

- 운영 데이터 사용 금지
- 테스트 전용 데이터 사용
- 개인정보가 없는 가상 데이터 우선
- 테스트 간 데이터 독립성 유지
- 테스트 실행 후 필요한 정리 수행


### 16.4. 테스트 케이스 목록

| Test ID | Requirement ID | 테스트 목적 | 유형 |
|---|---|---|---|
| TC-001 | FR-001 | TBD | UNIT |
| TC-002 | FR-001 | TBD | INTEGRATION |

Test Type:

- UNIT
- SLICE
- INTEGRATION
- ARCHITECTURE
- E2E
- PERFORMANCE
- SECURITY


### 16.5. 필수 검증 기준

- 적용 대상 컴파일 성공
- 적용 대상 단위 테스트 성공
- 필수 통합 테스트 성공
- 기존 테스트 회귀 없음
- 최종 빌드 성공
- 설정된 필수 품질 검사 통과
- Reviewer 검토 PASS

상세 테스트 규칙은
harness/rules.md를 따른다.

작업 범위에 맞는 검증 계획은
Planner가 구체적으로 정의한다.


## 17. 인수 조건

### 17.1. 인수 조건 정의

인수 조건은 해당 기능을 완료했다고
판단하기 위한 구체적인 검증 기준이다.

모호한 표현을 피하고
확인 가능한 기대 결과를 정의한다.

잘못된 예:

- 회원가입이 잘 동작해야 한다.

권장 예:

- 유효한 입력으로 회원가입하면
  HTTP 201 응답을 반환한다.
- 승인된 정책에 따라 회원 데이터가 저장된다.
- 중복 이메일 요청은 정해진 오류를 반환한다.
- 비밀번호가 평문으로 저장되지 않는다.

위 내용은 설명용 예시이며
실제 프로젝트 요구사항을 의미하지 않는다.


### 17.2. 인수 조건 상세 템플릿

Acceptance ID: AC-001

Related Requirement: FR-001

Related Policy: TBD

Scenario: TBD

Given:

- TBD

When:

- TBD

Then:

- TBD

Related API:

- TBD

Related Test:

- TBD


### 17.3. 요구사항 추적 매트릭스

| Requirement | Policy | API | Table | Acceptance | Test |
|---|---|---|---|---|---|
| FR-001 | TBD | TBD | TBD | AC-001 | TC-001 |
| FR-002 | TBD | TBD | TBD | TBD | TBD |

실제 연결 관계가 없는 항목은
NOT_APPLICABLE로 표시한다.

추적 매트릭스는 요구사항 누락 및
테스트 누락을 확인하는 데 사용한다.


### 17.4. 기능 완료 판단

개별 기능은 다음 기준을 충족해야 한다.

- 확정된 기능 요구사항 구현
- 적용 대상 비즈니스 정책 준수
- 정의된 인수 조건 충족
- 필요한 테스트 작성 및 통과
- 관련 API 계약 준수
- 필요한 데이터 무결성 유지
- 필수 보안 정책 준수

최종 작업 완료 판단은
AGENTS.md 및 harness/rules.md를 따른다.


## 18. 프로젝트 참고 자료

### 18.1. 참고 자료 목록

| Document ID | 문서명 | 경로 또는 URL | 상태 |
|---|---|---|---|
| DOC-001 | 요구사항 정의서 | TBD | TBD |
| DOC-002 | IA | TBD | TBD |
| DOC-003 | 화면 설계서 | TBD | TBD |
| DOC-004 | ERD | TBD | TBD |
| DOC-005 | DDL | TBD | TBD |
| DOC-006 | API 명세서 | TBD | TBD |
| DOC-007 | 이벤트 스토밍 | TBD | TBD |
| DOC-008 | 기술 선정표 | TBD | TBD |
| DOC-009 | 테스트 명세서 | TBD | TBD |


### 18.2. 문서 참조 규칙

작업에 관련된 문서를 우선 확인한다.

참고 자료가 존재하지 않는 경우
존재한다고 가정하지 않는다.

접근할 수 없는 외부 문서의 내용을
임의로 추정하지 않는다.

문서가 누락되었다고 해서
항상 전체 개발 작업을 중단하지 않는다.

현재 작업에 필수적인 정보인지 판단한다.


### 18.3. 실제 소스 코드 참조

| 항목 | 경로 |
|---|---|
| Main Source | src/main/java |
| Test Source | src/test/java |
| Resources | src/main/resources |
| Build Configuration | build.gradle |
| Project Settings | settings.gradle |

위 경로는 일반적인 Gradle Java 프로젝트의
기본 예시이다.

실제 저장소의 구조를 우선 확인한다.


### 18.4. Context 용량 관리

대규모 요구사항 정의서, ERD, API 명세서를
context.md에 모두 중복 복사하지 않는다.

가능한 경우 다음 정보를 기록한다.

- 핵심 정보 요약
- 문서 ID
- 문서 경로
- 관련 도메인
- 문서 버전
- 승인 상태

에이전트는 현재 작업에 관련된
상세 자료만 추가로 확인한다.


## 19. 기술 결정 및 미확정 사항

### 19.1. Architecture Decision Record

| ADR ID | 결정 사항 | 결정 근거 | 상태 |
|---|---|---|---|
| ADR-001 | Java 17 사용 | 공통 개발 환경 | CONFIRMED |
| ADR-002 | Hexagonal Architecture | 기술 독립성 확보 | CONFIRMED |
| ADR-003 | SOLID 준수 | 객체지향 설계 품질 | CONFIRMED |
| ADR-004 | 도메인 중심 패키지 | 응집도 및 확장성 | CONFIRMED |
| ADR-005 | 시스템 아키텍처 | TBD | TBD |


### 19.2. 기술 결정 상세 템플릿

ADR ID: ADR-XXX

Title: TBD

Status: PROPOSED

Date: TBD

Context:

- TBD

Options:

1. TBD
2. TBD

Decision:

- TBD

Reason:

- TBD

Advantages:

- TBD

Disadvantages:

- TBD

Consequences:

- TBD

Approved By:

- TBD


### 19.3. 미확정 사항

| Issue ID | 항목 | 필요한 결정 | 영향 범위 | 상태 |
|---|---|---|---|---|
| OPEN-001 | 프로젝트명 | 프로젝트 식별 정보 | 초기 설정 | OPEN |
| OPEN-002 | 시스템 구조 | Monolith 또는 MSA 등 | 전체 구조 | OPEN |
| OPEN-003 | 도메인 | 핵심 기능 범위 | 요구사항 | OPEN |
| OPEN-004 | Spring Boot 버전 | 호환 버전 선정 | 빌드 환경 | OPEN |
| OPEN-005 | 데이터베이스 | 스키마 및 접속 정책 | 데이터 계층 | OPEN |
| OPEN-006 | 인증 방식 | 인증 및 인가 정책 | 보안 | OPEN |

Issue Status:

- OPEN
- IN_REVIEW
- RESOLVED
- DEFERRED


### 19.4. 가정 및 제안 기록

| Assumption ID | 가정 또는 제안 | 근거 | 영향 | 상태 |
|---|---|---|---|---|
| ASM-001 | TBD | TBD | TBD | PROPOSED |

가정과 확정된 요구사항을 구분한다.

가정이 사실과 다를 경우
관련 설계와 구현을 재검토한다.


## 20. Context 관리 및 갱신

### 20.1. 갱신 대상

다음 변경이 발생하면
관련 Context 갱신 필요 여부를 확인한다.

- 프로젝트 목표 변경
- 기능 요구사항 변경
- 비즈니스 정책 변경
- 데이터베이스 스키마 변경
- API 계약 변경
- 기술 스택 변경
- 보안 정책 변경
- 시스템 아키텍처 변경
- 새로운 설계 결정


### 20.2. 갱신 책임

Orchestrator:

- Context 변경 필요성 판단
- 변경 영향 확인
- 승인된 변경 내용 반영

Planner:

- 새로운 요구사항 및 설계 정보 제안
- 기존 Context와의 충돌 확인

Developer:

- 실제 구현과 Context 간 차이 보고
- 필요한 문서 변경 제안

Reviewer:

- 구현과 확정된 Context의 일치 여부 검토
- 문서 불일치 및 누락 보고

에이전트는 자신의 구현을 정당화하기 위해
기존 요구사항을 임의 변경하지 않는다.


### 20.3. 변경 이력

| Version | Date | Description | Status |
|---|---|---|---|
| 1.0.0 | TBD | 초기 Context 템플릿 작성 | INITIAL |


### 20.4. 작업 상태 관리

context.md는 프로젝트의 지속적인 정보와
승인된 요구사항을 관리한다.

다음 정보는 별도의 작업 상태 영역에서
관리하는 것을 원칙으로 한다.

- 현재 실행 단계
- 현재 Agent 실행 상태
- 수정 루프 횟수
- 일시적인 테스트 결과
- 개별 작업 로그
- 세션별 실행 기록

향후 자동화 루프 구축 시
별도의 State 파일을 사용한다.


### 20.5. 신규 프로젝트 적용 방법

신규 프로젝트를 시작할 때 다음을 수행한다.

1. 프로젝트 기본 정보 작성
2. 시스템 아키텍처 확인
3. 기술 스택 확정
4. 사용자 및 역할 정의
5. 도메인 정의
6. 비즈니스 정책 확인
7. 기능 요구사항 작성
8. 데이터베이스 정보 작성
9. API 계약 확인
10. 개발 환경 및 제약사항 확인
11. 테스트 및 인수 조건 정의
12. 참고 자료 연결

모든 항목을 한 번에 확정할 필요는 없다.

현재 작업에 필요한 필수 정보부터 확정한다.

적용 대상이 아닌 정보는
NOT_APPLICABLE로 표시한다.

확정되지 않은 정보는 TBD 상태로 유지한다.


### 20.6. 최종 운영 원칙

Context의 목적은 에이전트에게
가능한 많은 정보를 제공하는 것이 아니다.

현재 작업에 필요한 정확하고 승인된 정보를
적절한 시점에 제공하는 것이다.

모든 에이전트는 다음 원칙을 준수한다.

- 확인된 정보를 기반으로 작업한다.
- 가정과 사실을 구분한다.
- 미확정 정보를 임의 확정하지 않는다.
- 실제 구현과 문서의 차이를 보고한다.
- 불필요한 Context 중복을 방지한다.
- 작업에 관련된 문서만 추가 확인한다.
- 승인된 변경사항을 일관되게 반영한다.

하네스의 실행 및 완료 판단은
AGENTS.md와 harness/workflow.md를 따른다.
