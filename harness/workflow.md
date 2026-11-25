
# Harness Development Workflow

## 1. 목적 및 적용 범위

### 1.1. 목적

본 문서는 Codex CLI 기반 멀티 에이전트
Harness의 실행 절차를 정의한다.

다음 사항을 관리한다.

- 사용자 요구사항 분석
- 실제 Subagent 호출
- 에이전트 간 작업 위임
- 에이전트 산출물 전달
- 작업 의존성 관리
- 순차 및 병렬 개발
- 독립적인 코드 검토
- 반복적인 결함 수정
- 최종 품질 검증
- 작업 완료 판단
- 실행 결과 보고


### 1.2. 적용 대상

- Main Codex: Orchestrator
- Planner Agent
- Developer Agent
- Reviewer Agent


### 1.3. 관련 문서

- AGENTS.md
- harness/rules.md
- harness/context.md
- .codex/config.toml
- .codex/agents/planner.toml
- .codex/agents/developer.toml
- .codex/agents/reviewer.toml

본 문서는 AGENTS.md에서 정의한
운영 정책의 세부 실행 절차를 제공한다.

기술적인 개발 및 검증 기준은
harness/rules.md를 따른다.

프로젝트 요구사항 및 비즈니스 정책은
harness/context.md와 승인된 참고 문서를 따른다.

에이전트별 역할은 각 Agent TOML 설정을 따른다.

상위 운영 지침 및 시스템 권한을
본 문서로 임의 변경하지 않는다.


## 2. 기본 실행 정책

### 2.1. Workflow 단계

전체 실행 절차는 다음과 같다.

1. INIT
2. PLAN
3. IMPLEMENT
4. REVIEW
5. DECISION
6. VALIDATE
7. REPORT

일반 개발 작업에서 필수 단계를 생략하지 않는다.

실패 또는 차단으로 종료되는 경우에는
미실행 단계를 명시하고 REPORT로 이동한다.


### 2.2. 기본 에이전트 구성

Orchestrator:

- Main Codex Session

Planner:

- Custom Agent Name: planner

Developer:

- Custom Agent Name: developer

Reviewer:

- Custom Agent Name: reviewer

별도의 Orchestrator Subagent를 생성하지 않는다.

Main Codex가 전체 Workflow를 제어한다.


### 2.3. 기본 실행 제한

- 최초 구현 라운드: 1회
- 최대 수정 라운드: 5회
- 최대 구현 및 수정 라운드: 6회
- 최대 동시 열린 Subagent: 설정값 기준 4개
- 하위 에이전트의 독립적인 재귀 호출: 금지

동시 Subagent 제한은 실제 Codex 설정과
세션의 유효 설정을 기준으로 한다.

설정값을 임의로 초과하지 않는다.


### 2.4. 실행 모드

SEQUENTIAL:

- 순차 개발
- 작업 간 의존성이 있는 경우 사용

PARALLEL:

- 독립적인 작업의 병렬 개발
- 작업 범위 및 파일 소유권이 분리된 경우 사용

SMOKE_TEST:

- 하네스 등록 및 동작 검증
- 별도로 지정한 테스트 범위에서 실행
- 일반 애플리케이션 개발과 구분

기본 실행 모드는 SEQUENTIAL이다.

병렬 개발이 안전하고 유효한 경우
Orchestrator가 PARALLEL을 선택할 수 있다.


## 3. Harness 실행 조건

### 3.1. 필수 실행 대상

다음 작업에 본 Workflow를 적용한다.

- 프로젝트 초기 설정
- 신규 프로젝트 생성
- 프로젝트 스캐폴딩
- 신규 기능 구현
- 기존 기능 수정
- API 구현
- 도메인 모델 구현
- 데이터베이스 변경
- 의존성 변경
- 버그 수정
- 리팩터링
- 테스트 코드 작성 및 변경
- 보안 기능 변경
- 성능 개선
- 빌드 설정 변경

세부 실행 조건은 AGENTS.md 제5절을 따른다.


### 3.2. 실행 제외 대상

다음 작업은 전체 Workflow를 생략할 수 있다.

- 단순 질문
- 기술 설명
- 코드 설명
- 파일 조회
- 프로젝트 구조 조회
- 구현을 수반하지 않는 기술 조사
- 하네스 자체의 초기 구축

하네스 등록 및 동작 테스트는
명시된 SMOKE_TEST 절차에 따라 수행할 수 있다.

일반 개발 작업에 SMOKE_TEST 예외를
임의로 적용하지 않는다.


### 3.3. 실행 생략 금지

다음 표현을 실행 생략의 근거로 사용하지 않는다.

- 간단하게 구현해줘
- 알아서 개발해줘
- 기본 설정해줘
- 권장안으로 진행해줘
- 빠르게 수정해줘

사용자가 구현 세부사항을 위임하더라도
필수 에이전트 호출 및 품질 검증은 수행한다.


## 4. Orchestrator 운영 규칙

### 4.1. 기본 책임

Orchestrator는 다음 사항을 수행한다.

- 사용자 요청 분류
- 프로젝트 지침 확인
- 실행 환경 확인
- 실제 Subagent 호출
- 작업 범위 및 의존성 관리
- 에이전트 결과 수집
- 산출물 전달
- 수정 라운드 관리
- 병렬 실행 제어
- 최종 검증
- 작업 완료 판단
- 최종 결과 보고


### 4.2. 실제 Agent 호출

다음 역할의 Custom Agent를
실제 Codex Subagent 기능으로 호출한다.

- planner
- developer
- reviewer

Agent 호출 시 해당 역할 이름을 명시한다.

Custom Agent를 호출할 수 없는 경우
기본 Agent로 임의 대체하지 않는다.

호출 기능을 사용할 수 없는 경우
BLOCKED 상태로 보고한다.

Main Codex가 역할을 전환하는 것으로
실제 Subagent 호출을 대체하지 않는다.


### 4.3. 호출 후 처리

각 Subagent 호출에 대해 다음을 수행한다.

1. Agent 생성 여부 확인
2. 작업 위임
3. 실행 결과 대기
4. 작업 결과 수집
5. 산출물 검증
6. 다음 단계 결정
7. 불필요한 Agent 스레드 종료

작업 결과가 반환되기 전에
다음 의존 작업을 시작하지 않는다.

병렬 실행에서는 독립적인 작업을 먼저 호출하고
모든 필수 결과를 수집한 후 통합한다.


### 4.4. Agent 실행 증거

가능한 경우 다음 정보를 기록한다.

- Agent Role
- Agent Name
- Agent Identifier
- Task ID
- Execution Phase
- Execution Mode
- Returned Status
- Returned Result
- Start 및 End 정보
- 실행 중단 또는 종료 사유

도구가 제공하지 않는 식별자나 시간을
임의로 생성하지 않는다.

확인할 수 없는 정보는 UNAVAILABLE로 표시한다.

실제 호출 활동과 반환 결과가 없는 경우
해당 Agent 실행을 성공으로 판단하지 않는다.


### 4.5. 역할 제한

Orchestrator는 일반 개발 작업에서
Developer의 소스 코드 구현을 대신하지 않는다.

다만 다음 제어 업무는 직접 수행할 수 있다.

- 프로젝트 및 파일 조회
- Git 상태 확인
- Agent 결과 검증
- 빌드 및 테스트 실행
- 작업 상태 관리
- 승인된 문서 변경 관리
- 최종 결과 보고

소스 코드 수정이 필요하면
Developer에게 작업을 위임한다.

에이전트별 역할 제한은
실제 샌드박스 권한과 구분한다.


