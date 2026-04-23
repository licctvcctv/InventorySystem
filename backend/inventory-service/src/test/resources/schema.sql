CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(100),
    name VARCHAR(200),
    brand VARCHAR(100),
    product_type VARCHAR(100),
    product_attr VARCHAR(200),
    unit VARCHAR(50),
    purchase_price DECIMAL(12,2),
    sale_price DECIMAL(12,2),
    supplier_id BIGINT,
    warehouse_id BIGINT,
    sku VARCHAR(100),
    price DECIMAL(12,2),
    stock INT DEFAULT 0,
    description VARCHAR(500),
    category_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stock_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_no VARCHAR(100),
    product_id BIGINT,
    product_name VARCHAR(200),
    product_attr VARCHAR(200),
    unit VARCHAR(50),
    warehouse_id BIGINT,
    related_order_id BIGINT,
    type INT,
    quantity INT,
    unit_price DECIMAL(12,2),
    total_amount DECIMAL(12,2),
    supplier_id BIGINT,
    stock_quantity INT,
    remark VARCHAR(500),
    created_by VARCHAR(100),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    address VARCHAR(500),
    manager VARCHAR(100),
    contact VARCHAR(100),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
    description VARCHAR(500),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventory_ledger (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    product_code VARCHAR(100),
    product_name VARCHAR(200),
    product_type VARCHAR(100),
    product_attr VARCHAR(200),
    unit VARCHAR(50),
    final_stock INT,
    sale_price DECIMAL(12,2),
    sale_amount DECIMAL(12,2),
    supplier_id BIGINT,
    warehouse_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
