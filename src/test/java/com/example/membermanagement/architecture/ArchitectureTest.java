package com.example.membermanagement.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {

    private static final String BASE_PACKAGE = "com.example.membermanagement";
    private static final String PERSISTENCE_PACKAGE = BASE_PACKAGE + ".member.adapter.out.persistence..";
    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASE_PACKAGE);

    @Test
    @DisplayName("스프링 부트 시작 클래스는 애플리케이션 최상위 패키지에 위치한다")
    void springBootApplicationIsInTheBasePackage() {
        classes().that().areAnnotatedWith(SpringBootApplication.class)
                .should().resideInAPackage(BASE_PACKAGE)
                .check(PRODUCTION_CLASSES);
    }

    @Test
    @DisplayName("스프링 부트 시작 클래스는 회원 모듈에 직접 의존하지 않는다")
    void springBootApplicationDoesNotDependOnMemberModule() {
        noClasses().that().areAnnotatedWith(SpringBootApplication.class)
                .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + ".member..")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    @DisplayName("JPA와 Spring Data 저장소 의존성은 영속성 어댑터 안에 둔다")
    void persistenceDependenciesStayWithinThePersistenceAdapter() {
        noClasses().that().resideOutsideOfPackage(PERSISTENCE_PACKAGE)
                .should().dependOnClassesThat().resideInAnyPackage(
                        "jakarta.persistence..",
                        "org.springframework.data.jpa.repository..",
                        "org.springframework.data.repository..")
                .check(PRODUCTION_CLASSES);
    }
}