## 5. 작업 상태 관리

### 5.1. 내부 실행 상태

현재 작업에 대해 다음 정보를 관리한다.

- Task ID
- User Requirement
- Current Phase
- Execution Mode
- Revision Count
- Max Revisions
- Planner Status
- Developer Status
- Reviewer Decision
- Validation Status
- Agent Execution Records
- Changed Files
- Review Findings
- Remaining Issues

STEP 6에서는 위 정보를
Orchestrator의 현재 실행 문맥에서 관리한다.

별도의 State 파일 생성을 필수로 요구하지 않는다.

향후 외부 자동화 루프 구축 시
독립적인 상태 저장 방식을 적용할 수 있다.


### 5.2. 초기 상태

작업 시작 시 다음 상태를 설정한다.

- Current Phase: INIT
- Revision Count: 0
- Max Revisions: 5
- Planner Status: NOT_RUN
- Developer Status: NOT_RUN
- Reviewer Decision: NOT_RUN
- Validation Status: NOT_RUN

최초 구현은 수정 횟수에 포함하지 않는다.


### 5.3. Planner 상태

- READY
- BLOCKED

READY:

- 구현에 필요한 계획 수립 완료

BLOCKED:

- 필수 요구사항 또는 환경 부족


### 5.4. Developer 상태

- IMPLEMENTED
- FAILED
- BLOCKED

IMPLEMENTED:

- 할당된 작업 구현 완료
- 적용 대상 자체 검증 통과
- 독립 리뷰 대기

FAILED:

- 구현 또는 자체 검증 실패

BLOCKED:

- 필수 환경, 권한 또는 정보 부족


### 5.5. Reviewer 상태

- PASS
- FAIL
- BLOCKED

PASS:

- 현재 검토 범위의 필수 기준 충족

FAIL:

- 수정이 필요한 차단 결함 존재

BLOCKED:

- 필수 검증 수행 불가


### 5.6. 검증 항목 상태

- PASS
- FAIL
- BLOCKED
- NOT_RUN
- NOT_CONFIGURED
- NOT_APPLICABLE

NOT_RUN:

- 검증을 실행하지 않음

NOT_CONFIGURED:

- 해당 검증 도구가 설정되지 않음

NOT_APPLICABLE:

- 현재 작업에 적용되지 않는 검증 항목

필수 검증 항목의 미실행은
성공으로 간주하지 않는다.


### 5.7. 최종 작업 상태

- COMPLETED
- FAILED
- BLOCKED

중간 상태와 최종 작업 상태를 혼동하지 않는다.

Developer의 IMPLEMENTED 및
Reviewer의 PASS만으로
전체 작업을 COMPLETED 처리하지 않는다.


## 6. PHASE 1 - INIT

### 6.1. 목적

사용자 요구사항과 프로젝트 상태를 확인하고
하네스 실행에 필요한 전제조건을 검증한다.


### 6.2. 입력

- 사용자 요청
- AGENTS.md
- harness/rules.md
- harness/context.md
- harness/workflow.md
- Codex 설정
- Agent 설정
- 실제 프로젝트 파일


### 6.3. 수행 주체

- Orchestrator


### 6.4. 실행 절차

1. 사용자 요청의 개발 성격을 확인한다.

2. 하네스 적용 대상인지 판단한다.

3. 현재 작업 디렉터리를 확인한다.

4. Git 저장소 및 변경 상태를 확인한다.

5. 필수 하네스 문서의 존재 및 내용을 확인한다.

6. Custom Agent 설정을 확인한다.

7. 실제 Subagent 호출 기능의 사용 가능성을
   확인 가능한 범위에서 점검한다.

8. 현재 프로젝트의 기술 스택을 확인한다.

9. 승인된 요구사항과 관련 문서를 확인한다.

10. 사용자 기존 변경사항을 식별한다.

11. 작업 범위 및 제외 범위를 설정한다.

12. 필수 환경과 권한을 확인한다.

13. 초기 작업 상태를 설정한다.


### 6.5. Git 상태 확인

필요한 경우 다음 명령어를 사용한다.

    git status --short
    git branch --show-current
    git diff --stat
    git diff
    git diff --cached
    git ls-files --others --exclude-standard

현재 작업 시작 전에 존재했던 변경사항을
이번 작업의 결과와 구분한다.

기존 사용자 변경사항을 임의로 제거하지 않는다.

Git 저장소 초기화 자체가 요청된 경우에는
저장소가 아직 없다는 이유만으로 차단하지 않는다.


### 6.6. 필수 문서 확인

다음 파일을 확인한다.

- AGENTS.md
- harness/rules.md
- harness/context.md
- harness/workflow.md
- .codex/config.toml
- .codex/agents/planner.toml
- .codex/agents/developer.toml
- .codex/agents/reviewer.toml

누락되거나 비어 있는 필수 파일이 존재하면
일반 개발 하네스 실행을 시작하지 않는다.

현재 작업에 관계없는 Context의 TBD 항목은
자동으로 차단 조건으로 취급하지 않는다.


### 6.7. 신규 프로젝트 예외

사용자 요청이 프로젝트 초기 설정인 경우
다음 파일이 아직 존재하지 않을 수 있다.

- build.gradle
- settings.gradle
- gradlew
- src/main/java
- src/test/java

생성 자체가 사용자 요청의 범위에 포함된다면
이를 필수 환경 누락으로 간주하지 않는다.

Planner가 필요한 초기 구조를 설계하고
Developer가 해당 구조를 생성하도록 한다.


### 6.8. 산출물

## Init Result

- Task ID:
- User Requirement:
- Harness Required:
- Working Directory:
- Git Status:
- Existing Changes:
- Required Documents:
- Agent Availability:
- Confirmed Context:
- Missing Context:
- Initial Scope:
- Initial Blockers:
- Revision Count: 0


### 6.9. 종료 조건

다음 조건을 충족하면 PLAN으로 이동한다.

- 사용자 요청 확인
- 필수 운영 지침 확인
- 작업 범위 확인
- 필수 하네스 설정 확인
- 해결되지 않은 초기 차단 조건 없음

필수 환경 또는 정보가 부족하면
해결 가능 여부를 판단한다.

해결할 수 없는 경우
BLOCKED 상태로 REPORT 단계에 이동한다.


## 7. PHASE 2 - PLAN

### 7.1. 목적

사용자 요구사항을 분석하고
구현 가능한 개발 계획을 수립한다.


### 7.2. 수행 주체

- Planner Agent

Orchestrator는 planner Custom Agent를
실제로 호출해야 한다.


### 7.3. Planner 전달 정보

- Task ID
- User Requirement
- Init Result
- Project Context
- Related Documents
- Technical Rules
- Initial Scope
- Known Constraints
- Existing Changes

Planner는 관련 원본 파일을 직접 확인할 수 있다.


### 7.4. 호출 지침

다음 내용을 Planner에게 전달한다.

당신은 현재 작업의 Planner Agent이다.

사용자 요구사항과 프로젝트 지침을 확인하고
실행 가능한 개발 계획을 작성하라.

Hexagonal Architecture와 SOLID를 준수하라.

작업 단위, 선행 의존성, 수정 가능 파일,
병렬 실행 가능 여부, 테스트 계획 및
인수 조건을 구체적으로 정의하라.

애플리케이션 코드와 테스트 코드를
직접 수정하지 마라.

planner.toml의 Output Contract에 따라
READY 또는 BLOCKED를 반환하라.


### 7.5. Planner 산출물 확인

