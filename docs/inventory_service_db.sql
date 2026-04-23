/*
 Navicat Premium Dump SQL

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : inventory_service_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 05/03/2026 16:02:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `inventory_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `inventory_service_db`;

-- Seata AT transaction undo log table
CREATE TABLE IF NOT EXISTS `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'Branch transaction id',
  `xid` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Global transaction id',
  `context` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Undo log context',
  `rollback_info` longblob NOT NULL COMMENT 'Rollback info',
  `log_status` int NOT NULL COMMENT '0: normal, 1: defense',
  `log_created` datetime(6) NOT NULL COMMENT 'Create time',
  `log_modified` datetime(6) NOT NULL COMMENT 'Modify time',
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Seata AT undo log';

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- ----------------------------
-- Records of categories
-- ----------------------------
BEGIN;
INSERT INTO `categories` (`id`, `name`, `description`, `created_at`) VALUES (1, '电子数码', '手机、电脑及配件', '2026-01-01 19:18:39');
INSERT INTO `categories` (`id`, `name`, `description`, `created_at`) VALUES (2, '办公家具', '桌椅、柜子', '2026-01-01 19:18:39');
INSERT INTO `categories` (`id`, `name`, `description`, `created_at`) VALUES (3, '电脑配件', 'CPU、内存、硬盘等', '2026-03-05 09:43:00');
INSERT INTO `categories` (`id`, `name`, `description`, `created_at`) VALUES (4, '网络设备', '路由器、交换机等', '2026-03-05 09:43:00');
INSERT INTO `categories` (`id`, `name`, `description`, `created_at`) VALUES (5, '办公耗材', '打印纸、墨盒等', '2026-03-05 09:43:00');
COMMIT;

-- ----------------------------
-- Table structure for inventory_ledger
-- ----------------------------
DROP TABLE IF EXISTS `inventory_ledger`;
CREATE TABLE `inventory_ledger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品编码',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品类型',
  `product_attr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '属性（配置）',
  `unit` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '库存单位',
  `final_stock` int NOT NULL DEFAULT '0' COMMENT '最终库存量',
  `sale_price` decimal(12,2) DEFAULT '0.00' COMMENT '销售单价',
  `sale_amount` decimal(12,2) DEFAULT '0.00' COMMENT '销售金额',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存台账表';

-- ----------------------------
-- Records of inventory_ledger
-- ----------------------------
BEGIN;
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (3, 1, 'SP001', '高性能笔记本', '电子数码', '16G内存/512G固态', '台', 95, 5999.00, 569905.00, 1, 1, '2026-02-13 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (4, 2, 'SP002', '无线鼠标', '电子数码', '静音无线2.4G', '个', 450, 99.00, 44550.00, 1, 1, '2026-02-13 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (5, 3, 'SP003', '人体工学椅', '办公家具', '网布透气护腰', '把', 30, 899.00, 26970.00, 3, 1, '2026-02-13 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (6, 4, 'SP004', '机械键盘', '电子数码', 'Cherry红轴RGB', '个', 180, 299.00, 53820.00, 1, 1, '2026-02-18 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (7, 5, 'SP005', '27寸显示器', '电子数码', '4K IPS面板', '台', 72, 1599.00, 115128.00, 2, 2, '2026-02-18 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (8, 6, 'SP006', 'USB集线器', '电子数码', 'Type-C 7合1', '个', 300, 59.00, 17700.00, 1, 1, '2026-02-21 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (9, 7, 'SP007', '企业路由器', '网络设备', '千兆企业级', '台', 60, 499.00, 29940.00, 3, 3, '2026-02-23 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (10, 8, 'SP008', '网络交换机', '网络设备', '24口千兆', '台', 40, 899.00, 35960.00, 3, 3, '2026-02-23 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (11, 9, 'SP009', 'A4打印纸', '办公耗材', '70g 500张/包', '包', 1000, 25.00, 25000.00, 3, 1, '2026-02-25 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (12, 10, 'SP010', '激光打印机', '办公耗材', '黑白激光', '台', 30, 1299.00, 38970.00, 2, 2, '2026-02-25 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (13, 11, 'SP011', 'Intel Core i9', '电脑配件', '第14代酷睿', '颗', 150, 2500.00, 375000.00, 1, 1, '2026-02-28 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (14, 12, 'SP012', 'DDR5内存条', '电脑配件', '16GB 4800MHz', '根', 250, 399.00, 99750.00, 4, 1, '2026-02-28 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `inventory_ledger` (`id`, `product_id`, `product_code`, `product_name`, `product_type`, `product_attr`, `unit`, `final_stock`, `sale_price`, `sale_amount`, `supplier_id`, `warehouse_id`, `created_at`, `updated_at`) VALUES (15, 13, 'SP013', '固态硬盘1TB', '电脑配件', 'NVMe M.2', '块', 180, 499.00, 89820.00, 4, 2, '2026-03-02 09:39:54', '2026-03-05 09:39:54');
COMMIT;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品编码',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `brand` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品品牌',
  `product_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品类型',
  `product_attr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品属性',
  `unit` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '计量单位',
  `purchase_price` decimal(12,2) DEFAULT '0.00' COMMENT '采购价',
  `sale_price` decimal(12,2) DEFAULT '0.00' COMMENT '售价',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `warehouse_id` bigint DEFAULT NULL COMMENT '仓库ID',
  `sku` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'SKU编码',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '参考单价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '当前库存数量',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品库存表';

-- ----------------------------
-- Records of products
-- ----------------------------
BEGIN;
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (1, 'SP001', '高性能笔记本', '联想', '电子数码', '16G内存/512G固态', '台', 4500.00, 5999.00, 1, 1, 'SKU-LAPTOP-001', 5999.00, 95, '16G内存/512G固态', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 1);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (2, 'SP002', '无线鼠标', '罗技', '电子数码', '静音无线2.4G', '个', 50.00, 99.00, 1, 1, 'SKU-MOUSE-001', 99.00, 450, '静音无线鼠标', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 1);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (3, 'SP003', '人体工学椅', '西昊', '办公家具', '网布透气护腰', '把', 500.00, 899.00, 3, 1, 'SKU-CHAIR-001', 899.00, 30, '网布透气护腰', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 2);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (4, 'SP004', '机械键盘', 'Cherry', '电子数码', 'Cherry红轴RGB', '个', 180.00, 299.00, 1, 1, 'SKU-KB-001', 299.00, 180, 'Cherry轴机械键盘', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 1);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (5, 'SP005', '27寸显示器', '戴尔', '电子数码', '4K IPS面板', '台', 1200.00, 1599.00, 2, 2, 'SKU-MON-001', 1599.00, 72, '4K IPS显示器', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 1);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (6, 'SP006', 'USB集线器', '绿联', '电子数码', 'Type-C 7合1', '个', 30.00, 59.00, 1, 1, 'SKU-HUB-001', 59.00, 300, 'Type-C 7合1扩展坞', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 1);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (7, 'SP007', '企业路由器', '华为', '网络设备', '千兆企业级', '台', 300.00, 499.00, 3, 3, 'SKU-RT-001', 499.00, 60, '千兆企业级路由器', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 4);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (8, 'SP008', '网络交换机', '华三', '网络设备', '24口千兆', '台', 600.00, 899.00, 3, 3, 'SKU-SW-001', 899.00, 40, '24口千兆交换机', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 4);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (9, 'SP009', 'A4打印纸', '得力', '办公耗材', '70g 500张/包', '包', 15.00, 25.00, 3, 1, 'SKU-PP-001', 25.00, 1000, '70g A4复印纸500张', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 5);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (10, 'SP010', '激光打印机', '惠普', '办公耗材', '黑白激光', '台', 800.00, 1299.00, 2, 2, 'SKU-PRT-001', 1299.00, 30, '黑白激光打印机', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 5);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (11, 'SP011', 'Intel Core i9', 'Intel', '电脑配件', '第14代酷睿', '颗', 2000.00, 2500.00, 1, 1, 'SKU-CPU-001', 2500.00, 150, '第14代酷睿i9', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 3);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (12, 'SP012', 'DDR5内存条', '三星', '电脑配件', '16GB 4800MHz', '根', 280.00, 399.00, 4, 1, 'SKU-RAM-001', 399.00, 250, '16GB DDR5 4800MHz', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 3);
INSERT INTO `products` (`id`, `product_code`, `name`, `brand`, `product_type`, `product_attr`, `unit`, `purchase_price`, `sale_price`, `supplier_id`, `warehouse_id`, `sku`, `price`, `stock`, `description`, `created_at`, `updated_at`, `category_id`) VALUES (13, 'SP013', '固态硬盘1TB', '三星', '电脑配件', 'NVMe M.2', '块', 350.00, 499.00, 4, 2, 'SKU-SSD-001', 499.00, 180, 'NVMe M.2 1TB', '2026-03-05 09:43:00', '2026-03-05 09:43:00', 3);
COMMIT;

-- ----------------------------
-- Table structure for stock_records
-- ----------------------------
DROP TABLE IF EXISTS `stock_records`;
CREATE TABLE `stock_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_no` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单据编号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `product_attr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '属性（配置）',
  `unit` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位',
  `type` int NOT NULL COMMENT '类型：1-入库, 2-出库',
  `quantity` int NOT NULL COMMENT '变动数量',
  `unit_price` decimal(12,2) DEFAULT '0.00' COMMENT '单价',
  `total_amount` decimal(12,2) DEFAULT '0.00' COMMENT '金额',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `stock_quantity` int DEFAULT '0' COMMENT '当前库存量',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注(如关联订单号)',
  `created_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `warehouse_id` bigint DEFAULT NULL COMMENT 'Warehouse ID',
  `related_order_id` bigint DEFAULT NULL COMMENT 'Related Order ID',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `stock_records_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存变动记录表';

-- ----------------------------
-- Records of stock_records
-- ----------------------------
BEGIN;
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (9, 'RK20260201001', 1, '高性能笔记本', '16G内存/512G固态', '台', 1, 100, 4500.00, 450000.00, 1, 100, '笔记本首批入库', NULL, '2026-02-03 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (10, 'RK20260203001', 2, '无线鼠标', '静音无线2.4G', '个', 1, 500, 50.00, 25000.00, 1, 500, '鼠标大批入库', NULL, '2026-02-05 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (11, 'RK20260206001', 3, '人体工学椅', '网布透气护腰', '把', 1, 50, 500.00, 25000.00, 3, 50, '椅子入库', NULL, '2026-02-08 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (12, 'RK20260209001', 4, '机械键盘', 'Cherry红轴RGB', '个', 1, 200, 180.00, 36000.00, 1, 200, '键盘入库', NULL, '2026-02-11 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (13, 'RK20260211001', 5, '27寸显示器', '4K IPS面板', '台', 1, 80, 1200.00, 96000.00, 2, 80, '显示器入库', NULL, '2026-02-13 09:43:00', 2, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (14, 'RK20260213001', 6, 'USB集线器', 'Type-C 7合1', '个', 1, 300, 30.00, 9000.00, 1, 300, 'USB集线器入库', NULL, '2026-02-15 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (15, 'RK20260216001', 7, '企业路由器', '千兆企业级', '台', 1, 60, 300.00, 18000.00, 3, 60, '路由器入库', NULL, '2026-02-18 09:43:00', 3, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (16, 'RK20260219001', 8, '网络交换机', '24口千兆', '台', 1, 40, 600.00, 24000.00, 3, 40, '交换机入库', NULL, '2026-02-21 09:43:00', 3, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (17, 'RK20260221001', 9, 'A4打印纸', '70g 500张/包', '包', 1, 1000, 15.00, 15000.00, 3, 1000, '打印纸大批入库', NULL, '2026-02-23 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (18, 'RK20260223001', 10, '激光打印机', '黑白激光', '台', 1, 30, 800.00, 24000.00, 2, 30, '打印机入库', NULL, '2026-02-25 09:43:00', 2, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (19, 'RK20260225001', 11, 'Intel Core i9', '第14代酷睿', '颗', 1, 150, 2000.00, 300000.00, 1, 150, 'CPU入库', NULL, '2026-02-27 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (20, 'RK20260227001', 12, 'DDR5内存条', '16GB 4800MHz', '根', 1, 250, 280.00, 70000.00, 4, 250, '内存条入库', NULL, '2026-03-01 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (21, 'RK20260301001', 13, '固态硬盘1TB', 'NVMe M.2', '块', 1, 180, 350.00, 63000.00, 4, 180, '固态硬盘入库', NULL, '2026-03-03 09:43:00', 2, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (22, 'CK20260213001', 1, '高性能笔记本', '16G内存/512G固态', '台', 2, 5, 5999.00, 29995.00, NULL, 95, '销售出库-笔记本', NULL, '2026-02-15 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (23, 'CK20260219001', 2, '无线鼠标', '静音无线2.4G', '个', 2, 50, 99.00, 4950.00, NULL, 450, '销售出库-鼠标', NULL, '2026-02-21 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (24, 'CK20260223001', 3, '人体工学椅', '网布透气护腰', '把', 2, 20, 899.00, 17980.00, NULL, 30, '销售出库-椅子', NULL, '2026-02-25 09:43:00', 1, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (25, 'CK20260228001', 5, '27寸显示器', '4K IPS面板', '台', 2, 8, 1599.00, 12792.00, NULL, 72, '销售出库-显示器', NULL, '2026-03-02 09:43:00', 2, NULL);
INSERT INTO `stock_records` (`id`, `record_no`, `product_id`, `product_name`, `product_attr`, `unit`, `type`, `quantity`, `unit_price`, `total_amount`, `supplier_id`, `stock_quantity`, `remark`, `created_by`, `created_at`, `warehouse_id`, `related_order_id`) VALUES (26, 'CK20260302001', 4, '机械键盘', 'Cherry红轴RGB', '个', 2, 20, 299.00, 5980.00, NULL, 180, '销售出库-键盘', NULL, '2026-03-04 09:43:00', 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for warehouses
-- ----------------------------
DROP TABLE IF EXISTS `warehouses`;
CREATE TABLE `warehouses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '仓库名称',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `manager` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '负责人',
  `contact` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';

-- ----------------------------
-- Records of warehouses
-- ----------------------------
BEGIN;
INSERT INTO `warehouses` (`id`, `name`, `address`, `manager`, `contact`, `created_at`) VALUES (1, '北京总仓', '北京市朝阳区物流园A区', '张三', '13700000001', '2026-01-01 19:18:39');
INSERT INTO `warehouses` (`id`, `name`, `address`, `manager`, `contact`, `created_at`) VALUES (2, '上海分仓', '上海市青浦区工业园', '李四', '13700000002', '2026-01-01 19:18:39');
INSERT INTO `warehouses` (`id`, `name`, `address`, `manager`, `contact`, `created_at`) VALUES (3, '广州分仓', '广州市黄埔区科学城', '王五', '13700000003', '2026-03-05 09:43:00');
INSERT INTO `warehouses` (`id`, `name`, `address`, `manager`, `contact`, `created_at`) VALUES (4, '成都分仓', '成都市高新区天府软件园', '赵六', '13700000004', '2026-03-05 09:43:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
