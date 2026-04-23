CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE,
    full_name VARCHAR(100),
    password VARCHAR(200),
    email VARCHAR(200),
    phone VARCHAR(50),
    role VARCHAR(50),
    status INT DEFAULT 1,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS login_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    ip_address VARCHAR(50),
    login_time TIMESTAMP,
    status INT
);