Orchestrator는 다음 항목을 확인한다.

- 사용자 요구사항 반영 여부
- In Scope 및 Out of Scope
- 확정 정책 반영 여부
- 미확정 정책 처리 방식
- 아키텍처 설계
- API 설계
- 데이터베이스 변경 범위
- 작업 단위별 Task ID
- Allowed Files
- 선행 의존성
- 병렬 실행 가능 여부
- 테스트 계획
- 인수 조건
- 기술적 위험 및 차단 조건

계획이 불완전하면 Planner에게 보완을 요청한다.

필수 정보 부족으로 계획을 수립할 수 없는 경우
BLOCKED로 처리한다.


### 7.6. 작업 범위 검증

Planner의 개발 계획이 사용자 요청을
불필요하게 확대하지 않았는지 확인한다.

다음 변경은 명시적인 요구 또는 승인 여부를 확인한다.

- 기술 스택 변경
- 시스템 아키텍처 변경
- 데이터베이스 파괴적 변경
- 인증 및 인가 정책 변경
- 기존 API 계약 변경
- 외부 유료 서비스 도입

Orchestrator는 근거 없이
새로운 비즈니스 요구사항을 추가하지 않는다.


### 7.7. 실행 모드 결정

Planner 결과에 따라 실행 모드를 결정한다.

SEQUENTIAL:

- 선행 의존성이 존재하는 경우
- 파일 수정 범위가 중복되는 경우
- 공통 설정 변경이 필요한 경우

PARALLEL:

- 작업이 서로 독립적인 경우
- 파일 수정 범위가 분리된 경우
- 공통 계약이 확정된 경우
- 병렬 실행에 필요한 자원이 확보된 경우

실행 모드 판단 근거를 기록한다.


### 7.8. 종료 조건

Planner의 READY 결과를 실제로 수집하고
필수 계획 항목을 확인하면 IMPLEMENT로 이동한다.

Planner가 BLOCKED를 반환한 경우
차단 원인 해결을 먼저 시도한다.

해결할 수 없는 경우 REPORT로 이동한다.


## 8. PHASE 3 - IMPLEMENT

### 8.1. 목적

Planner의 개발 계획에 따라
실제 코드를 구현하고 필요한 테스트를 작성한다.


### 8.2. 수행 주체

- Developer Agent

Orchestrator는 developer Custom Agent를
실제로 호출해야 한다.


### 8.3. Developer 전달 정보

- Task ID
- User Requirement
- Planner Result
- Task Objective
- Allowed Files
- Dependencies
- Acceptance Criteria
- Required Tests
- Execution Mode
- Relevant Context
- Existing Changes

Execution Mode:

- IMPLEMENT
- FIX
- SMOKE_TEST

일반 최초 개발에서는 IMPLEMENT를 사용한다.


### 8.4. 호출 지침

다음 내용을 Developer에게 전달한다.

당신은 현재 작업의 Developer Agent이다.

전달된 Planner Result와 작업 범위를 확인하라.

harness/rules.md 및 승인된 프로젝트
요구사항에 따라 기능을 구현하라.

Allowed Files 범위에서만 작업하라.

필요한 테스트 코드를 작성하고
적용 대상 검증 명령어를 실행하라.

기존 사용자 변경사항을 보호하라.

developer.toml의 Output Contract에 따라
IMPLEMENTED, FAILED 또는 BLOCKED를 반환하라.


### 8.5. 순차 실행

작업 간 의존성이 존재하는 경우
선행 작업부터 순서대로 호출한다.

예시:

    TASK-001: 프로젝트 기본 설정
    TASK-002: Domain Model 구현
    TASK-003: Application Port 구현
    TASK-004: Application Service 구현
    TASK-005: Adapter 구현
    TASK-006: 통합 테스트 작성

위 순서는 설명용 예시이다.

실제 실행 순서는 Planner의 의존성 분석에 따른다.

선행 작업이 완료되지 않은 상태에서
의존 작업을 시작하지 않는다.


### 8.6. 병렬 실행

독립적인 작업은 여러 Developer 인스턴스에
동시에 위임할 수 있다.

실행 절차:

1. 병렬 작업 그룹을 확인한다.
2. 작업별 Allowed Files를 확인한다.
3. 선행 작업 완료 여부를 확인한다.
4. 공유 자원 충돌 여부를 확인한다.
5. 가용 Agent 스레드 수를 확인한다.
6. 독립적인 Developer 인스턴스를 호출한다.
7. 모든 작업 결과를 기다린다.
8. 각 작업의 결과와 변경 범위를 확인한다.
9. 통합 상태를 확인한다.
10. 필요한 후속 작업을 실행한다.

병렬 작업을 순차 호출한 경우
실제 병렬 실행이라고 보고하지 않는다.


### 8.7. 파일 소유권

각 Developer에게 수정 가능한
파일 또는 디렉터리를 명시적으로 할당한다.

다음 파일은 공통 자원으로 취급한다.

- build.gradle
- settings.gradle
- Gradle Wrapper
- application.yml
- 공통 Security 설정
- 공통 인터페이스
- 데이터베이스 마이그레이션
- 공통 예외 처리 코드
- AGENTS.md
- harness 설정
- .codex 설정

공통 자원을 여러 Developer가
동시에 수정하지 않는다.

새로운 공통 변경이 필요한 경우
Orchestrator가 작업 순서를 조정한다.


### 8.8. Developer 결과 확인

다음 산출물을 수집한다.

- Developer Result
- 구현된 기능
- 변경 파일 목록
- 실행된 테스트
- 컴파일 및 테스트 결과
- 실패 원인
- 남은 문제
- 작업 범위 준수 여부

Developer가 보고한 변경 사항은
가능한 범위에서 실제 파일과 비교한다.

신규 생성 파일도 검토 대상에 포함한다.


### 8.9. Developer 결과 분기

IMPLEMENTED:

- 할당된 작업 결과를 수집한다.
- 모든 필수 구현 작업이 완료되면 REVIEW로 이동한다.

FAILED:

- 실패 원인을 수집한다.
- 독립적인 Reviewer에게 실패한 구현 상태를
  검토하도록 위임한다.
- 필요한 경우 DECISION을 통해 수정 루프를 실행한다.

BLOCKED:

- 필수 환경, 정보 또는 권한 문제를 확인한다.
- 해결 가능한 차단 조건은 해결 후 재개한다.
- 해결 불가능한 경우 REPORT로 이동한다.

FAILED 상태를 IMPLEMENTED로 변경하여
강제로 다음 단계로 이동하지 않는다.


## 9. PHASE 4 - REVIEW

### 9.1. 목적

Developer의 실제 구현 결과를 독립적으로 검토하고
차단 결함과 선택적인 개선 제안을 구분한다.


### 9.2. 수행 주체

- Reviewer Agent

Orchestrator는 reviewer Custom Agent를
실제로 호출해야 한다.

Reviewer는 Developer와 독립적인
Subagent 인스턴스로 실행한다.


### 9.3. Reviewer 전달 정보

- Task ID
- User Requirement
- Planner Result
- Developer Result
- Allowed Files
- Acceptance Criteria
- Required Tests
- Changed Files
- Git Change Information
- Previous Review Findings
- Verification Evidence
- Revision Count

Reviewer는 Developer 보고서뿐 아니라
실제 변경 파일을 직접 확인한다.


### 9.4. 호출 지침

다음 내용을 Reviewer에게 전달한다.

당신은 현재 작업의 독립 Reviewer Agent이다.

사용자 요구사항, Planner Result,
Developer Result 및 실제 변경 파일을 확인하라.

