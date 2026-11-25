package com.example.membermanagement.member.adapter.out.persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.membermanagement.member.adapter.out.persistence.mapper.MemberPersistenceMapper;
import com.example.membermanagement.member.adapter.out.persistence.repository.MemberJpaRepository;
import com.example.membermanagement.member.application.exception.DuplicateEmailException;
import com.example.membermanagement.member.application.port.out.DeleteMemberPort;
import com.example.membermanagement.member.application.port.out.ExistsMemberByEmailPort;
import com.example.membermanagement.member.application.port.out.LoadMemberPagePort;
import com.example.membermanagement.member.application.port.out.LoadMemberPort;
import com.example.membermanagement.member.application.port.out.SaveMemberPort;
import com.example.membermanagement.member.application.result.MemberPageResult;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;

@Component
public class MemberPersistenceAdapter implements ExistsMemberByEmailPort, SaveMemberPort,
        LoadMemberPort, LoadMemberPagePort, DeleteMemberPort {

    private static final String EMAIL_UNIQUE_CONSTRAINT = "uk_members_email";
    private static final int MYSQL_DUPLICATE_KEY = 1062;

    private final MemberJpaRepository repository;
    private final MemberPersistenceMapper mapper;

    public MemberPersistenceAdapter(MemberJpaRepository repository, MemberPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByEmail(Email email) {
        return repository.existsByEmail(email.value());
    }

    @Override
    public Optional<Member> loadById(UUID memberId) {
        return repository.findById(memberId.toString()).map(mapper::toDomain);
    }

    @Override
    public boolean deleteById(UUID memberId) {
        return repository.deleteRowByMemberId(memberId.toString()) > 0;
    }

    @Override
    public MemberPageResult loadPage(int page, int size) {
        // JPA's first-result parameter is an int; a valid page beyond that offset is empty.
        if ((long) page * size > Integer.MAX_VALUE) {
            long totalElements = repository.count();
            return new MemberPageResult(List.of(), page, size, totalElements, totalPages(totalElements, size));
        }
        Sort order = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.asc("memberId"));
        Page<Member> members = repository.findAll(PageRequest.of(page, size, order)).map(mapper::toDomain);
        return new MemberPageResult(members.getContent(), page, size, members.getTotalElements(),
                totalPages(members.getTotalElements(), size));
    }

    private long totalPages(long totalElements, int size) {
        return totalElements / size + (totalElements % size == 0 ? 0 : 1);
    }

    @Override
    public Member save(Member member) {
        try {
            // Flush here so the MySQL UNIQUE failure is observed within this port call.
            return mapper.toDomain(repository.saveAndFlush(mapper.toEntity(member)));
        } catch (DataIntegrityViolationException exception) {
            if (isEmailUniqueViolation(exception)) {
                throw new DuplicateEmailException(exception);
            }
            throw exception;
        }
    }

    private boolean isEmailUniqueViolation(Throwable failure) {
        boolean duplicateKey = false;
        boolean emailConstraint = false;
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof SQLException sql && sql.getErrorCode() == MYSQL_DUPLICATE_KEY) {
                duplicateKey = true;
                emailConstraint |= sql.getMessage() != null && sql.getMessage().contains(EMAIL_UNIQUE_CONSTRAINT);
            }
            if (cause instanceof ConstraintViolationException constraint) {
                String name = constraint.getConstraintName();
                emailConstraint |= name != null && name.contains(EMAIL_UNIQUE_CONSTRAINT);
            }
        }
        return duplicateKey && emailConstraint;
    }
}
