package com.example.membermanagement.member.adapter.out.persistence;

import java.sql.SQLException;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;

import com.example.membermanagement.member.adapter.out.persistence.entity.MemberJpaEntity;
import com.example.membermanagement.member.adapter.out.persistence.mapper.MemberPersistenceMapper;
import com.example.membermanagement.member.adapter.out.persistence.repository.MemberJpaRepository;
import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberPersistenceAdapterTest {

    private final MemberJpaRepository repository = mock(MemberJpaRepository.class);
    private final MemberPersistenceAdapter adapter = new MemberPersistenceAdapter(repository,
            new MemberPersistenceMapper());
    private final Member member = Member.register(new Email("member@example.com"), new Nickname("회원"), Instant.now());

    @Test
    void convertsOnlyEmailUniqueFailure() {
        SQLException duplicate = new SQLException("Duplicate entry for key 'uk_members_email'", "23000", 1062);
        when(repository.saveAndFlush(any(MemberJpaEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate", duplicate));

        assertThatThrownBy(() -> adapter.save(member)).isInstanceOf(DuplicateEmailException.class)
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void preservesOtherIntegrityFailures() {
        SQLException otherConstraint = new SQLException("Duplicate entry for key 'PRIMARY'", "23000", 1062);
        DataIntegrityViolationException failure = new DataIntegrityViolationException("primary key", otherConstraint);
        when(repository.saveAndFlush(any(MemberJpaEntity.class))).thenThrow(failure);

        assertThatThrownBy(() -> adapter.save(member)).isSameAs(failure);
    }

    @Test
    void returnsEmptyLargePageUsingCountWithoutLoadingEntities() {
        when(repository.count()).thenReturn(3L);

        var result = adapter.loadPage(Integer.MAX_VALUE, 2);

        assertThat(result.content()).isEmpty();
        assertThat(result.page()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.totalElements()).isEqualTo(3);
        assertThat(result.totalPages()).isEqualTo(2);
        verify(repository).count();
        verify(repository, never()).findAll(any(Pageable.class));
    }
}
