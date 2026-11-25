package com.example.membermanagement.member.adapter.out.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "members", uniqueConstraints = @UniqueConstraint(name = "uk_members_email", columnNames = "email"))
public class MemberJpaEntity {

    @Id
    @Column(name = "member_id", nullable = false, length = 36, columnDefinition = "char(36)")
    private String memberId;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime updatedAt;

    protected MemberJpaEntity() {
    }

    public MemberJpaEntity(String memberId, String email, String nickname, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
