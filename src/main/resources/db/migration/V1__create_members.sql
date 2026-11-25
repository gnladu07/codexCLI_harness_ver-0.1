CREATE TABLE members (
    member_id CHAR(36) NOT NULL,
    email VARCHAR(254)
        COLLATE utf8mb4_bin NOT NULL,
    nickname VARCHAR(30) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (member_id),

    CONSTRAINT uk_members_email
        UNIQUE (email),

    INDEX idx_members_created_at_member_id (
        created_at DESC,
        member_id ASC
    )
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4;