harness/rules.md를 기준으로
Hexagonal Architecture, SOLID,
기능 요구사항, 보안, 데이터 무결성,
테스트 및 회귀 위험을 검토하라.

필수 규칙 위반과 실제 결함을 식별하라.

선택적인 개선 제안은
차단 결함과 구분하라.

애플리케이션 코드 및 테스트 코드를
직접 수정하지 마라.

reviewer.toml의 Output Contract에 따라
PASS, FAIL 또는 BLOCKED를 반환하라.


### 9.5. 리뷰 범위

필수 검토 대상:

- 사용자 요구사항
- Planner의 인수 조건
- 실제 변경 코드
- 신규 생성 파일
- 관련 기존 코드
- 기술적인 개발 규칙
- 보안 및 데이터 처리
- 테스트 코드
- 실행된 검증 결과

현재 작업과 관련 없는 기존 결함은
별도로 구분하여 보고한다.


### 9.6. 리뷰 검증 기준

Reviewer는 다음 항목을 확인한다.

- 요구사항 누락 여부
- Hexagonal Architecture 준수 여부
- SOLID 원칙 준수 여부
- Domain 기술 독립성
- Port 및 Adapter 계약 준수
- 인증 및 인가
- 트랜잭션 경계
- 데이터 무결성
- 동시성 및 멱등성
- 예외 처리
- 테스트 누락 및 실패
- 기존 기능 회귀
- 변경 범위 준수

구체적인 기준은 harness/rules.md를 따른다.


### 9.7. Finding 구조

각 발견 사항은 다음 정보를 포함한다.

- Finding ID
- Type
- Severity
- File
- Line
- Rule ID
- Description
- Evidence
- Impact
- Recommendation
- Blocking
- Status

Type:

- DEFECT
- SUGGESTION
- UNVERIFIED

Severity:

- CRITICAL
- HIGH
- MEDIUM
- LOW

Status:

- OPEN
- RESOLVED

실제 결함과 선택적인 개선 제안을 구분한다.

파일 및 라인 정보를 확인할 수 없는 경우
확인할 수 없다는 사실을 명시한다.


### 9.8. 리뷰 결과 검증

Orchestrator는 Reviewer Result를 수집한 후
다음 사항을 확인한다.

- Decision 존재 여부
- 검토 범위의 적절성
- 결함의 구체적인 근거
- 필수 수정 항목
- 미검증 항목
- 테스트 증거
- 이전 결함의 해결 여부

PASS와 차단 결함이 동시에 존재하는 등
결과가 모순되는 경우 재검토를 요청한다.

Reviewer의 근거를 확인하지 않고
임의로 PASS 또는 FAIL을 변경하지 않는다.


### 9.9. 종료 조건

유효한 Reviewer Result를 수집하면
DECISION 단계로 이동한다.


## 10. PHASE 5 - DECISION

### 10.1. 목적

Reviewer의 판단 결과를 기반으로
다음 단계 및 수정 작업 수행 여부를 결정한다.


### 10.2. 수행 주체

- Orchestrator


### 10.3. PASS 처리

다음 조건을 확인한다.

- 필수 구현 작업 완료
- Reviewer PASS
- 차단 결함 없음
- 필수 인수 조건에 대한 검토 근거 확보
- 현재 리뷰 단계의 필수 검증 완료

모든 조건을 충족하면
VALIDATE 단계로 이동한다.

Reviewer PASS를 전체 하네스 완료와
혼동하지 않는다.


### 10.4. FAIL 처리

Reviewer가 FAIL을 반환하면
다음 작업을 수행한다.

1. 차단 결함 목록 수집
2. Finding ID 및 Rule ID 확인
3. 실제 수정 필요 범위 확인
4. 남은 수정 라운드 확인
5. 수정 작업 단위 구성
6. Developer에게 FIX 모드로 위임
7. 수정 완료 후 Reviewer 재호출

수정 라운드가 남아 있으면
IMPLEMENT 단계로 돌아간다.

수정 한도에 도달하면
FAILED 상태로 REPORT 단계에 이동한다.


### 10.5. BLOCKED 처리

Reviewer가 BLOCKED를 반환하면
차단 조건을 확인한다.

예시:

- 필수 테스트 환경 부족
- 데이터베이스 접속 불가
- 외부 의존성 접근 불가
- 필요한 정보 부족
- 필수 검증 명령어 실행 불가
- 권한 제한
- Agent 실행 중단

안전하고 승인된 범위에서 해결 가능한 경우
차단 원인을 해결한다.

해결 후 해당 검증 단계를 다시 실행한다.

해결할 수 없는 경우
BLOCKED 상태로 REPORT 단계에 이동한다.


### 10.6. 선택적인 개선 제안

다음 조건을 충족하는 개선 제안은
자동 수정 루프의 필수 대상으로 삼지 않는다.

- 필수 규칙을 위반하지 않음
- 요구사항을 위반하지 않음
- 실제 기능 결함이 아님
- 차단 결함이 아님

필요한 경우 최종 보고에
Optional Improvements로 기록한다.


### 10.7. 리뷰 결과 충돌

Developer가 Reviewer 지적에 이의를 제기한 경우
다음 절차를 수행한다.

1. Developer의 반론 근거 확인
2. Reviewer의 결함 증거 확인
3. 관련 실제 코드 확인
4. 적용되는 Rule ID 및 요구사항 확인
5. Reviewer에게 재검토 요청
6. 갱신된 Reviewer Decision 수집

Orchestrator는 필요한 검증 없이
차단 결함을 임의로 무효화하지 않는다.

해결할 수 없는 필수 검증 논쟁은
BLOCKED 또는 FAILED로 명확하게 보고한다.


## 11. 수정 및 재검증 루프

### 11.1. 수정 루프 실행 조건

다음 상황에서 수정 루프를 실행한다.

- Developer 구현 실패에 대한 검토 결과 FAIL
- Reviewer 검토 FAIL
- 최종 컴파일 FAIL
- 최종 테스트 FAIL
- 최종 빌드 FAIL
- 필수 품질 검사 FAIL
- 최종 검증 과정에서 발견된 차단 결함

실패 원인이 환경 또는 권한 부족이라면
무의미한 코드 수정 대신 BLOCKED 처리를 검토한다.


### 11.2. 최대 수정 횟수

최초 구현 이후 최대 5회의
수정 라운드를 허용한다.

초기값:

    revisionCount = 0
    maxRevisions = 5

각 수정 라운드를 실제 시작하기 전에
다음 조건을 확인한다.

    revisionCount < maxRevisions

조건을 충족하면 수정 횟수를 1 증가시킨다.

    revisionCount = revisionCount + 1

증가된 수정 횟수를 Developer 호출 정보에 포함한다.

한 수정 라운드에는 여러 관련 결함에 대한
Developer 수정 작업이 포함될 수 있다.

병렬로 수행되는 수정 작업도
동일한 수정 라운드에 포함할 수 있다.

단, 새로운 수정 라운드를 시작하면서
기존 라운드 번호를 재사용하지 않는다.


### 11.3. 수정 횟수 공유

리뷰 실패와 최종 검증 실패는
동일한 수정 횟수를 공유한다.

예시:

- 초기 구현: Revision 0
- 리뷰 실패 수정: Revision 1
- 리뷰 실패 수정: Revision 2
- 최종 테스트 실패 수정: Revision 3
- 리뷰 실패 수정: Revision 4
- 최종 빌드 실패 수정: Revision 5

위 상태에서 추가 수정이 필요하면
FAILED로 종료한다.

