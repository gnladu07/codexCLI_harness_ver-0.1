# Development Rules

## 1. 공통 개발 원칙

### 1.1. 목적

본 문서는 Java 및 Spring Boot 기반 프로젝트에서
모든 개발 에이전트가 준수해야 하는 기술 표준을 정의한다.

적용 대상:

- Planner Agent
- Developer Agent
- Reviewer Agent
- Orchestrator

본 문서는 AGENTS.md의 운영 지침을 보완한다.

프로젝트별 구체적인 요구사항은
harness/context.md를 기준으로 한다.

### 1.2. 기본 개발 환경

- Language: Java 17
- Framework: Spring Boot
- Build Tool: Gradle
- Database: MySQL
- ORM: Spring Data JPA
- Architecture: Hexagonal Architecture
- Design Principles: SOLID
- Unit Test: JUnit 5
- Mocking: Mockito
- Version Control: Git

라이브러리 및 프레임워크의 구체적인 버전은
실제 프로젝트 설정과 context.md를 기준으로 한다.

기존 프로젝트의 기술 스택을 임의로 변경하지 않는다.

### 1.3. 공통 설계 원칙

다음 원칙을 준수한다.

- SOLID: 객체지향 설계 5대 원칙
- KISS: 불필요하게 복잡한 설계 금지
- DRY: 동일한 지식 및 로직의 불필요한 중복 방지
- YAGNI: 현재 요구사항에 없는 기능 구현 금지
- SoC: 관심사의 분리
- High Cohesion: 높은 응집도
- Low Coupling: 낮은 결합도

설계 원칙을 적용하기 위해 불필요한 추상화,
인터페이스 및 디자인 패턴을 생성하지 않는다.

현재 요구사항을 충족하면서 변경에 대응할 수 있는
가장 단순한 설계를 우선한다.

### 1.4. 개발 기본 규칙

- 요구사항과 인수 조건을 먼저 확인한다.
- 기존 코드와 프로젝트 구조를 먼저 분석한다.
- 기존 프로젝트의 일관성을 유지한다.
- 요청 범위를 벗어난 기능을 구현하지 않는다.
- 사용자의 기존 변경사항을 임의로 수정하지 않는다.
- 임시 구현을 정상 구현으로 보고하지 않는다.
- 검증되지 않은 가정을 사실로 취급하지 않는다.
- 구현과 함께 필요한 테스트를 작성한다.
- 불필요한 기술 및 라이브러리를 추가하지 않는다.

## 2. Java 17 개발 규칙

### 2.1. Java 버전

Java 17에서 지원하는 문법과 API를 사용한다.

Java 17에서 지원하지 않는 기능을 사용하지 않는다.

프로젝트의 Java 버전을 임의로 변경하지 않는다.

### 2.2. Java 언어 기능

필요에 따라 다음 기능을 활용할 수 있다.

- Record
- Enum
- Optional
- Stream API
- Lambda Expression
- Sealed Class
- Switch Expression
- Text Block
- Pattern Matching for instanceof

최신 문법을 사용하기 위해 코드의 가독성을
저하시키지 않는다.

Record는 불변 DTO, Command, Query,
Value Object 등에 사용할 수 있다.

일반적인 JPA Entity에는 Record를 사용하지 않는다.

### 2.3. 객체 생성 및 상태 관리

- 객체는 생성 시 유효한 상태를 유지해야 한다.
- 생성자 또는 정적 팩토리 메서드를 사용한다.
- 불필요한 public setter를 사용하지 않는다.
- 가능한 경우 불변 객체를 사용한다.
- 변경할 필요가 없는 필드는 final로 선언한다.
- 객체의 상태 변경은 의미 있는 메서드로 수행한다.
- 외부에서 내부 컬렉션을 임의로 수정하지 못하게 한다.

잘못된 예:

    member.setStatus(MemberStatus.ACTIVE);

권장 예:

    member.activate();

도메인 객체는 단순한 데이터 저장소가 아니라
자신의 상태와 비즈니스 규칙을 관리해야 한다.

### 2.4. Null 처리

- 필수 값에 대한 null 검증을 수행한다.
- null을 반환하는 API는 필요한 경우 명확히 문서화한다.
- Optional은 값의 부재를 표현할 때 사용한다.
- Optional.get()을 존재 여부 확인 없이 호출하지 않는다.
- Optional 필드 및 Optional 파라미터 사용을 기본적으로 피한다.
- 빈 컬렉션으로 표현 가능한 경우 null을 반환하지 않는다.

권장 예:

    Member member = memberRepository.findById(memberId)
        .orElseThrow(MemberNotFoundException::new);

### 2.5. 컬렉션 및 Stream

- 구체적인 구현체보다 적절한 인터페이스 타입을 사용한다.
- List, Set, Map의 목적에 맞는 자료구조를 선택한다.
- 중복 허용 여부와 순서 보장 여부를 고려한다.
- Stream 내부에서 불필요한 상태 변경을 수행하지 않는다.
- 복잡한 Stream 체이닝보다 가독성을 우선한다.
- 검증 없이 parallelStream을 사용하지 않는다.

잘못된 예:

    List<Member> members = null;

권장 예:

    List<Member> members = Collections.emptyList();

### 2.6. 숫자 및 날짜 처리

- 금액 계산에 float 또는 double을 사용하지 않는다.
- 금액은 BigDecimal 또는 명시된 최소 화폐 단위를 사용한다.
- BigDecimal 사용 시 필요한 scale과 반올림 정책을 정의한다.
- 날짜 및 시간은 java.time 패키지를 우선 사용한다.
- 시간대가 중요한 데이터는 기준 시간대를 명확히 한다.
- 테스트 가능한 시간 처리를 위해 필요한 경우 Clock을 사용한다.
- 날짜 및 시간 계산을 임의의 문자열 연산으로 구현하지 않는다.

### 2.7. 상수 및 Enum

- 의미가 불명확한 매직 넘버를 사용하지 않는다.
- 반복되는 정책 값은 의미 있는 상수로 정의한다.
- 고정된 상태와 유형은 Enum 사용을 고려한다.
- 변경 가능한 운영 정책은 설정 또는 정책 객체로 관리한다.

잘못된 예:

    if (member.getStatus() == 1) {
        // ...
    }

권장 예:

    if (member.isActive()) {
        // ...
    }

## 3. 코드 컨벤션

### 3.1. 네이밍 규칙

Package:

- 소문자를 사용한다.
- 의미 있는 도메인 및 역할 이름을 사용한다.

Class:

- PascalCase를 사용한다.
- 클래스의 책임을 표현한다.

Method:

- camelCase를 사용한다.
- 행위를 표현하는 동사 중심의 이름을 사용한다.

Variable:

- camelCase를 사용한다.
- 변수의 목적을 이해할 수 있는 이름을 사용한다.

Constant:

- UPPER_SNAKE_CASE를 사용한다.

예시:

    package com.example.member.domain.model;

    public class Member {

        private static final int MAX_NICKNAME_LENGTH = 30;

        private String nickname;

        public void changeNickname(String nickname) {
            // ...
        }
    }

### 3.2. 클래스 네이밍

역할에 따라 다음 이름을 사용한다.

- Domain Model: Member
- Value Object: MemberId
- Input Port: RegisterMemberUseCase
- Output Port: LoadMemberPort
- Application Service: RegisterMemberService
- Web Adapter: MemberController
- Persistence Adapter: MemberPersistenceAdapter
- JPA Entity: MemberJpaEntity
- Spring Data Repository: MemberJpaRepository
- Request DTO: RegisterMemberRequest
- Response DTO: MemberResponse
- Command: RegisterMemberCommand
- Query: FindMemberQuery

