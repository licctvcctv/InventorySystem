CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    contact_person VARCHAR(100),
    phone VARCHAR(50),
    email VARCHAR(200),
    address VARCHAR(500),
    payment_info VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    contact_person VARCHAR(100),
    phone VARCHAR(50),
    email VARCHAR(200),
    address VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS finance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    related_order_id BIGINT,
    type INT,
    amount DECIMAL(12,2),
    category VARCHAR(100),
    target_name VARCHAR(200),
    remark VARCHAR(500),
    created_at TIMESTAMP,
    source_order_no VARCHAR(100),
    income_source VARCHAR(200),
    expense_target VARCHAR(200)
);