새로운 Agent를 호출하거나
실행 단계를 변경한다는 이유로
수정 횟수를 초기화하지 않는다.


### 11.4. 수정 요청 전달 정보

Developer에게 다음 정보를 전달한다.

- Task ID
- Execution Mode: FIX
- Revision Count
- User Requirement
- Planner Result
- Previous Developer Result
- Reviewer Result
- Required Fixes
- Finding IDs
- Rule IDs
- Evidence
- Allowed Files
- Required Regression Tests
- Previous Validation Failures

Developer는 확인된 수정 범위 내에서 작업한다.

문제 해결을 위해 설계 변경이 필요한 경우
Planner의 재검토를 요청할 수 있다.

재설계 과정에서 수정 횟수를 초기화하지 않는다.


### 11.5. 수정 호출 지침

Developer에게 다음 내용을 전달한다.

당신은 현재 작업의 Developer Agent이다.

Execution Mode는 FIX이다.

전달된 리뷰 결함과 실패 증거를 확인하고
실제 코드에서 원인을 검증하라.

확인된 차단 결함을 수정하라.

기존 요구사항과 정상 기능을 유지하라.

필요한 회귀 테스트를 작성하고 실행하라.

선택적인 개선 제안으로 작업 범위를
불필요하게 확대하지 마라.

수정 내용과 검증 결과를 반환하라.


### 11.6. 재검토

수정 작업이 완료되면
Reviewer를 다시 호출한다.

Reviewer에게 다음 정보를 추가 전달한다.

- Previous Review Findings
- Current Developer Fix Result
- Revision Count
- Updated Changed Files
- Regression Test Results

Reviewer는 다음 항목을 확인한다.

- 이전 결함 해결 여부
- 수정 코드의 적절성
- 회귀 문제 발생 여부
- 신규 결함 존재 여부
- 필수 테스트 결과

해결된 결함은 RESOLVED로 표시한다.

해결되지 않은 결함은 OPEN으로 유지한다.

새로운 결함에는 새로운 Finding ID를 부여한다.


### 11.7. 동일 결함 반복 처리

동일한 결함이 반복되는 경우
다음 사항을 확인한다.

- 실제 변경 여부
- 수정 접근 방식의 적절성
- 기존 실패 원인의 재발 여부
- 관련 테스트 보완 여부
- 새로운 해결 전략 존재 여부

동일한 수정 방법을
근거 없이 반복하지 않는다.

반복 실행에도 진전이 없거나
실질적인 해결 전략이 없는 경우에는
최대 횟수 이전에 FAILED로 종료할 수 있다.


### 11.8. 수정 루프 조기 종료

다음 상황에서는 수정 루프를 종료할 수 있다.

- 최대 수정 횟수 도달
- 해결되지 않는 동일 결함 반복
- 필수 환경 사용 불가
- 사용자 승인 필요
- 작업 범위 외 변경 필요
- 보안 정책 또는 권한 제한
- Agent 호출 기능 사용 불가
- 사용자에 의한 작업 중단

실제 결함이 해결되지 않았다면 FAILED로 처리한다.

필수 환경, 승인 또는 정보 부족으로
작업을 진행할 수 없다면 BLOCKED로 처리한다.


### 11.9. 제어 흐름 의사 코드

다음은 논리적인 실행 흐름이다.

    revisionCount = 0
    maxRevisions = 5

    execute INIT
    execute PLAN

    if INIT or PLAN is BLOCKED:
        execute REPORT
        stop

    execute initial IMPLEMENT

    while task is active:

        if implementation is BLOCKED:
            resolve blocker or stop as BLOCKED

        execute REVIEW
        execute DECISION

        if review is BLOCKED:
            resolve blocker or stop as BLOCKED

        if review is PASS:
            execute VALIDATE

            if all required validations PASS:
                status = COMPLETED
                break

            if validation is BLOCKED:
                resolve blocker or stop as BLOCKED

        collect actionable blocking failures

        if revisionCount >= maxRevisions:
            status = FAILED
            break

        if no meaningful repair is available:
            status = FAILED or BLOCKED
            break

        revisionCount += 1

        execute IMPLEMENT in FIX mode

    execute REPORT

위 내용은 실행 가능한 프로그램이 아니다.

실제 Codex Agent 호출 및 상태 판단을 위한
논리적인 제어 절차이다.


## 12. PHASE 6 - VALIDATE

### 12.1. 목적

모든 구현 및 수정 작업이 완료된 상태에서
전체 프로젝트의 품질을 최종 검증한다.


### 12.2. 수행 주체

- Orchestrator

필요한 경우 Reviewer 또는 관련 Agent의
검증 결과를 참고한다.

최종 검증 책임은 Orchestrator에게 있다.


### 12.3. 검증 전 확인

- 모든 필수 구현 작업 완료
- Reviewer PASS 확보
- 실행 중인 변경 작업 없음
- 병렬 작업 결과 통합 완료
- 필수 테스트 환경 확인
- 실제 빌드 도구 확인
- 기존 사용자 변경사항 확인

검증 도중 다른 Developer가
동시에 코드를 수정하지 않도록 한다.


### 12.4. 컴파일 검증

Gradle Wrapper가 존재하는 경우
다음 명령어를 사용한다.

Unix 또는 Git Bash:

    ./gradlew compileJava

Windows PowerShell 또는 CMD:

    .\gradlew.bat compileJava

실제 프로젝트에 해당 태스크가 존재하는지 확인한다.

멀티 모듈 프로젝트는 적용 대상 모듈과
최종 검증 범위를 확인한다.

실행 결과와 종료 코드를 기록한다.


### 12.5. 테스트 검증

기본 명령어:

    ./gradlew test

프로젝트에 별도의 통합 테스트 태스크가
존재하는 경우 필요한 테스트를 추가 실행한다.

검증 대상:

- Domain Unit Test
- Application Unit Test
- Web Adapter Test
- Persistence Test
- Integration Test
- Regression Test
- Security Test
- Concurrency Test

실제 작업 범위와 인수 조건을 기준으로
필수 테스트 유형을 결정한다.

테스트가 존재하지 않거나 실행되지 않았는데
성공한 것으로 보고하지 않는다.


### 12.6. 빌드 검증

기본 명령어:

    ./gradlew build

Windows:

    .\gradlew.bat build

실제 프로젝트의 빌드 구성에 따라
적절한 명령어를 사용한다.

빌드 과정에서 필수 검증이 수행되는지 확인한다.

테스트 제외 옵션 등을 사용하여
실패한 검증을 숨기지 않는다.


### 12.7. 추가 품질 검증

프로젝트에 설정되어 있는 경우
다음 검증을 수행한다.

- Checkstyle
- SpotBugs
- PMD
- JaCoCo
- ArchUnit

존재하지 않는 태스크를
임의로 실행하거나 성공으로 보고하지 않는다.

필수 도구가 설치되지 않은 경우 BLOCKED로 처리한다.

선택적 도구가 설치되지 않은 경우
NOT_CONFIGURED로 표시하고
가능한 대체 검토를 기록한다.


### 12.8. 아키텍처 검증

다음 항목을 확인한다.

- Domain의 기술 독립성
- Application의 Adapter 의존 여부
- Controller의 Repository 직접 호출 여부
- Port 및 Adapter 구조
- 모듈 간 순환 의존 여부
- SOLID 준수 여부

Reviewer의 검토 근거를 확인한다.

ArchUnit이 존재하는 경우
설정된 아키텍처 테스트를 실행한다.

