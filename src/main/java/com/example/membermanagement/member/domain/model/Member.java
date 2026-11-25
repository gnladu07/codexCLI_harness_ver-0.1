package com.example.membermanagement.member.domain.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public final class Member {

    private final UUID memberId;
    private final Email email;
    private final Nickname nickname;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Member(UUID memberId, Email email, Nickname nickname, Instant createdAt, Instant updatedAt) {
        this.memberId = Objects.requireNonNull(memberId, "memberId");
        this.email = Objects.requireNonNull(email, "email");
        this.nickname = Objects.requireNonNull(nickname, "nickname");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public static Member register(Email email, Nickname nickname, Instant now) {
        Instant registeredAt = Objects.requireNonNull(now, "now").truncatedTo(ChronoUnit.MICROS);
        return new Member(UUID.randomUUID(), email, nickname, registeredAt, registeredAt);
    }

    public static Member restore(UUID memberId, Email email, Nickname nickname, Instant createdAt, Instant updatedAt) {
        return new Member(memberId, email, nickname, createdAt, updatedAt);
    }

    public UUID memberId() {
        return memberId;
    }

    public Email email() {
        return email;
    }

    public Nickname nickname() {
        return nickname;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