클래스의 이름은 실제 역할과 일치해야 한다.

명확한 책임이 없는 Manager, Helper, Util 등의
이름을 무분별하게 사용하지 않는다.

### 3.3. 메서드 설계

- 하나의 메서드는 하나의 명확한 작업을 수행한다.
- 복잡한 메서드는 의미 있는 단위로 분리한다.
- 깊은 중첩 조건문을 지양한다.
- 필요한 경우 Guard Clause를 사용한다.
- 과도한 파라미터 사용을 피한다.
- 관련 파라미터는 Command 또는 Value Object로 묶을 수 있다.
- 조회 메서드와 상태 변경 메서드의 책임을 구분한다.
- 이름만으로 파악하기 어려운 부수 효과를 만들지 않는다.

메서드 길이와 파라미터 개수는 참고 지표로 사용한다.

정해진 줄 수를 충족하기 위해 의미 없는
메서드 분리를 수행하지 않는다.

### 3.4. 접근 제어자

- 필요한 최소 접근 범위를 사용한다.
- 외부 공개가 필요한 경우에만 public을 사용한다.
- 내부 구현 세부사항은 private으로 보호한다.
- 상속을 위해 불필요하게 protected를 사용하지 않는다.
- 내부 구현을 테스트하기 위해 접근 범위를 무분별하게 넓히지 않는다.

### 3.5. 코드 포맷

- 기본 들여쓰기는 공백 4칸을 사용한다.
- 프로젝트에 포맷터가 있으면 해당 규칙을 우선한다.
- 사용하지 않는 import를 제거한다.
- 와일드카드 import를 기본적으로 지양한다.
- 의미 없는 빈 줄과 불필요한 공백을 제거한다.
- 가독성을 저하시키는 과도하게 긴 코드를 피한다.
- 기존 프로젝트의 코드 스타일을 유지한다.

### 3.6. 주석 및 문서화

주석은 코드가 존재하는 이유와 설계 의도를 설명한다.

다음 상황에는 필요한 주석을 작성한다.

- 복잡한 비즈니스 규칙
- 기술적 제약사항
- 중요한 설계 결정
- 외부 시스템 연동 조건
- 동시성 및 트랜잭션 관련 주의사항
- 공개 API의 계약 및 예외 조건
- 일반적이지 않은 어노테이션 사용 이유

다음 주석은 지양한다.

- 코드 자체를 그대로 반복하는 주석
- 변경된 코드와 일치하지 않는 주석
- 의미 없는 자동 생성 주석
- 실제 구현되지 않은 기능을 완료했다고 표현하는 주석

사용자가 학습 목적의 상세 주석을 요청한 경우에는
메서드와 주요 어노테이션의 역할을 추가 설명한다.

## 4. Hexagonal Architecture

### 4.1. 아키텍처 목적

Hexagonal Architecture를 적용하여
비즈니스 로직을 외부 기술로부터 분리한다.

핵심 목표:

- 도메인 독립성
- 기술 의존성 최소화
- 외부 시스템 교체 용이성
- 테스트 용이성
- 변경 영향 범위 최소화
- 비즈니스 규칙의 보호

### 4.2. 기본 패키지 구조

신규 프로젝트는 도메인 중심 패키지 구조를 사용한다.

    com.example.project
    |
    |-- member
    |   |-- domain
    |   |   |-- model
    |   |   |-- service
    |   |   |-- policy
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
    |           |   |-- entity
    |           |   |-- repository
    |           |   |-- mapper
    |           |
    |           |-- external
    |           |-- messaging
    |
    |-- global
        |-- config
        |-- exception
        |-- security

모든 패키지를 처음부터 생성할 필요는 없다.

현재 기능에 필요한 패키지만 생성한다.

기존 프로젝트에 다른 헥사고날 패키지 구조가 존재하면
임의로 전체 패키지를 재구성하지 않는다.

### 4.3. Domain 계층

Domain 계층은 핵심 비즈니스 모델과 규칙을 관리한다.

허용되는 구성요소:

- Domain Entity
- Aggregate
- Value Object
- Domain Service
- Domain Policy
- Domain Event
- Domain Exception

Domain 계층은 다음 기술에 직접 의존하지 않는다.

- Spring Framework
- Spring Boot
- Spring Data JPA
- Jakarta Persistence
- Servlet API
- Controller
- 데이터베이스 구현체
- Redis Client
- Kafka Client
- 외부 HTTP Client

Domain 계층에서는 다음 어노테이션을 사용하지 않는다.

- @Entity
- @Repository
- @Service
- @Component
- @Autowired
- @Transactional
- @RestController

Domain은 순수 Java 기반으로 구현한다.

핵심 비즈니스 규칙을 Application Service 또는
Controller에 무분별하게 배치하지 않는다.

### 4.4. Application 계층

Application 계층은 유스케이스 실행과
도메인 객체 간 작업 흐름을 조율한다.

주요 책임:

- Input Port 정의 및 구현
- 사용자 요청에 따른 유스케이스 수행
- Domain 객체 호출
- Output Port 호출
- 트랜잭션 경계 설정
- Command, Query, Result 관리

Application 계층은 Domain 계층에 의존할 수 있다.

Application 계층은 다음 구현체에 직접 의존하지 않는다.

- Controller
- JPA Entity
- Spring Data JPA Repository
- Persistence Adapter
- 외부 API Adapter
- Kafka Producer 구현체
- Redis 구현체

본 프로젝트에서는 실용적인 헥사고날 구조를 적용한다.

Application Service에서 다음 어노테이션을 허용한다.

- @Service
- @Transactional

Domain 계층의 기술 독립성은 유지한다.

### 4.5. Input Port

Input Port는 외부에서 호출 가능한 유스케이스를 정의한다.

예시:

    public interface RegisterMemberUseCase {

        MemberId register(RegisterMemberCommand command);
    }

규칙:

- 유스케이스의 목적이 명확해야 한다.
- HTTP Request 또는 Response 객체를 받지 않는다.
- Servlet 관련 타입에 의존하지 않는다.
- Web Request DTO를 직접 받지 않는다.
- 필요한 경우 Command, Query, Result를 사용한다.
- 불필요하게 비대한 유스케이스 인터페이스를 만들지 않는다.

### 4.6. Output Port

Output Port는 Application 계층에서 필요로 하는
외부 기능의 계약을 정의한다.

예시:

    public interface LoadMemberPort {

        Optional<Member> findById(MemberId memberId);
    }

    public interface SaveMemberPort {

        Member save(Member member);
    }

규칙:

- 외부 구현 기술을 노출하지 않는다.
- JPA Entity를 반환하지 않는다.
- Spring Data JPA Repository를 상속하지 않는다.
- 외부 HTTP 응답 객체를 반환하지 않는다.
- Application과 Domain에 필요한 기능만 정의한다.
- 실제 변경 이유와 사용 목적에 따라 인터페이스를 분리한다.

### 4.7. Input Adapter

Input Adapter는 외부 요청을 수신하여
Input Port를 호출하는 역할을 수행한다.

예시:

- REST Controller
- WebSocket Handler
- Message Consumer
- Scheduler
- Command Line Adapter

주요 책임:

- 외부 입력 수신
- 입력 형식 검증
- 인증된 사용자 정보 확인
- Request DTO를 Command 또는 Query로 변환
- Input Port 호출
- 결과를 외부 응답으로 변환

금지 사항:

- Repository 직접 호출
- JPA Entity 직접 반환
- 핵심 비즈니스 로직 직접 구현
- 트랜잭션 흐름 직접 제어
- Output Adapter 직접 호출
- Application Service 구현체에 직접 의존