ArchUnit이 없으면
수동 검토 결과와 자동 테스트 미설정 상태를
서로 구분하여 기록한다.


### 12.9. 변경 범위 검증

최종 변경 내용을 확인한다.

필요한 경우 다음 명령어를 사용한다.

    git status --short
    git diff --stat
    git diff
    git diff --cached
    git ls-files --others --exclude-standard

다음 사항을 검토한다.

- 요청 범위 준수 여부
- 불필요한 파일 변경 여부
- 신규 파일의 적절성
- 기존 사용자 변경사항 보호 여부
- 의도하지 않은 공통 설정 변경 여부
- 민감정보 포함 여부

기존 사용자 변경사항을
이번 작업의 결함으로 임의 처리하지 않는다.


### 12.10. 검증 결과 기록

각 검증에 대해 다음 정보를 기록한다.

- Validation Name
- Command
- Working Directory
- Executed
- Exit Code
- Result
- Evidence
- Required
- Failure Reason

확인되지 않은 종료 코드와 테스트 개수를
임의로 생성하지 않는다.


### 12.11. 최종 검증 실패 처리

필수 검증에 실패하면
다음 사항을 확인한다.

- 코드 결함 여부
- 테스트 코드 결함 여부
- 환경 설정 문제 여부
- 외부 의존성 문제 여부
- 권한 문제 여부
- 기존 프로젝트의 선행 실패 여부

실제 수정 가능한 결함이 발견되면
DECISION을 통해 동일한 수정 루프를 실행한다.

Developer 수정 후에는
Reviewer의 재검토를 반드시 수행한다.

검증 대상 코드 또는 관련 설정이 변경되면
이전 Reviewer PASS를 그대로 재사용하지 않는다.

환경상 필수 검증이 불가능한 경우
BLOCKED로 보고한다.


## 13. Quality Gate

### 13.1. 필수 통과 조건

최종 COMPLETED 판단을 위해
다음 조건을 모두 확인한다.

1. 사용자 요구사항 충족

2. Planner 실제 호출 및 READY 확보

3. Developer 실제 호출 및 구현 완료

4. Reviewer 실제 호출 및 최종 PASS 확보

5. 모든 차단 결함 해결

6. 필수 인수 조건 충족

7. 컴파일 성공

8. 필수 테스트 성공

9. 최종 빌드 성공

10. 적용 대상 필수 품질 검사 통과

11. Hexagonal Architecture 및 SOLID 준수

12. 작업 범위 준수

13. 기존 사용자 변경사항 보호


### 13.2. Quality Gate 판단

다음 조건이 모두 충족되어야 한다.

    plannerReady == true
    developerCompleted == true
    reviewerPassed == true
    requiredTestsPassed == true
    compilePassed == true
    buildPassed == true
    acceptanceCriteriaPassed == true
    architecturePassed == true
    scopeValidated == true
    blockingFindings == 0

선택적 검증 도구의 미설정은
필수 기준과 구분한다.

위 조건은 논리적 판단 기준이며
실제 프로그램 변수 선언을 의미하지 않는다.


### 13.3. 검증 증거

다음 증거를 확보한다.

- 실제 Agent 호출 기록
- Planner Result
- Developer Result
- Reviewer Result
- 실제 변경 파일
- 실제 테스트 실행 결과
- 실제 빌드 실행 결과
- 필수 품질 검증 결과
- 인수 조건 확인 결과

검증 증거가 부족한 상태에서
최종 성공으로 판단하지 않는다.


### 13.4. 마지막 변경 이후의 검증

최종 Reviewer PASS 및
Quality Gate 결과는 마지막 변경 상태와 일치해야 한다.

리뷰 이후 소스 코드, 테스트 코드 또는
관련 빌드 설정이 변경된 경우에는
해당 변경에 대한 리뷰 및 검증을 다시 수행한다.

일반적인 빌드 산출물 생성처럼
검토 대상에 영향을 주지 않는 변경은
불필요한 수정 라운드로 간주하지 않는다.


### 13.5. Smoke Test 예외

SMOKE_TEST는 지정된 인수 조건만 검증한다.

예시:

- 지정된 파일 생성
- 지정된 문자열 일치
- 실제 Agent 호출 확인
- 병렬 실행 확인

일반 Java 애플리케이션 개발의
컴파일 및 빌드 조건을 기계적으로 적용하지 않는다.

Smoke Test의 성공은
전체 애플리케이션 품질 검증 성공과 구분한다.


## 14. 병렬 실행 상세 규칙

### 14.1. 실행 가능 조건

다음 조건을 모두 검토한다.

- 작업 간 선행 의존성 없음
- 파일 수정 범위 분리
- 공통 설정 변경 충돌 없음
- 공통 인터페이스 계약 확정
- 데이터베이스 변경 충돌 없음
- 공유 테스트 자원 충돌 없음
- 사용 가능한 Agent 스레드 확보

조건이 충족되지 않으면
순차 실행으로 전환한다.


### 14.2. 병렬 작업 그룹

Planner는 필요에 따라
다음 형태로 작업 그룹을 구성한다.

Execution Group: GROUP-001

Mode: PARALLEL

Tasks:

- TASK-001
- TASK-002
- TASK-003

Dependencies:

- 선행 작업 목록

Allowed Files:

- 각 Task에 할당된 파일 범위

Completion Condition:

- 모든 필수 Task 결과 수집
- 파일 충돌 없음
- 후속 작업 실행 가능

해당 그룹을 실제 코드와 요구사항에 맞게 정의한다.


### 14.3. 스레드 관리

Agent 스레드 수는
실제 Codex 설정의 제한을 따른다.

현재 기본 운영값은 4개이다.

기존 Planner 및 Reviewer 스레드가
열려 있는 경우 해당 수를 고려한다.

작업이 완료된 Agent의 결과를 먼저 수집하고
더 이상 필요하지 않은 스레드를 종료한다.

가용 스레드가 부족하면
작업을 여러 그룹으로 나누어 실행한다.


### 14.4. 공유 자원 관리

다음 자원은 병렬 충돌 가능성을 확인한다.

- 공통 소스 코드
- 빌드 출력 디렉터리
- Gradle 작업
- 데이터베이스
- 테스트 데이터
- 마이그레이션 파일
- 애플리케이션 실행 포트
- 공통 임시 파일
- 외부 시스템 테스트 자원

파일 수정 범위가 분리되었더라도
공유 테스트 환경이나 빌드 작업이
서로 충돌할 수 있음을 고려한다.

필요한 경우 빌드와 통합 테스트는
병렬 구현 종료 후 순차 실행한다.


### 14.5. 병렬 작업 실패

하나의 병렬 작업이 실패한 경우
다음 사항을 확인한다.

- 다른 작업과의 의존성
- 공통 자원 영향
- 실패한 작업의 수정 가능성
- 다른 작업의 계속 실행 가능 여부

안전한 독립 작업은 계속 진행할 수 있다.

실패한 선행 작업에 의존하는 작업은
수행하지 않는다.

모든 필수 작업 결과가 확보되기 전에
전체 구현 완료로 판단하지 않는다.


### 14.6. 병렬 작업 통합

병렬 작업이 완료되면 다음을 확인한다.

- 실제 변경 파일
- 파일별 작업 소유권
- 공통 인터페이스 일치
- 중복 구현
- 파일 충돌
- 패키지 및 도메인 경계
- 통합 테스트 결과

공유 작업 공간에서는
병렬 Agent의 변경사항이 이미 같은 파일 시스템에
존재할 수 있음을 고려한다.

