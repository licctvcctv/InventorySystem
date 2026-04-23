CREATE TABLE IF NOT EXISTS sys_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    code VARCHAR(100),
    description VARCHAR(500),
    status INT DEFAULT 1,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    code VARCHAR(100),
    type VARCHAR(50),
    parent_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_role_permissions (
    role_id BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id)
);