### 4.8. Output Adapter

Output Adapter는 Output Port를 구현하여
외부 기술과 연결한다.

예시:

- Persistence Adapter
- External API Adapter
- Redis Adapter
- Kafka Adapter
- File Storage Adapter
- Email Adapter

주요 책임:

- 외부 시스템 호출
- 데이터 저장 및 조회
- 외부 기술 객체와 내부 객체 간 변환
- 외부 시스템 오류의 적절한 변환
- Output Port 계약 준수

Output Adapter에서 핵심 비즈니스 정책을
독립적으로 결정하지 않는다.

### 4.9. 의존성 방향

허용되는 의존성:

- Application -> Domain
- Input Adapter -> Application Input Port
- Output Adapter -> Application Output Port
- Adapter -> 필요한 Domain 타입
- Configuration -> 필요한 구성요소

금지되는 의존성:

- Domain -> Application
- Domain -> Adapter
- Domain -> Spring 또는 JPA
- Application -> Adapter 구현체
- Input Adapter -> Output Adapter 구현체
- Controller -> Spring Data Repository

외부에서 내부로 향하는 의존성 방향을 유지한다.

실행 시 호출 흐름과 소스 코드 의존성 방향을
서로 혼동하지 않는다.

### 4.10. 도메인 간 의존성

다른 도메인의 내부 Adapter 또는
Persistence 구현체를 직접 참조하지 않는다.

필요한 경우 명확한 모듈 계약,
공개 유스케이스 또는 이벤트를 사용한다.

다른 도메인의 내부 데이터 구조를
직접 변경하지 않는다.

순환 의존성을 허용하지 않는다.

시스템이 MSA인지 Modular Monolith인지는
context.md에서 별도로 결정한다.

Hexagonal Architecture를 적용하기 위해
MSA를 강제로 도입하지 않는다.

### 4.11. Mapper 규칙

외부 기술 객체와 내부 모델의 변환은
적절한 Adapter 또는 Mapper에서 수행한다.

변환 대상:

- Request DTO -> Command
- Application Result -> Response DTO
- Domain Model -> JPA Entity
- JPA Entity -> Domain Model
- External Response -> 내부 모델

Mapper는 비즈니스 정책을 결정하지 않는다.

단순 객체 변환을 위해 불필요하게 복잡한
Mapper 계층을 생성하지 않는다.

## 5. SOLID 5대 원칙

### 5.1. SRP - Single Responsibility Principle

단일 책임 원칙을 준수한다.

클래스는 하나의 명확한 책임과
응집된 변경 이유를 가져야 한다.

금지 사례:

- Controller에서 비즈니스 로직과 DB 접근 처리
- Service에서 회원 관리와 결제 및 이메일 처리 혼합
- Repository에서 비즈니스 정책 결정
- Mapper에서 도메인 상태 변경

준수 기준:

- Controller는 외부 요청과 응답을 처리한다.
- Application Service는 유스케이스를 조율한다.
- Domain은 핵심 비즈니스 규칙을 처리한다.
- Repository 및 Adapter는 외부 기술 접근을 처리한다.

단일 책임을 준수하기 위해
모든 메서드를 별도의 클래스로 분리하지 않는다.

### 5.2. OCP - Open/Closed Principle

확장에는 열려 있고
불필요한 수정에는 닫혀 있는 구조를 설계한다.

변경 가능성이 높은 정책은
적절한 추상화와 구현체로 분리할 수 있다.

예시:

    public interface DiscountPolicy {

        BigDecimal calculate(BigDecimal price);
    }

신규 할인 정책이 추가될 때
기존 정책의 핵심 코드를 불필요하게 변경하지 않도록 한다.

단순하고 변경 가능성이 낮은 조건문까지
무조건 Strategy Pattern으로 변경하지 않는다.

### 5.3. LSP - Liskov Substitution Principle

하위 타입은 상위 타입의 계약을 준수해야 한다.

인터페이스 구현체를 교체하더라도
호출자의 기대 동작이 유지되어야 한다.

금지 사례:

- 구현체마다 계약이 서로 다르게 동작하는 경우
- 지원해야 하는 기능을 임의로 무시하는 경우
- 구현체가 상위 타입의 유효한 입력을 거부하는 경우
- 정상적인 호출에서 의미 없는 예외를 발생시키는 경우

필요한 경우 상속보다 조합을 우선한다.

상위 타입의 입력 조건과 결과 조건을
구현체가 위반하지 않는지 검증한다.

### 5.4. ISP - Interface Segregation Principle

사용하지 않는 기능에 의존하지 않도록
인터페이스를 설계한다.

금지 사례:

- 모든 도메인 기능을 하나의 비대한 인터페이스로 정의
- 특정 구현체가 사용하지 않는 메서드까지 강제 구현
- 역할과 무관한 메서드에 대한 의존성 발생

권장 예:

    public interface RegisterMemberUseCase {
        MemberId register(RegisterMemberCommand command);
    }

    public interface FindMemberUseCase {
        MemberResult find(MemberId memberId);
    }

    public interface DeleteMemberUseCase {
        void delete(MemberId memberId);
    }

유스케이스의 책임과 호출자의 요구에 따라 분리한다.

메서드마다 반드시 인터페이스를 생성해야 한다는
의미는 아니다.

### 5.5. DIP - Dependency Inversion Principle

상위 수준의 비즈니스 로직이
하위 수준의 기술 구현체에 직접 의존하지 않도록 한다.

상위 계층에서 필요한 추상화 계약을 정의하고
하위 계층이 해당 계약을 구현한다.

금지 사례:

    public class RegisterMemberService {

        private final MemberJpaRepository repository;
    }

권장 예:

    public class RegisterMemberService {

        private final SaveMemberPort saveMemberPort;
    }

Output Adapter는 Application에서 정의한
Output Port를 구현한다.

구현체 연결은 생성자 주입 또는
Configuration을 통해 수행한다.

모든 클래스에 불필요한 인터페이스를 만들지 않는다.

## 6. Spring Boot 개발 규칙

### 6.1. 의존성 주입

생성자 주입을 기본으로 사용한다.

권장 예:

    @Service
    public class RegisterMemberService
            implements RegisterMemberUseCase {

        private final SaveMemberPort saveMemberPort;

        public RegisterMemberService(
                SaveMemberPort saveMemberPort
        ) {
            this.saveMemberPort = saveMemberPort;
        }
    }

필드 주입을 사용하지 않는다.

잘못된 예:

    @Autowired
    private MemberRepository memberRepository;

Lombok이 이미 사용되는 프로젝트에서는
@RequiredArgsConstructor를 사용할 수 있다.

단순히 생성자 생성을 위해 Lombok을
무조건 추가하지 않는다.

### 6.2. Spring Bean

- Bean은 명확한 책임을 가져야 한다.
- Singleton Bean에 요청별 가변 상태를 저장하지 않는다.
- 순환 참조를 허용하지 않는다.
- 직접 new로 생성한 객체에 Spring AOP가
  자동 적용된다고 가정하지 않는다.
- Bean 생명주기와 스코프를 고려한다.

### 6.3. 설정 관리

환경별 설정을 분리한다.

예시:

- application.yml
- application-local.yml
- application-test.yml
- application-prod.yml

규칙:

- 민감한 설정값을 소스 코드에 저장하지 않는다.
- 환경에 따라 변경되는 값은 외부 설정으로 관리한다.
- 관련 설정은 @ConfigurationProperties를 고려한다.
- 존재하지 않는 환경 변수에 대해 필요한 검증을 수행한다.
- 개발 환경과 운영 환경의 설정을 혼동하지 않는다.
- 운영 설정에서 불필요한 디버그 기능을 활성화하지 않는다.

