MERGE INTO products (id, product_code, name, brand, product_type, product_attr, unit, purchase_price, sale_price, sku, stock, description, created_at, updated_at)
KEY(id)
VALUES (1, 'SP-INIT-001', '测试初始商品', '测试品牌', '电子产品', '默认规格', '台', 100.00, 200.00, 'SKU-INIT-001', 0, '测试用初始商品', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO warehouses (id, name, address, manager, contact, created_at)
KEY(id)
VALUES (1, '测试仓库', '测试地址', '测试管理员', '13800138000', CURRENT_TIMESTAMP);
