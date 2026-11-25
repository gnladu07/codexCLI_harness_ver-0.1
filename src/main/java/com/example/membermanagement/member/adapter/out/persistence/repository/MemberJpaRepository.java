package com.example.membermanagement.member.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.membermanagement.member.adapter.out.persistence.entity.MemberJpaEntity;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    boolean existsByEmail(String email);

    @Modifying
    @Query("delete from MemberJpaEntity member where member.memberId = :memberId")
    int deleteRowByMemberId(@Param("memberId") String memberId);
}