### 6.4. 어노테이션 사용

어노테이션의 동작과 적용 범위를 이해하고 사용한다.

- @RestController: HTTP 요청 및 응답 처리
- @Service: Application Service 등록
- @Repository: Persistence 관련 Bean 및 예외 변환
- @Component: 일반 Spring Bean 등록
- @Configuration: Bean 및 설정 정의
- @Transactional: 트랜잭션 경계 정의
- @Validated: 필요한 메서드 검증 적용
- @Valid: Bean Validation 수행

어노테이션을 관습적으로 무분별하게 추가하지 않는다.

Domain 계층의 Spring 독립성을 유지한다.

## 7. 도메인 모델 개발 규칙

### 7.1. Domain Entity

Domain Entity는 식별자와 생명주기를 가진다.

- 식별자의 의미와 생성 정책을 명확히 한다.
- 비즈니스 상태를 스스로 관리한다.
- 불필요한 setter를 사용하지 않는다.
- 잘못된 상태 전이를 방지한다.
- 핵심 비즈니스 규칙을 메서드로 표현한다.

### 7.2. Value Object

Value Object는 값의 의미와 유효성을 표현한다.

예시:

- MemberId
- Email
- Money
- PhoneNumber

규칙:

- 가능한 경우 불변으로 구현한다.
- 값 기반 동등성을 제공한다.
- 생성 시 필요한 유효성 검사를 수행한다.
- 외부 기술에 직접 의존하지 않는다.

모든 기본 타입을 무조건
Value Object로 변환하지 않는다.

비즈니스 의미 및 검증 책임을 고려하여 결정한다.

### 7.3. Domain Service

단일 Domain Entity에 배치하기 어려운
핵심 비즈니스 규칙을 수행한다.

기술적인 DB 조회 및 외부 시스템 연동을
Domain Service에 직접 구현하지 않는다.

Application Service와 Domain Service의
책임을 혼동하지 않는다.

### 7.4. 상태 전이

상태가 존재하는 도메인은
허용되는 상태 전이를 정의한다.

예시:

    PENDING -> ACTIVE
    ACTIVE -> SUSPENDED
    SUSPENDED -> ACTIVE

허용되지 않은 상태 전이는
Domain 계층에서 방지한다.

단순 setter로 상태 전이 규칙을 우회하지 않는다.

### 7.5. Domain Event

비즈니스적으로 의미 있는 상태 변경을
표현해야 할 때 Domain Event를 고려한다.

Domain Event에 Kafka, Redis 등의
외부 기술 타입을 포함하지 않는다.

이벤트 기반 아키텍처가 필요하지 않은
기능에 메시지 브로커를 강제로 도입하지 않는다.

## 8. REST API 개발 규칙

### 8.1. URI 설계

리소스 중심으로 URI를 설계한다.

신규 API의 기본 예시:

    POST   /api/v1/members
    GET    /api/v1/members
    GET    /api/v1/members/{memberId}
    PATCH  /api/v1/members/{memberId}
    DELETE /api/v1/members/{memberId}

리소스명은 기본적으로 복수형 명사를 사용한다.

불필요한 동사 중심 URI를 지양한다.

도메인 행위가 일반적인 CRUD로 표현되기 어려운 경우
명확한 행위 중심 엔드포인트를 허용한다.

기존 API 계약과 호환성을 고려한다.

### 8.2. HTTP Method

- GET: 리소스 조회
- POST: 리소스 생성 또는 명령 실행
- PUT: 리소스 전체 교체
- PATCH: 리소스 부분 변경
- DELETE: 리소스 삭제

GET 요청에서 비즈니스 상태를 변경하지 않는다.

PUT 및 DELETE의 멱등성 의미를 고려한다.

POST는 필요한 업무에 대해 별도의
중복 요청 방지 정책을 적용한다.

### 8.3. HTTP Status Code

상황에 맞는 상태 코드를 반환한다.

- 200 OK: 정상 조회 또는 처리
- 201 Created: 리소스 생성 성공
- 202 Accepted: 비동기 처리 요청 접수
- 204 No Content: 응답 본문 없는 처리 성공
- 400 Bad Request: 잘못된 요청
- 401 Unauthorized: 인증되지 않은 요청
- 403 Forbidden: 권한이 없는 요청
- 404 Not Found: 리소스가 존재하지 않음
- 409 Conflict: 현재 상태 또는 데이터 충돌
- 422 Unprocessable Content: 처리할 수 없는 입력
- 429 Too Many Requests: 요청 제한 초과
- 500 Internal Server Error: 예상하지 못한 서버 오류
- 503 Service Unavailable: 일시적 서비스 이용 불가

모든 오류를 200 또는 500으로 반환하지 않는다.

400과 422의 구분은 프로젝트 API 정책을 기준으로 한다.

201 응답에서 필요한 경우 Location 헤더를 제공한다.

204 응답에는 본문을 포함하지 않는다.

### 8.4. DTO

외부 API 요청과 응답에 DTO를 사용한다.

JPA Entity를 Controller에서 직접 반환하지 않는다.

Request DTO와 Response DTO의 책임을 분리한다.

Java Record를 필요한 경우 사용할 수 있다.

예시:

    public record RegisterMemberRequest(
        String email,
        String password,
        String nickname
    ) {}

    public record MemberResponse(
        String memberId,
        String email,
        String nickname
    ) {}

비밀번호 및 민감한 내부 데이터를
응답 DTO에 포함하지 않는다.

### 8.5. 입력 검증

Web Adapter에서 입력 형식 검증을 수행한다.

필요에 따라 다음 검증을 사용한다.

- @NotNull
- @NotBlank
- @Size
- @Email
- @Min
- @Max
- @Pattern
- @Valid

형식 검증과 비즈니스 검증을 구분한다.

Web Adapter:

- 필수값 여부
- 문자열 길이
- 이메일 형식
- 허용된 입력 형태

Domain 또는 Application:

- 상태 전이 가능 여부
- 업무상 허용된 요청인지 여부
- 중복 데이터 여부
- 유스케이스별 업무 조건

클라이언트 검증만 신뢰하지 않는다.

도메인 불변 조건을 Web Adapter에서만 검증하지 않는다.

### 8.6. 목록 조회

대량 데이터 조회에는 페이지네이션을 적용한다.

- 기본 페이지 크기 정의
- 최대 페이지 크기 제한
- 허용된 정렬 조건만 사용
- 안정적인 정렬 기준 적용
- 필요한 데이터만 조회

모든 데이터를 한 번에 반환하는 API를
무분별하게 구현하지 않는다.

### 8.7. API 호환성

기존 응답 구조와 상태 코드를
승인 없이 변경하지 않는다.

필드 삭제, 타입 변경, 필수 입력 추가 등
Breaking Change에 주의한다.

OpenAPI 문서가 존재하면
변경된 API 계약을 함께 반영한다.

## 9. 데이터베이스 및 JPA 개발 규칙

### 9.1. 기본 원칙

데이터베이스는 MySQL을 기본으로 한다.

구체적인 버전, 식별자 정책, 스키마 규칙은
context.md 및 기존 데이터베이스를 기준으로 한다.

다음 사항을 고려한다.

- 데이터 무결성
- 정규화
- 참조 관계
- 유일성
- 조회 패턴
- 인덱스
- 트랜잭션
- 동시성

### 9.2. Domain Model과 JPA Entity

Domain Model과 JPA Entity를 분리한다.

Domain Model 예시:

    public class Member {

        private final MemberId id;
        private String nickname;

        public void changeNickname(String nickname) {
            // 비즈니스 규칙 검증 및 상태 변경
        }
    }

