-- =====================================================
-- V1__init_schema.sql
-- Initial schema for the auth platform
-- =====================================================

CREATE TABLE IF NOT EXISTS tenants (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tenant_code     VARCHAR(100)    NOT NULL UNIQUE,
    name            VARCHAR(255)    NOT NULL,
    description     VARCHAR(500),
    enabled         TINYINT(1)      NOT NULL DEFAULT 1,
    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id                      BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username                VARCHAR(100)    NOT NULL,
    email                   VARCHAR(255)    NOT NULL,
    password                VARCHAR(255)    NOT NULL,
    first_name              VARCHAR(100),
    last_name               VARCHAR(100),
    mobile_number           VARCHAR(30),
    recovery_email          VARCHAR(255),
    profile_image           VARCHAR(500),
    email_verified          TINYINT(1)      NOT NULL DEFAULT 0,
    mobile_verified         TINYINT(1)      NOT NULL DEFAULT 0,
    account_locked          TINYINT(1)      NOT NULL DEFAULT 0,
    account_locked_at       DATETIME(6),
    account_expired         TINYINT(1)      NOT NULL DEFAULT 0,
    credentials_expired     TINYINT(1)      NOT NULL DEFAULT 0,
    enabled                 TINYINT(1)      NOT NULL DEFAULT 1,
    failed_login_attempts   INT             NOT NULL DEFAULT 0,
    last_login              DATETIME(6),
    active                  TINYINT(1)      NOT NULL DEFAULT 1,
    deleted                 TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id               BIGINT,
    created_at              DATETIME(6),
    updated_at              DATETIME(6)     NOT NULL,
    created_by              VARCHAR(100)    NOT NULL,
    updated_by              VARCHAR(100)    NOT NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_tenant_id (tenant_id)
);

CREATE TABLE IF NOT EXISTS roles (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL UNIQUE,
    description     VARCHAR(500),
    is_system_role  TINYINT(1)      NOT NULL DEFAULT 0,

    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,

    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    permission_code VARCHAR(100)    NOT NULL UNIQUE,
    permission_name VARCHAR(100)    NOT NULL,
    description     VARCHAR(500),
    module          VARCHAR(100),

    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,

    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,

    user_id         BIGINT          NOT NULL,
    role_id         BIGINT          NOT NULL,

    primary_role    TINYINT(1)      NOT NULL DEFAULT 0,
    assigned_at     DATETIME(6),
    expires_at      DATETIME(6),

    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,

    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL,

    UNIQUE KEY uq_user_role (user_id, role_id),

    CONSTRAINT fk_ur_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ur_role
        FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS role_permissions (
    id              BIGINT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_id         BIGINT  NOT NULL,
    permission_id   BIGINT  NOT NULL,
    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL,
    UNIQUE KEY uq_role_permission (role_id, permission_id),
    CONSTRAINT fk_rp_role       FOREIGN KEY (role_id)       REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(500)    NOT NULL UNIQUE,
    user_id     BIGINT          NOT NULL,
    expires_at  DATETIME(6)     NOT NULL,
    revoked     TINYINT(1)      NOT NULL DEFAULT 0,
    active      TINYINT(1)      NOT NULL DEFAULT 1,
    deleted     TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id   BIGINT,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)     NOT NULL,
    created_by  VARCHAR(100)    NOT NULL,
    updated_by  VARCHAR(100)    NOT NULL,
    INDEX idx_rt_token (token),
    INDEX idx_rt_user  (user_id),
    CONSTRAINT fk_rt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_sessions (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    session_token   VARCHAR(500)    NOT NULL UNIQUE,
    ip_address      VARCHAR(50),
    user_agent      VARCHAR(500),
    last_active_at  DATETIME(6),
    expires_at      DATETIME(6),
    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL,
    CONSTRAINT fk_us_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);



CREATE TABLE login_attempts (
    id BIGINT NOT NULL AUTO_INCREMENT,

    user_id BIGINT NULL,

    username VARCHAR(100),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    login_time DATETIME,
    status VARCHAR(50),

    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255) NOT NULL,

    active BOOLEAN NOT NULL,
    deleted BOOLEAN NOT NULL,

    tenant_id BIGINT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_login_attempt_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS password_history (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    password_hash   VARCHAR(255)    NOT NULL,
    changed_at      DATETIME(6),
    active          TINYINT(1)      NOT NULL DEFAULT 1,
    deleted         TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id       BIGINT,
    created_at      DATETIME(6),
    updated_at      DATETIME(6)     NOT NULL,
    created_by      VARCHAR(100)    NOT NULL,
    updated_by      VARCHAR(100)    NOT NULL,
    INDEX idx_ph_user (user_id),
    CONSTRAINT fk_ph_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    action      VARCHAR(100)    NOT NULL,
    description VARCHAR(1000),
    ip_address  VARCHAR(50),
    user_agent  VARCHAR(500),
    success     TINYINT(1),
    action_time DATETIME(6),
    active      TINYINT(1)      NOT NULL DEFAULT 1,
    deleted     TINYINT(1)      NOT NULL DEFAULT 0,
    tenant_id   BIGINT,
    created_at  DATETIME(6),
    updated_at  DATETIME(6)     NOT NULL,
    created_by  VARCHAR(100)    NOT NULL,
    updated_by  VARCHAR(100)    NOT NULL,
    INDEX idx_al_user   (user_id),
    INDEX idx_al_action (action),
    INDEX idx_al_tenant (tenant_id),
    CONSTRAINT fk_al_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

-- ===================================================
-- Seed: default tenant and default roles
-- ===================================================

INSERT IGNORE INTO tenants (tenant_code, name, description, enabled, active, deleted, created_at, updated_at, created_by, updated_by)
VALUES ('DEFAULT', 'Default Tenant', 'Auto-seeded default tenant', 1, 1, 0, NOW(), NOW(), 'system', 'system');

INSERT IGNORE INTO roles (name, description, active, deleted, created_at, updated_at, created_by, updated_by)
VALUES ('ROLE_USER',  'Standard user role',          1, 0, NOW(), NOW(), 'system', 'system'),
       ('ROLE_ADMIN', 'Administrator role',           1, 0, NOW(), NOW(), 'system', 'system'),
       ('ROLE_SUPER_ADMIN', 'Super-administrator role', 1, 0, NOW(), NOW(), 'system', 'system');
