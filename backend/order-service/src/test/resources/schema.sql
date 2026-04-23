CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(100),
    customer_id BIGINT,
    supplier_id BIGINT,
    warehouse_id BIGINT,
    type VARCHAR(50),
    total_amount DECIMAL(12,2),
    status INT DEFAULT 0,
    description VARCHAR(500),
    created_by VARCHAR(100),
    invoice_type VARCHAR(100),
    salesman VARCHAR(100),
    deal_amount DECIMAL(12,2),
    total_cost DECIMAL(12,2),
    order_debt DECIMAL(12,2),
    discount DECIMAL(12,2),
    payment_status INT DEFAULT 0,
    customer_address VARCHAR(500),
    customer_payment_info VARCHAR(500),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    product_name VARCHAR(200),
    product_attr VARCHAR(200),
    unit VARCHAR(50),
    quantity INT,
    price DECIMAL(12,2),
    cost_price DECIMAL(12,2),
    amount DECIMAL(12,2),
    cost_amount DECIMAL(12,2),
    stock_quantity INT
);

CREATE TABLE IF NOT EXISTS payment_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_date DATE,
    payment_no VARCHAR(100),
    source_order_no VARCHAR(100),
    source_order_id BIGINT,
    settlement_method VARCHAR(100),
    amount_due DECIMAL(12,2),
    amount_paid DECIMAL(12,2),
    payment_person VARCHAR(100),
    payee VARCHAR(200),
    payer VARCHAR(200),
    is_fully_paid BOOLEAN,
    remark VARCHAR(500),
    type VARCHAR(50),
    created_at TIMESTAMP
);