JPA Entity 예시:

    @Entity
    @Table(name = "members")
    public class MemberJpaEntity {

        @Id
        private String id;

        @Column(nullable = false)
        private String nickname;

        protected MemberJpaEntity() {
        }
    }

JPA Entity는 Output Adapter 내부에서 관리한다.

Mapper를 통해 Domain Model과 변환한다.

JPA 변경 감지가 Domain Model에
자동 적용된다고 가정하지 않는다.

Domain과 JPA Entity 간 상태 변경을
명시적으로 영속화한다.

### 9.3. Spring Data Repository

Spring Data Repository는
Persistence Adapter 내부에서 정의한다.

예시:

    public interface MemberJpaRepository
            extends JpaRepository<MemberJpaEntity, String> {

        boolean existsByEmail(String email);
    }

Controller와 Application Service에서
해당 Repository를 직접 호출하지 않는다.

Persistence Adapter가 Output Port를 구현하고
내부적으로 JPA Repository를 사용한다.

### 9.4. 엔티티 관계 설정

- 객체 관계는 실제 도메인 요구에 따라 정의한다.
- 무분별한 양방향 연관관계를 피한다.
- 순환 참조 및 직렬화 문제를 고려한다.
- 연관관계의 주인 개념을 이해하고 적용한다.
- Cascade와 orphanRemoval을 신중히 사용한다.
- 필요한 경우 LAZY 로딩을 명시적으로 설정한다.
- 불필요한 EAGER 조회를 지양한다.

모든 연관관계를 자동으로 삭제하거나
영속화하도록 설정하지 않는다.

### 9.5. 데이터 무결성

애플리케이션 검증뿐 아니라
데이터베이스 제약조건을 활용한다.

- PRIMARY KEY
- UNIQUE
- NOT NULL
- FOREIGN KEY
- CHECK

동일 데이터에 대한 동시 요청 가능성이 있으면
사전 중복 조회만으로 무결성을 보장하지 않는다.

필요한 UNIQUE 제약조건 및
데이터 충돌 예외 처리를 함께 적용한다.

### 9.6. SQL 및 조회 최적화

- 필요한 컬럼만 조회한다.
- SQL 파라미터 바인딩을 사용한다.
- 외부 입력을 SQL 문자열에 직접 결합하지 않는다.
- N+1 문제를 확인한다.
- 필요한 경우 Fetch Join 또는 EntityGraph를 사용한다.
- DTO Projection을 적절히 활용한다.
- 실제 조회 조건과 정렬에 맞는 인덱스를 검토한다.
- 대량 조회에서 메모리 사용량을 고려한다.

인덱스를 추가할 때 조회 이점과
쓰기 비용을 함께 고려한다.

실행 계획을 확인하지 않고
성능이 개선되었다고 단정하지 않는다.

### 9.7. 데이터베이스 변경 관리

기존 프로젝트에서 사용하는
마이그레이션 도구를 우선한다.

필요에 따라 다음 도구를 고려한다.

- Flyway
- Liquibase

운영 환경에서는 임의의 스키마 변경을 금지한다.

운영 DB에 대해 ddl-auto=create 또는
create-drop을 사용하지 않는다.

운영 스키마는 검증된 마이그레이션으로 관리한다.

데이터 손실 가능성이 있는 변경은
사용자 승인 및 복구 방안을 확인한다.

### 9.8. 삭제 정책

Hard Delete와 Soft Delete는
업무 요구사항에 따라 결정한다.

Soft Delete 적용 시 다음을 고려한다.

- 기본 조회 조건
- 유일성 제약조건
- 복구 정책
- 데이터 보존 기간
- 개인정보 삭제 정책

모든 테이블에 일괄적으로 Soft Delete를 적용하지 않는다.

## 10. 트랜잭션 및 동시성 규칙

### 10.1. 트랜잭션 경계

트랜잭션은 유스케이스 단위로 설계한다.

기본적으로 Application Service에서
트랜잭션 경계를 설정한다.

예시:

    @Service
    public class RegisterMemberService
            implements RegisterMemberUseCase {

        @Transactional
        public MemberId register(
                RegisterMemberCommand command
        ) {
            // 회원 등록 유스케이스 처리
        }
    }

조회 전용 유스케이스는 필요한 경우
@Transactional(readOnly = true)를 사용한다.

readOnly 설정이 데이터 변경을 항상
강제 차단한다고 가정하지 않는다.

### 10.2. 트랜잭션 주의사항

- 트랜잭션 범위를 불필요하게 확대하지 않는다.
- 긴 네트워크 호출을 트랜잭션 내부에서 지양한다.
- 예외 처리 후 의도치 않게 커밋되지 않도록 한다.
- 롤백 조건을 확인한다.
- Spring AOP의 프록시 동작을 고려한다.
- Self Invocation으로 트랜잭션이 적용되지
  않을 수 있음을 고려한다.

비즈니스적으로 원자성이 필요한 작업을
임의로 여러 트랜잭션으로 분리하지 않는다.

### 10.3. 동시성 제어

다음 업무는 동시성 문제를 검토한다.

- 재고 차감
- 결제 처리
- 예약 및 매칭
- 잔액 변경
- 수량 제한
- 중복 등록
- 상태 변경

상황에 따라 다음 방법을 검토한다.

- 데이터베이스 제약조건
- 조건부 UPDATE
- Optimistic Lock
- Pessimistic Lock
- 적절한 트랜잭션 격리 수준
- Idempotency Key

실제 문제가 없는데 분산 락을
무조건 적용하지 않는다.

선택한 방식의 충돌 처리와 재시도 정책을 정의한다.

### 10.4. 멱등성

중복 요청으로 인해 동일한 업무가
반복 처리되어서는 안 되는 기능은
멱등성 보장 방안을 설계한다.

예시:

- 결제 요청
- 포인트 지급
- 이벤트 소비
- 주문 등록
- 예약 확정

필요한 경우 다음을 활용한다.

- Idempotency Key
- UNIQUE 제약조건
- 처리 이벤트 기록
- 상태 기반 조건부 처리

동시 요청에 대해서도
멱등성이 보장되는지 검증한다.

### 10.5. 외부 시스템 및 이벤트

외부 API 실패와 내부 DB 실패를 구분한다.

DB 트랜잭션이 외부 API 또는
메시지 브로커까지 자동으로 보장한다고 가정하지 않는다.

이벤트 발행과 DB 변경의 일관성이 필요한 경우
Outbox Pattern 등을 검토한다.

재시도에는 다음을 고려한다.

- 재시도 가능 오류
- 최대 재시도 횟수
- Backoff
- Timeout
- 중복 처리 방지

필요하지 않은 분산 시스템 기술을
프로젝트에 강제로 도입하지 않는다.

## 11. 예외 처리 규칙

### 11.1. 기본 원칙

예외는 발생 원인과 업무 의미를 표현해야 한다.

예상 가능한 비즈니스 오류와
예상하지 못한 시스템 오류를 구분한다.

다음 행위를 금지한다.

- 빈 catch 블록
- 예외 원인을 숨기는 처리
- 모든 예외를 RuntimeException으로 재생성
- 실패했음에도 성공 결과 반환
- 모든 오류를 동일한 상태 코드로 반환

예외 변환 시 필요한 원인 정보를 유지한다.

### 11.2. Domain Exception

도메인 규칙 위반에 대한 예외는
업무 의미를 표현한다.

예시:

- InvalidMemberStatusException
- InvalidNicknameException
- InvalidReservationStateException

Domain Exception에 HTTP Status를
직접 의존시키지 않는다.

