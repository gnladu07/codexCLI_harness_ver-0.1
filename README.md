# Member Management

STAGE-001은 회원 관리 애플리케이션의 로컬 개발 환경을 구성합니다. MySQL에서 Flyway로 `members` 테이블을 생성합니다. 회원 API와 유스케이스는 STAGE-002부터 추가합니다.

## 준비물

- JDK 17. `JAVA_HOME`에 JDK 17 경로를 설정합니다.
- Docker Engine과 Docker Compose v2 이상. Windows에서는 Docker Desktop의 Linux 컨테이너를 사용합니다.
- 첫 빌드에서는 Gradle 배포본과 의존성, 첫 MySQL 실행에서는 Docker 이미지 다운로드가 필요합니다.

Gradle Wrapper 8.14.3, Spring Boot 3.5.16, MySQL 8.0을 사용합니다. Spring Boot BOM이 Testcontainers와 Flyway를 포함한 의존성 버전을 관리하며 ArchUnit은 Java 17에서 실행되는 1.5.1로 고정합니다.

## 로컬 실행

Git Bash 또는 macOS/Linux 셸에서는 `cp .env.example .env`, PowerShell에서는 `Copy-Item .env.example .env`를 실행합니다. 생성한 `.env`에 다음 값을 설정합니다.

| 변수 | 설정값 |
| --- | --- |
| `DB_URL` | `jdbc:mysql://127.0.0.1:3307/member_management` |
| `DB_USERNAME` | 로컬 MySQL 일반 사용자 이름. 예시 `member_app` |
| `DB_PASSWORD` | 로컬 일반 사용자 비밀번호 |
| `MYSQL_ROOT_PASSWORD` | MySQL 초기화에 사용할 별도의 로컬 root 비밀번호 |

프로젝트 루트에서 다음 명령으로 MySQL을 시작합니다. `--wait`는 애플리케이션 계정으로 SQL을 실행할 수 있을 때까지 기다립니다.

```sh
docker compose up -d --wait
docker compose ps
```

Compose는 프로젝트 루트의 `.env`를 읽습니다. Spring Boot 실행 전에는 동일한 DB 접속 정보를 실행 셸의 환경 변수로 설정합니다. 아래 사용자 이름과 비밀번호 예시는 자신의 `.env` 값으로 바꿉니다.

### Git Bash 및 macOS/Linux

```bash
export DB_URL='jdbc:mysql://127.0.0.1:3307/member_management'
export DB_USERNAME='member_app'
printf 'MySQL application password: '
read -r -s DB_PASSWORD
printf '\n'
export DB_PASSWORD
./gradlew bootRun
```

### PowerShell

```powershell
$env:DB_URL = 'jdbc:mysql://127.0.0.1:3307/member_management'
$env:DB_USERNAME = 'member_app'
$password = Read-Host 'MySQL application password' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $password).Password
Remove-Variable password
.\gradlew.bat bootRun
```

Spring Boot는 기본 `local` 프로필로 `127.0.0.1:8080`에서 시작합니다. 시작 로그에서 Flyway의 V1 적용 또는 최신 스키마 확인 메시지를 확인할 수 있습니다. 별도 터미널에서 `curl -i http://127.0.0.1:8080/`을 실행하면 등록된 API가 없는 현재 단계에서는 HTTP 404가 반환됩니다. Windows PowerShell에서는 `curl.exe`를 사용합니다.

애플리케이션은 `Ctrl+C`, MySQL은 `docker compose down`으로 종료합니다. MySQL 데이터는 이름이 지정된 `mysql-data` 볼륨에 보존됩니다. 사용자와 비밀번호 초기화 값은 빈 볼륨으로 처음 실행할 때 적용됩니다. 기존 볼륨의 계정 정보와 `.env`가 다르면 기존 자격 증명을 확인합니다.

## 검증

```sh
./gradlew --version
./gradlew compileJava
./gradlew test
./gradlew build
```

PowerShell에서는 `./gradlew` 대신 `.\gradlew.bat`를 사용합니다. `test`와 `build`는 ArchUnit 검사와 MySQL 통합 테스트를 실행합니다. MySQL 통합 테스트에는 Docker가 필요하며, Testcontainers가 임의 포트에 독립 MySQL 8.0 컨테이너를 실행합니다. 테스트는 Flyway V1 적용, 컬럼 정의, 이메일 고유 제약과 정렬 인덱스를 검사합니다.

`docker compose config --quiet`로 구성과 필수 변수 입력을 확인할 수 있습니다. JDBC 연결이 실패하면 `docker compose ps`에서 MySQL 상태와 `.env`의 계정 정보, 실행 셸의 환경 변수를 확인합니다.

## 패키지 구조

기본 패키지는 `com.example.membermanagement`입니다.

| 위치 | 책임 |
| --- | --- |
| `MemberManagementApplication` | Spring Boot 시작 |
| `member.domain` | 순수 Java 회원 모델과 도메인 정책 |
| `member.application` | 유스케이스와 입출력 포트 |
| `member.adapter.in.web` | HTTP 변환과 입력 포트 호출 |
| `member.adapter.out.persistence` | 출력 포트 구현과 JPA 매핑 |

ArchUnit은 실제 애플리케이션 시작 클래스의 위치와 회원 모듈 의존성, 영속성 기술 의존 경계를 검사합니다. Domain의 Spring 독립성과 Application 및 Adapter 간의 의존 방향은 해당 구현 타입을 추가하는 단계에서 검증합니다. Hibernate의 `validate` 모드는 JPA 매핑이 추가되면 Flyway가 구성한 스키마를 기준으로 검사합니다.