별도 Git Worktree 또는 브랜치를
실제로 사용하지 않았다면
Git Merge를 수행했다고 보고하지 않는다.

통합 후 Reviewer에게 전체 변경 사항을 전달한다.


### 14.7. 병렬 개발 제한

현재 Workflow는 공유 작업 공간을 기반으로 한다.

Allowed Files는 행동 지침이며
운영체제 수준의 파일 소유권 격리를 의미하지 않는다.

같은 파일을 여러 Agent가
동시에 수정하도록 위임하지 않는다.

강제적인 파일 격리와 Worktree 기반 병렬 개발은
별도의 자동화 환경에서 구축할 수 있다.


## 15. Agent 간 산출물 전달

### 15.1. 전달 원칙

Orchestrator는 각 Agent의 산출물을
다음 단계의 Agent에게 명시적으로 전달한다.

하위 Agent가 다른 Agent의 대화 내용이나
이전 실행 결과를 자동으로 모두 알고 있다고
가정하지 않는다.

필요한 산출물과 참조 경로를 함께 전달한다.

원본 요구사항 및 필수 인수 조건이
요약 과정에서 누락되지 않도록 한다.


### 15.2. Planner -> Developer

전달 대상:

- Planner Result
- 사용자 요구사항
- 작업 범위
- 도메인 설계
- API 및 DB 계약
- 구현 작업 단위
- Allowed Files
- Dependencies
- Acceptance Criteria
- Required Tests
- 기술적 제약사항


### 15.3. Developer -> Reviewer

전달 대상:

- Developer Result
- 원본 사용자 요구사항
- Planner Result
- 실제 변경 파일 목록
- 구현 내용
- 자체 테스트 결과
- 실패 및 미검증 항목
- 인수 조건
- 적용 규칙


### 15.4. Reviewer -> Developer

수정이 필요한 경우 전달한다.

- Reviewer Decision
- Required Fixes
- Finding IDs
- Severity
- Rule IDs
- 실제 결함 증거
- 수정 권고 사항
- Blocking 여부
- 필요한 회귀 테스트
- 현재 Revision Count


### 15.5. Reviewer -> Orchestrator

전달 대상:

- 최종 Reviewer Result
- PASS, FAIL 또는 BLOCKED
- 차단 결함 목록
- 해결된 결함
- 미검증 항목
- 검토 증거
- 선택적인 개선 제안


### 15.6. 산출물 저장 정책

STEP 6에서는 Agent 결과를
현재 Codex 실행 문맥에서 전달하고 관리한다.

별도의 보고서 파일이나 State 파일을
반드시 생성할 필요는 없다.

사용자가 보고서 저장을 요청하거나
프로젝트 정책에서 허용하는 경우에만
지정된 경로에 산출물을 저장한다.

향후 외부 자동화 루프 구축 시
구조화된 JSON 및 로그 저장을 적용할 수 있다.


## 16. 오류 및 예외 처리

### 16.1. Agent 호출 실패

Agent 호출에 실패하면 다음을 확인한다.

- Custom Agent 등록 상태
- 멀티 에이전트 활성화 여부
- 가용 Agent 스레드 수
- 모델 호환성
- CLI 실행 환경
- 권한 및 사용량 제한

일시적인 문제에 대해서는
제한된 재시도를 수행할 수 있다.

동일 호출을 최초 시도 이후
최대 2회까지 재시도한다.

해결되지 않으면 BLOCKED로 처리한다.

실제 호출 실패를 역할 전환으로 대체하지 않는다.


### 16.2. Agent 응답 누락 또는 불완전

Agent가 필수 산출물을 반환하지 않은 경우
해당 Agent에게 보완을 요청한다.

보완 요청은 최초 결과 이후
최대 2회로 제한한다.

유효한 산출물을 얻지 못하면
BLOCKED 또는 FAILED로 처리한다.

단순한 보고서 보완 요청은
코드 수정 라운드에 포함하지 않는다.

실제 코드 수정이 필요하면
수정 라운드에 포함한다.


### 16.3. Agent 실행 중단

Agent 실행이 중단되면 다음을 확인한다.

- 실제 작업 완료 여부
- 부분 변경 파일 존재 여부
- 테스트 실행 여부
- 작업 상태 복구 가능 여부
- 중단 원인

필요한 경우 동일 역할의 Agent를
다시 호출할 수 있다.

부분 변경을 검토하지 않고
작업을 처음부터 무조건 반복하지 않는다.

호출 재시도 제한을 우회하지 않는다.


### 16.4. 권한 및 환경 문제

샌드박스, 파일 시스템 또는
네트워크 접근 제한을 준수한다.

권한 오류를 해결하기 위해
보안 설정을 임의로 변경하지 않는다.

승인이 필요한 작업은
현재 승인 정책에 따라 처리한다.

필수 승인이 거부되거나
환경 문제를 해결할 수 없는 경우
BLOCKED로 종료한다.


### 16.5. 빌드 환경 문제

다음 사항을 확인한다.

- Java 버전
- Gradle Wrapper
- 의존성 다운로드
- Gradle 캐시 접근
- 데이터베이스 연결
- 테스트 프로파일
- 환경 변수
- Docker 또는 외부 테스트 환경

환경 문제를 애플리케이션 코드 결함으로
무조건 판단하지 않는다.

문제 해결을 위해 테스트를 삭제하거나
빌드 검증을 생략하지 않는다.


### 16.6. 작업 범위 충돌

다른 Agent 또는 사용자가
동일 파일을 수정하고 있는 경우
해당 파일의 추가 변경을 중단한다.

실제 변경 상태를 확인한 후
작업 소유권과 순서를 조정한다.

기존 사용자 변경을 임의로 되돌리지 않는다.


### 16.7. 차단 조건 재확인

동일한 BLOCKED 조건에 대한
자동 해결 및 재확인 시도는
최대 2회로 제한한다.

Agent 호출 실패에 대한 재시도는
16.1의 제한을 우선 적용한다.

제한에 도달했거나
새로운 해결 근거가 없는 경우에는
BLOCKED 상태로 REPORT에 이동한다.

재시도 종류를 바꾸어
제한을 우회하지 않는다.


## 17. PHASE 7 - REPORT

### 17.1. 목적

전체 하네스 실행 결과를 사용자에게
정확하고 일관된 형식으로 보고한다.

성공, 실패 및 차단 여부와 관계없이
최종 결과 보고를 수행한다.


### 17.2. 수행 주체

- Orchestrator


### 17.3. 완료 전 정리

다음 사항을 확인한다.

- 필수 Agent 결과 수집
- 불필요한 Agent 스레드 종료
- 작업 상태 확정
- 최종 변경 파일 확인
- 검토 결과 정리
- 수정 라운드 정리
- 테스트 및 빌드 결과 정리
- 남은 문제 확인

사용자의 요청 없이
소스 코드나 테스트 파일을 삭제하지 않는다.

실행 로그를 임의로 조작하지 않는다.


### 17.4. 최종 보고서 형식

# Harness Execution Report

## 1. 작업 개요

- Task ID:
- 작업명:
- 작업 목적:
- 사용자 요구사항:
- 최종 상태:
- 실행 모드:

## 2. Workflow 실행 결과

| Phase | Status | Description |
|---|---|---|
| INIT | | |
| PLAN | | |
| IMPLEMENT | | |
| REVIEW | | |
| DECISION | | |
| VALIDATE | | |
| REPORT | | |

Phase Status:

- COMPLETED
- FAILED
- BLOCKED
- NOT_RUN

## 3. Agent 실행 결과