### 11.3. Application Exception

유스케이스 수행 중 발생하는 오류를 표현한다.

예시:

- MemberNotFoundException
- DuplicateEmailException
- ReservationConflictException

모든 오류마다 반드시 새로운 예외 클래스를
생성해야 하는 것은 아니다.

오류의 의미와 처리 방식에 따라 설계한다.

### 11.4. 외부 기술 예외

외부 시스템의 구현 기술이
내부 도메인으로 직접 전파되지 않도록 한다.

필요한 경우 Adapter에서 적절한
Application Exception으로 변환한다.

모든 데이터베이스 오류를
중복 데이터 오류로 간주하지 않는다.

### 11.5. Global Exception Handler

Web Adapter 또는 공통 Web 오류 처리 영역에서
@RestControllerAdvice를 사용하여
API 예외 응답을 일관되게 처리한다.

예외와 HTTP Status 간 매핑을 관리한다.

Spring ProblemDetail 또는 기존 프로젝트의
공통 오류 응답 형식을 사용할 수 있다.

서로 다른 오류 응답 규격을
불필요하게 혼용하지 않는다.

오류 응답에는 필요한 경우 다음 정보를 포함한다.

- 오류 코드
- 사용자에게 제공할 메시지
- HTTP 상태
- 요청 추적 식별자
- 입력 검증 오류 정보

다음 정보는 응답에 포함하지 않는다.

- 내부 Stack Trace
- 데이터베이스 접속 정보
- 비밀번호 및 인증 정보
- 내부 시스템 경로
- 민감한 SQL 정보

## 12. 보안 규칙

### 12.1. 인증과 인가

인증과 인가의 책임을 구분한다.

- Authentication: 요청자의 신원 확인
- Authorization: 요청자의 접근 권한 확인

Spring Security가 사용되는 프로젝트에서는
기존 보안 설정 및 권한 정책을 준수한다.

기본적으로 보호해야 하는 리소스의 접근은
명시적으로 허용된 경우에만 제공한다.

### 12.2. 접근 제어

URL 접근 권한뿐 아니라
리소스 단위 접근 권한도 확인한다.

예시:

- 본인의 회원 정보만 수정 가능
- 상담 참여자만 상담 내역 조회 가능
- 병원 관리자는 본인 병원 정보만 관리 가능

사용자가 전달한 회원 ID 또는 역할 값을
인증된 사용자 정보로 신뢰하지 않는다.

필요한 경우 Application 또는 Domain에서
업무상 접근 권한을 검증한다.

### 12.3. 비밀번호 관리

- 비밀번호 평문 저장 금지
- 비밀번호 응답 반환 금지
- 비밀번호 로그 출력 금지
- 적절한 적응형 단방향 해시 사용
- Spring Security PasswordEncoder 활용

BCrypt 또는 프로젝트에서 승인한
안전한 비밀번호 해시 알고리즘을 사용한다.

### 12.4. JWT 및 세션

JWT가 사용되는 프로젝트에서는
다음 항목을 필요한 정책에 따라 검증한다.

- 서명
- 허용된 알고리즘
- 만료 시간
- 발급자
- 대상 서비스
- 토큰 유형

JWT Payload를 암호화된 데이터로 간주하지 않는다.

서명 검증 없는 토큰을 신뢰하지 않는다.

토큰 만료 및 갱신 정책은 context.md를 기준으로 한다.

### 12.5. CORS 및 CSRF

- 허용할 Origin을 명확히 설정한다.
- 인증 정보를 포함하는 CORS 정책을 신중히 설정한다.
- 인증 방식에 맞게 CSRF 보호 정책을 결정한다.
- 문제 해결을 위해 모든 보안 설정을 비활성화하지 않는다.
- 개발 환경의 임시 허용 설정을 운영에 적용하지 않는다.

### 12.6. 민감정보 관리

다음 정보를 소스 코드 및 Git에 저장하지 않는다.

- API Key
- Access Token
- Refresh Token
- Database Password
- AWS Access Key
- JWT Signing Secret
- 개인 식별 정보
- 실제 운영 데이터

환경 변수 또는 승인된 Secret 관리 도구를 사용한다.

예시 설정에는 실제 값이 아닌
Placeholder를 사용한다.

### 12.7. 외부 요청 및 입력 보안

다음 취약점 가능성을 검토한다.

- SQL Injection
- XSS
- CSRF
- SSRF
- Path Traversal
- Broken Access Control
- Mass Assignment
- 무제한 파일 업로드
- 민감정보 노출

외부 URL 호출에는 필요에 따라
허용 범위와 Timeout을 설정한다.

요청 DTO를 통해 변경 가능한 필드를 제한한다.

보안 취약점을 해결하기 위해
업무상 필요한 권한 검증을 제거하지 않는다.

## 13. 테스트 코드 작성 규칙

### 13.1. 테스트 기본 원칙

테스트는 구현 내용이 아니라
요구사항과 기대 동작을 검증한다.

다음 원칙을 준수한다.

- 독립적인 테스트
- 반복 실행 가능한 테스트
- 결정적인 결과
- 명확한 테스트 목적
- 실패 원인을 파악하기 쉬운 구조
- 실제 인수 조건에 대한 검증

신규 기능에는 필요한 테스트를 작성한다.

버그 수정에는 가능한 경우
회귀 테스트를 추가한다.

### 13.2. 테스트 도구

기본적으로 다음 도구를 사용한다.

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc

필요한 경우 다음 도구를 검토한다.

- AssertJ
- Testcontainers
- ArchUnit

기존 프로젝트의 테스트 도구를 우선한다.

존재하지 않는 도구를 사용했다고 보고하지 않는다.

### 13.3. Domain 단위 테스트

Domain 테스트는 Spring Context 없이 수행한다.

검증 대상:

- Domain Entity 상태 변경
- Value Object 유효성
- 비즈니스 정책
- 상태 전이
- 도메인 예외
- 경계값

일반적인 Domain 테스트에
@SpringBootTest를 사용하지 않는다.

### 13.4. Application 단위 테스트

Application Service는 필요한 Output Port를
Mock 또는 Fake로 대체하여 테스트한다.

검증 대상:

- 유스케이스 정상 동작
- 도메인 호출 및 작업 흐름
- 존재하지 않는 데이터
- 중복 데이터
- 업무 조건 위반
- Output Port 호출 결과
- 외부 오류 처리

구현 세부사항에 대한 과도한 verify를 지양한다.

비즈니스 결과와 필요한 상호작용을 검증한다.

### 13.5. Web Adapter 테스트

Web 계층 테스트는 필요한 경우
@WebMvcTest 및 MockMvc를 사용한다.

검증 대상:

- HTTP Method 및 URI
- Request DTO 변환
- 입력 검증
- HTTP Status
- Response DTO
- 인증 및 인가
- 예외 응답

Controller 테스트에서
실제 데이터베이스 접근을 기본적으로 수행하지 않는다.

### 13.6. Persistence Adapter 테스트

Persistence 관련 테스트는
필요한 경우 @DataJpaTest를 사용한다.

Custom Query, Mapper, Adapter 구현 및
제약조건 동작을 검증한다.

필요한 구성요소는 명시적으로 테스트에 포함한다.

MySQL 고유 문법, 인덱스, 잠금 또는
데이터베이스 특화 기능을 검증하는 경우
MySQL 기반 통합 테스트를 우선 검토한다.

H2 테스트 성공만으로
MySQL 호환성을 보장한다고 판단하지 않는다.

### 13.7. 통합 테스트

여러 구성요소 간 실제 동작이 중요한 경우
@SpringBootTest 등을 활용한다.

