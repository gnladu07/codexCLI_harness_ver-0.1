package com.example.membermanagement.member.adapter.out.persistence.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.membermanagement.member.adapter.out.persistence.entity.MemberJpaEntity;
import com.example.membermanagement.member.domain.model.Email;
import com.example.membermanagement.member.domain.model.Member;
import com.example.membermanagement.member.domain.model.Nickname;

@Component
public class MemberPersistenceMapper {

    public MemberJpaEntity toEntity(Member member) {
        return new MemberJpaEntity(member.memberId().toString(), member.email().value(), member.nickname().value(),
                LocalDateTime.ofInstant(member.createdAt(), ZoneOffset.UTC),
                LocalDateTime.ofInstant(member.updatedAt(), ZoneOffset.UTC));
    }

    public Member toDomain(MemberJpaEntity entity) {
        return Member.restore(UUID.fromString(entity.getMemberId()), new Email(entity.getEmail()),
                new Nickname(entity.getNickname()), entity.getCreatedAt().toInstant(ZoneOffset.UTC),
                entity.getUpdatedAt().toInstant(ZoneOffset.UTC));
    }
}