| Agent | Identifier | Task | Status |
|---|---|---|---|
| Planner | | | |
| Developer | | | |
| Reviewer | | | |

- 실제 Agent 호출 횟수:
- 병렬 실행 여부:
- 병렬 실행 그룹:
- 확인 가능한 실행 증거:
- Agent 실행 제약사항:

## 4. 주요 구현 사항

- 구현 기능:
- 주요 설계:
- 적용 기술:
- 적용 개발 규칙:

## 5. 변경 파일

| File | Change Type | Description |
|---|---|---|
| | | |

## 6. 리뷰 및 수정 결과

- 전체 리뷰 횟수:
- 최대 수정 가능 횟수: 5
- 실제 수정 라운드:
- 해결된 결함:
- 미해결 결함:
- 선택적인 개선 제안:

## 7. 수정 이력

| Revision | Finding | Action | Result |
|---|---|---|---|
| 0 | 최초 구현 | | |

## 8. 최종 품질 검증

| Validation | Result | Evidence |
|---|---|---|
| Compile | | |
| Unit Test | | |
| Integration Test | | |
| Build | | |
| Architecture | | |
| SOLID | | |
| Security | | |
| Acceptance Criteria | | |
| Review | | |
| Scope | | |

## 9. 인수 조건

| Acceptance ID | Description | Result |
|---|---|---|
| | | |

## 10. 남은 문제 및 제약사항

- 미해결 문제:
- 미검증 항목:
- 환경 제약:
- 확인이 필요한 정보:
- 사용자 승인 필요 사항:

## 11. 최종 판단

Status: COMPLETED, FAILED 또는 BLOCKED

Reason:

- 최종 판단 근거

Next Action:

- 필요한 후속 작업
- 후속 작업이 없다면 NONE


### 17.5. COMPLETED 보고 조건

다음 조건을 모두 충족해야 한다.

- 필수 Planner 호출 성공
- 필수 Developer 호출 성공
- 필수 Reviewer 호출 성공
- 모든 필수 인수 조건 충족
- 차단 결함 없음
- 최종 Reviewer PASS
- 필수 컴파일 성공
- 필수 테스트 성공
- 최종 빌드 성공
- 적용 대상 Quality Gate 통과

완료 조건을 충족하지 않은 작업은
COMPLETED로 표시하지 않는다.


### 17.6. FAILED 보고 조건

다음 상황에서 FAILED로 보고한다.

- 실제 구현 실패가 해결되지 않음
- 차단 결함이 해결되지 않음
- 필수 테스트 실패가 해결되지 않음
- 최대 수정 횟수 도달
- 동일한 결함 반복으로 진전 없음
- 필수 완료 조건 미충족

실패 원인과 현재 남아 있는 변경 사항을 보고한다.


### 17.7. BLOCKED 보고 조건

다음 상황에서 BLOCKED로 보고한다.

- 필수 정보 부족
- 실제 Agent 호출 기능 사용 불가
- 필수 도구 또는 환경 부족
- 필수 승인 또는 권한 부족
- 필수 검증 수행 불가
- 사용자 결정 필요

차단 원인 및 재개에 필요한 조건을 보고한다.


## 18. Harness 자체 검증

### 18.1. 검증 범위

하네스 자체의 초기 구축 및 검증은
일반 애플리케이션 개발과 구분한다.

다음 사항을 확인할 수 있다.

- Agent 등록
- Agent 실제 호출
- Agent 결과 전달
- 파일 생성 권한
- Reviewer PASS 판단
- Reviewer FAIL 판단
- 수정 루프 실행
- 병렬 Agent 실행
- 최대 수정 횟수 판단
- 차단 조건 판단


### 18.2. Smoke Test 실행 원칙

Smoke Test는 사용자 또는 Orchestrator가
명시적으로 지정한 테스트 범위를 따른다.

테스트용 파일 외에
실제 애플리케이션 코드를 수정하지 않는다.

일반 애플리케이션 코드가 없는 경우
Gradle 빌드가 존재한다고 가정하지 않는다.

Smoke Test 검증 항목과
일반 Quality Gate를 구분한다.


### 18.3. Workflow 테스트

Workflow 검증에서는 다음 실행을 확인한다.

- INIT 수행
- Planner 실제 호출
- Planner Result 전달
- Developer 실제 호출
- Developer Result 전달
- Reviewer 실제 호출
- FAIL 또는 PASS 분기
- 필요한 경우 Developer FIX 호출
- Reviewer 재검토
- Smoke Test 최종 검증
- 최종 REPORT

실제 호출 기록 없이
Workflow 검증 성공으로 보고하지 않는다.


## 19. 종료 및 안전 규칙

### 19.1. 무한 루프 금지

최대 수정 횟수는 5회로 제한한다.

Agent 호출 실패 재시도와
산출물 보완 요청도 유한하게 제한한다.

환경 문제를 해결하지 못한 상태에서
동일한 명령어를 무한 반복하지 않는다.

단계 재진입, 새 Agent 생성,
Context 변경 등의 방법으로
반복 제한을 우회하지 않는다.


### 19.2. 사용자 중단

사용자가 작업을 중단하도록 요청하면
추가 Agent 호출 및 코드 수정을 중단한다.

가능한 범위에서 실행 중인 작업을 종료한다.

현재 변경 상태와 완료된 작업,
미완료 작업을 보고한다.

사용자 요청 없이 기존 작업을 삭제하지 않는다.


### 19.3. 검증 결과 조작 금지

다음 행위를 금지한다.

- 허위 Agent 호출 보고
- 허위 실행 시간 작성
- 허위 테스트 개수 작성
- 허위 빌드 성공 보고
- 실제 Reviewer FAIL을 임의로 PASS 변경
- 실패한 테스트 삭제
- 품질 검사 비활성화
- 인수 조건의 임의 완화
- 요구사항 수정으로 실패 은폐


### 19.4. 권한 및 보안

모든 작업은 실제 Codex 샌드박스와
승인 정책을 준수한다.

운영 데이터베이스 변경,
배포, Git push 및 파괴적인 명령 실행은
AGENTS.md의 정책에 따른다.

하네스 실행을 성공시키기 위해
보안 정책을 우회하지 않는다.


### 19.5. 실행 문맥 손실

현재 작업의 실행 상태와
수정 횟수를 확인할 수 없는 경우
성공으로 가정하지 않는다.

확인 가능한 기록을 기반으로
실행 상태를 복구할 수 있는지 판단한다.

수정 횟수를 신뢰할 수 없는 상태에서
자동 수정 루프를 재시작하지 않는다.

복구할 수 없으면 BLOCKED로 보고한다.

외부 State 저장 및 복구 기능은
별도의 자동화 단계에서 구축한다.


## 20. 최종 운영 원칙

하네스는 단순한 역할 분리 문서가 아니다.

실제 Agent 호출, 작업 위임,
독립적인 코드 검토, 결함 수정 및
최종 검증을 수행하는 개발 절차이다.

모든 개발 작업은 다음 원칙을 준수한다.

- 실제 Agent를 호출한다.
- 승인된 요구사항을 기준으로 작업한다.
- 역할별 책임을 유지한다.
- 필수 산출물을 다음 단계에 전달한다.
- 확인된 결함을 수정한다.
- 반복 횟수를 제한한다.
- 최종 검증 결과를 확인한다.
- 실패 및 차단 상태를 숨기지 않는다.

최종 완료 여부는
AGENTS.md, harness/rules.md,
harness/context.md 및 본 Workflow의
적용 대상 필수 조건을 기준으로 판단한다.