검증 대상:

- 전체 유스케이스 흐름
- 트랜잭션
- 데이터베이스 연동
- Spring Security 설정
- 이벤트 발행 및 소비
- 외부 시스템 연동 계약
- 동시성
- 실제 Bean 구성

모든 테스트를 통합 테스트로 작성하지 않는다.

단위 테스트로 충분한 검증은
단위 테스트를 우선한다.

### 13.8. 테스트 작성 형식

Given - When - Then 구조를 기본으로 사용한다.

예시:

    @Test
    @DisplayName("정상적인 닉네임으로 변경할 수 있다")
    void changeNickname_success() {

        // Given
        Member member = createMember();

        // When
        member.changeNickname("newNickname");

        // Then
        assertEquals(
            "newNickname",
            member.getNickname()
        );
    }

테스트 이름으로 검증 목적을 이해할 수 있어야 한다.

테스트 데이터는 테스트별로 독립적으로 관리한다.

필요에 따라 Fixture 또는 Test Builder를 사용한다.

### 13.9. 필수 테스트 시나리오

기능의 특성에 따라 다음 시나리오를 검토한다.

- 정상 요청
- 필수값 누락
- 유효하지 않은 입력
- 경계값
- 존재하지 않는 데이터
- 중복 데이터
- 권한 없는 요청
- 상태 전이 실패
- 외부 시스템 오류
- 트랜잭션 롤백
- 동시 요청
- 중복 요청

모든 기능에 모든 시나리오를
기계적으로 적용하지 않는다.

적용 대상에서 제외한 중요한 시나리오는
필요한 경우 제외 사유를 설명한다.

### 13.10. Mock 사용 규칙

- 실제 검증 대상 객체는 Mock으로 대체하지 않는다.
- 외부 의존성을 격리할 때 Mock을 사용한다.
- Mock의 반환값만 확인하는 무의미한 테스트를 피한다.
- 실제 코드의 결과를 검증한다.
- Mock 기반 테스트를 실제 시스템 연동 테스트로 보고하지 않는다.

### 13.11. 테스트 안정성

다음 행위를 금지한다.

- 테스트 실패를 숨기기 위한 테스트 삭제
- 근거 없는 @Disabled 사용
- 실제 기대 결과에 맞지 않는 Assertion 수정
- 의미 없는 Assertion
- 테스트 실행 순서에 대한 의존
- 운영 데이터베이스를 사용하는 테스트
- 검증 없이 외부 네트워크에 의존하는 테스트

테스트 실행 시간이 필요한 경우
적절한 Timeout을 설정한다.

불안정한 테스트를 단순 반복 실행하여
성공으로 처리하지 않는다.

### 13.12. 테스트 커버리지

커버리지 수치는 품질 참고 지표로 사용한다.

핵심 비즈니스 로직과 실패 경로에 대한
의미 있는 검증을 우선한다.

프로젝트에 최소 커버리지 기준이 정의되어 있다면
해당 기준을 충족해야 한다.

커버리지를 높이기 위한 무의미한 테스트를 작성하지 않는다.

커버리지 도구가 없는 상태에서
커버리지 수치를 임의로 보고하지 않는다.

## 14. 아키텍처 검증 규칙

### 14.1. 검증 대상

Reviewer는 다음 항목을 확인한다.

- Domain의 기술 독립성
- Application의 Adapter 직접 의존 여부
- Controller의 Repository 직접 호출 여부
- JPA Entity의 외부 노출 여부
- Domain 간 순환 의존 여부
- Input Port 및 Output Port 계약 준수
- 계층별 책임 분리
- SOLID 원칙 위반 여부

### 14.2. ArchUnit

프로젝트에 ArchUnit이 설정되어 있으면
아키텍처 테스트를 실행한다.

검증 가능한 규칙은 테스트로 자동화하는 것을 권장한다.

예시:

- Domain은 Spring에 의존하지 않는다.
- Domain은 JPA에 의존하지 않는다.
- Application은 Adapter에 의존하지 않는다.
- Controller는 Persistence 구현체에 의존하지 않는다.
- 도메인 모듈 간 순환 의존이 없어야 한다.

ArchUnit이 없는 경우에는 코드 및 import 분석을
통해 아키텍처 위반 여부를 검토한다.

수동 검토를 자동화 테스트 성공으로 보고하지 않는다.

ArchUnit 도입 여부는 프로젝트 설정과
사용자 요구사항을 기준으로 결정한다.

## 15. 성능 및 리소스 관리

### 15.1. 기본 원칙

측정 가능한 성능 문제를 우선 해결한다.

실제 요구사항이나 근거 없이
복잡한 성능 최적화를 수행하지 않는다.

### 15.2. 데이터 조회 성능

- 불필요한 반복 조회를 피한다.
- N+1 문제를 검토한다.
- 대량 데이터를 한 번에 로딩하지 않는다.
- 필요한 경우 Batch 및 Pagination을 사용한다.
- 목록 조회에 불필요한 컬럼을 포함하지 않는다.

### 15.3. 캐시

캐시는 다음 사항을 고려하여 적용한다.

- 실제 조회 빈도
- 데이터 변경 빈도
- 허용 가능한 데이터 지연
- 캐시 무효화 정책
- TTL
- 장애 시 처리 정책
- 개인정보 및 권한 정보

모든 조회 기능에 캐시를 적용하지 않는다.

### 15.4. 외부 시스템 연동

- Connection Timeout 설정
- Read Timeout 설정
- 필요한 경우 제한된 Retry 설정
- 외부 오류에 대한 예외 처리
- 리소스 해제
- 연결 및 요청 수 관리

외부 시스템의 무응답이
전체 서비스 장애로 확산되지 않도록 고려한다.

## 16. 로깅 규칙

### 16.1. 로그 작성

프로젝트의 표준 로깅 프레임워크를 사용한다.

System.out.println을 일반적인
애플리케이션 로깅 용도로 사용하지 않는다.

필요한 로그는 구조적으로 작성한다.

예시:

    log.info("Member registration completed. memberId={}",
        memberId);

### 16.2. 로그 레벨

- TRACE: 매우 상세한 추적 정보
- DEBUG: 개발 및 디버깅 정보
- INFO: 정상적인 주요 작업 처리 정보
- WARN: 주의가 필요한 예상 가능한 문제
- ERROR: 조치가 필요한 시스템 오류

정상적인 비즈니스 예외를
무조건 ERROR로 기록하지 않는다.

동일한 예외를 여러 계층에서 중복 기록하지 않는다.

### 16.3. 민감정보 로깅 금지

다음 정보를 로그에 기록하지 않는다.

- 비밀번호
- 인증 토큰
- API Key
- 결제정보
- 불필요한 개인정보
- 민감한 요청 및 응답 본문

필요한 경우 마스킹을 적용한다.

사용자 입력값으로 인해
로그 구조가 훼손되지 않도록 주의한다.

### 16.4. 추적 가능성

필요한 경우 다음 식별자를 활용한다.

- Request ID
- Trace ID
- 업무 처리 식별자
- 이벤트 식별자

민감정보가 아닌 식별자를 활용하여
오류 추적이 가능하도록 설계한다.

## 17. 빌드 및 의존성 관리

### 17.1. Gradle

Gradle Wrapper가 존재하는 경우 이를 사용한다.

Unix 또는 Git Bash:

    ./gradlew compileJava
    ./gradlew test
    ./gradlew build

Windows:

    .\gradlew.bat compileJava
    .\gradlew.bat test
    .\gradlew.bat build

프로젝트의 실제 빌드 도구 및 운영체제를 확인한다.

빌드 성공 여부는 명령어 실행 결과와
종료 코드를 기준으로 판단한다.

### 17.2. 의존성 추가

새로운 라이브러리를 추가하기 전에
다음 사항을 검토한다.

- 실제 필요성
- 기존 기능으로 대체 가능 여부
- Java 17 호환성
- Spring Boot 호환성
- 보안 취약점
- 유지보수 상태
- 라이선스
- 프로젝트 전체 영향

사용하지 않는 의존성을 추가하지 않는다.

검증 없이 프레임워크 버전을 변경하지 않는다.

SNAPSHOT 또는 사전 공개 버전은
명시적인 필요와 승인 없이 사용하지 않는다.

### 17.3. 품질 검사 도구

프로젝트에 설정되어 있는 경우 다음을 활용한다.

- Checkstyle
- SpotBugs
- PMD
- JaCoCo
- ArchUnit

검사를 통과하기 위해 검사 규칙을
무단으로 완화하거나 비활성화하지 않는다.

품질 검사 도구가 설정되어 있지 않다면
NOT_CONFIGURED 또는 NOT_RUN으로 보고한다.

필수 검증 도구인 경우에는 BLOCKED로 판단한다.

## 18. Git 및 변경 관리

### 18.1. 변경 전 확인

작업 전에 Git 상태를 확인한다.

- 현재 브랜치
- 수정된 파일
- 추적되지 않는 파일
- 사용자 작업 내용
- 작업 범위

기존 변경사항을 임의로 되돌리지 않는다.

### 18.2. 변경 범위

- 요청과 관련된 파일만 수정한다.
- 무관한 전체 프로젝트 포맷팅을 수행하지 않는다.
- 불필요한 파일 이동 및 이름 변경을 피한다.
- 기존 API 계약의 변경 여부를 확인한다.
- 생성 파일과 임시 파일의 Git 포함 여부를 검토한다.

### 18.3. Git 작업

사용자 승인 또는 프로젝트 정책 없이
다음 작업을 수행하지 않는다.

- Git push
- Force push
- Hard reset
- Git clean
- 브랜치 삭제
- Git 기록 재작성
- 실제 배포

커밋은 사용자 요청 및 프로젝트 정책에 따라 수행한다.

### 18.4. 병렬 작업

동일 파일에 대한 동시 수정을 방지한다.

독립적으로 수정 가능한 작업 범위를 정의한다.

공통 설정, 인터페이스 및 DB 스키마 변경은
작업 간 의존성을 먼저 확인한다.

병렬 작업 통합 후 전체 검증을 다시 수행한다.

## 19. 코드 리뷰 규칙

### 19.1. Reviewer 검토 대상

Reviewer는 다음 항목을 검토한다.

- 사용자 요구사항
- 인수 조건
- 변경된 코드
- 관련 기존 코드
- 아키텍처
- SOLID 원칙
- 데이터 무결성
- 예외 처리
- 보안
- 트랜잭션
- 동시성
- 테스트
- 빌드 결과
- 기존 기능에 대한 회귀 가능성

실제 변경 코드와 검증 결과를 기반으로 판단한다.

### 19.2. 심각도 분류

CRITICAL:

- 심각한 보안 취약점
- 데이터 손실 위험
- 광범위한 무단 접근
- 핵심 데이터 무결성 파괴

HIGH:

- 핵심 기능 동작 실패
- 주요 요구사항 누락
- 중대한 아키텍처 위반
- 필수 테스트 실패
- 중대한 트랜잭션 또는 동시성 결함
- 인증 및 인가 검증 누락

MEDIUM:

- 제한적인 기능 결함
- 유지보수성을 저해하는 설계
- 일부 예외 처리 누락
- 비효율적인 구현
- 개선이 필요한 테스트

LOW:

- 사소한 코드 컨벤션 위반
- 가독성 개선 제안
- 경미한 네이밍 문제
- 선택적인 문서 개선

### 19.3. 리뷰 근거

모든 결함은 다음 정보를 포함한다.

- Severity
- File
- Line
- Rule ID
- Description
- Evidence
- Recommendation
- Blocking 여부

Rule ID는 위반한 규칙의 절 번호를 사용한다.

예시:

    Severity: HIGH
    File: MemberController.java
    Line: 실제 발견한 라인 번호
    Rule ID: 4.7, 4.9, 5.5
    Description: Controller가 JPA Repository 직접 호출
    Evidence: 실제 코드에서 발견된 의존성
    Recommendation: Input Port를 통한 유스케이스 호출
    Blocking: true

존재하지 않는 파일과 라인 번호를
임의로 생성하지 않는다.

검증하지 못한 문제는 확정된 결함과 구분한다.

### 19.4. 리뷰 결과 판단

다음 중 하나를 반환한다.

PASS:

- 필수 개발 규칙 준수
- 필수 인수 조건 충족
- 차단 결함 없음
- 필수 검증 근거 확보

FAIL:

- 차단 결함 존재
- 필수 규칙 위반
- 필수 요구사항 미충족
- 필수 테스트 실패 또는 누락

BLOCKED:

- 필수 환경 또는 정보 부족
- 필수 검증 도구 실행 불가
- 권한 문제로 인한 검증 불가

CRITICAL 및 HIGH 실제 결함은
수정되기 전까지 PASS 처리하지 않는다.

MEDIUM 및 LOW라도 필수 규칙 위반 또는
인수 조건 미충족에 해당하면 FAIL 처리한다.

선택적인 개선 제안은
실제 차단 결함과 명확히 구분한다.

### 19.5. 리뷰 금지 사항

- 개인적인 취향을 필수 규칙으로 간주
- 근거 없는 SOLID 위반 판정
- 불필요한 디자인 패턴 적용 강요
- 요청 범위를 벗어난 전면 리팩터링 요구
- 실행하지 않은 테스트 성공 보고
- 존재하지 않는 보안 문제 생성
- 검토 대상 코드의 직접 수정
- 동일한 결함의 불필요한 중복 보고

## 20. 기술적 작업 완료 기준

다음 항목을 검증한다.

### 20.1. 기능

- 사용자 요구사항 충족
- 필수 인수 조건 충족
- 핵심 비즈니스 규칙 구현
- 필요한 예외 처리 구현

### 20.2. 아키텍처

- Hexagonal Architecture 준수
- Domain 기술 독립성 유지
- 올바른 의존성 방향
- 계층별 책임 분리
- 순환 의존성 없음

### 20.3. 코드 품질

- SOLID 원칙 준수
- 불필요한 중복 없음
- 명확한 네이밍
- 읽기 쉬운 구현
- 불필요한 의존성 없음

### 20.4. 테스트

- 필요한 단위 테스트 작성
- 필요한 통합 테스트 작성
- 실패 및 예외 시나리오 검증
- 기존 테스트 통과
- 필수 테스트 누락 없음

### 20.5. 보안 및 데이터

- 민감정보 노출 없음
- 필요한 인증 및 인가 검증
- 데이터 무결성 유지
- 필요한 동시성 및 멱등성 검증

### 20.6. 빌드 및 리뷰

- 컴파일 성공
- 필수 테스트 성공
- 빌드 성공
- 설정된 필수 품질 검사 통과
- Reviewer PASS

위 항목 중 적용 대상인 필수 검증에 실패하면
개발 완료로 판단하지 않는다.

검증 결과를 확인할 수 없는 경우
AGENTS.md의 BLOCKED 처리 규칙을 따른다.

선택적 검증 도구가 설정되어 있지 않은 경우에는
미설정 상태와 대체 검토 방식을 보고한다.

최종 작업 상태 및 수정 루프는
AGENTS.md와 harness/workflow.md를 기준으로 한다.
