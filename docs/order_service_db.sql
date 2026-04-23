/*
 Navicat Premium Dump SQL

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : order_service_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 05/03/2026 16:02:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `order_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `order_service_db`;

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
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '关联订单ID',
  `product_id` bigint DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `product_attr` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '属性（配置）',
  `unit` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '单价',
  `cost_price` decimal(12,2) DEFAULT '0.00' COMMENT '成本单价',
  `cost_amount` decimal(12,2) DEFAULT '0.00' COMMENT '成本金额',
  `stock_quantity` int DEFAULT '0' COMMENT '库存量',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- ----------------------------
-- Records of order_items
-- ----------------------------
BEGIN;
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (16, 1, 1, '高性能笔记本', '16G内存/512G固态', '台', 10, 5999.00, 5999.00, 59990.00, 100, 59990.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (17, 2, 5, '27寸显示器', '4K IPS面板', '台', 8, 1500.00, 1500.00, 12000.00, 80, 12000.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (18, 3, 11, 'Intel Core i9', '第14代酷睿', '颗', 20, 2500.00, 2500.00, 50000.00, 150, 50000.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (19, 4, 4, '机械键盘', 'Cherry红轴', '个', 20, 299.00, 299.00, 5980.00, 200, 5980.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (20, 5, 12, 'DDR5内存条', '16GB 4800MHz', '根', 50, 399.00, 399.00, 19950.00, 250, 19950.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (21, 6, 1, '高性能笔记本', '16G内存/512G固态', '台', 5, 5999.00, 5999.00, 29995.00, 95, 29995.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (22, 7, 2, '无线鼠标', '静音无线2.4G', '个', 50, 99.00, 99.00, 4950.00, 450, 4950.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (23, 8, 3, '人体工学椅', '网布透气护腰', '把', 20, 899.10, 899.00, 17980.00, 30, 17982.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (24, 9, 5, '27寸显示器', '4K IPS面板', '台', 8, 1599.00, 1500.00, 12000.00, 72, 12792.00);
INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `product_attr`, `unit`, `quantity`, `price`, `cost_price`, `cost_amount`, `stock_quantity`, `amount`) VALUES (25, 10, 4, '机械键盘', 'Cherry红轴', '个', 10, 299.00, 299.00, 2990.00, 180, 2990.00);
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单类型：PURCHASE-采购, SALE-销售',
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `status` int DEFAULT '0' COMMENT '状态：0-待处理, 1-已完成, 2-已取消',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `customer_id` bigint DEFAULT NULL COMMENT 'Customer ID',
  `supplier_id` bigint DEFAULT NULL COMMENT 'Supplier ID',
  `warehouse_id` bigint DEFAULT NULL COMMENT 'Warehouse ID',
  `invoice_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '票据类型',
  `salesman` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务员',
  `deal_amount` decimal(12,2) DEFAULT '0.00' COMMENT '成交金额',
  `total_cost` decimal(12,2) DEFAULT '0.00' COMMENT '整单成本',
  `order_debt` decimal(12,2) DEFAULT '0.00' COMMENT '本单欠款',
  `payment_status` int DEFAULT '0' COMMENT '收/付款状态：0-未付, 1-部分, 2-已付',
  `customer_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户地址',
  `customer_payment_info` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '客户付款信息',
  `discount` decimal(5,2) DEFAULT '0.00' COMMENT '折扣',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (1, 'PO20260201001', 'PURCHASE', 59990.00, 1, '采购高性能笔记本10台', 'admin', '2026-02-13 09:39:54', '2026-03-05 09:39:54', NULL, 1, 1, '增值税专用发票', '张伟', 59990.00, 59990.00, 0.00, 2, NULL, NULL, 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (2, 'PO20260205001', 'PURCHASE', 12000.00, 1, '采购27寸显示器8台', 'admin', '2026-02-18 09:39:54', '2026-03-05 09:39:54', NULL, 2, 2, '增值税普通发票', '李强', 12000.00, 12000.00, 2000.00, 1, NULL, NULL, 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (3, 'PO20260210001', 'PURCHASE', 50000.00, 0, '采购Intel Core i9处理器20颗', 'admin', '2026-02-23 09:39:54', '2026-03-05 09:39:54', NULL, 1, 1, '增值税专用发票', '张伟', 50000.00, 50000.00, 20000.00, 1, NULL, NULL, 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (4, 'PO20260215001', 'PURCHASE', 5980.00, 1, '采购机械键盘20个', 'admin', '2026-02-28 09:39:54', '2026-03-05 09:39:54', NULL, 3, 1, '增值税普通发票', '王芳', 5980.00, 5980.00, 0.00, 2, NULL, NULL, 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (5, 'PO20260220001', 'PURCHASE', 19950.00, 0, '采购DDR5内存条50根', 'admin', '2026-03-03 09:39:54', '2026-03-05 09:39:54', NULL, 4, 1, '增值税专用发票', '李强', 19950.00, 19950.00, 19950.00, 0, NULL, NULL, 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (6, 'SO20260203001', 'SALE', 29995.00, 1, '阿里巴巴采购5台笔记本', 'admin', '2026-02-15 09:39:54', '2026-03-05 09:39:54', 1, NULL, 1, '增值税专用发票', '赵敏', 28495.25, 29995.00, 0.00, 2, '杭州市余杭区', '银行转账 工商银行', 5.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (7, 'SO20260208001', 'SALE', 4950.00, 1, '企鹅网络采购50个鼠标', 'admin', '2026-02-21 09:39:54', '2026-03-05 09:39:54', 2, NULL, 1, '增值税普通发票', '赵敏', 4950.00, 4950.00, 0.00, 2, '深圳市南山区', '支付宝', 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (8, 'SO20260212001', 'SALE', 17982.00, 1, '字节跳动采购20把椅子', 'admin', '2026-02-25 09:39:54', '2026-03-05 09:39:54', 3, NULL, 1, '增值税专用发票', '孙丽', 17082.90, 17982.00, 2000.00, 1, '北京市海淀区', '银行转账 建设银行', 5.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (9, 'SO20260218001', 'SALE', 12792.00, 0, '华为采购8台显示器', 'admin', '2026-03-02 09:39:54', '2026-03-05 09:39:54', 4, NULL, 2, '增值税专用发票', '赵敏', 12792.00, 12792.00, 12792.00, 0, '深圳市龙岗区', '银行转账', 0.00);
INSERT INTO `orders` (`id`, `order_no`, `type`, `total_amount`, `status`, `description`, `created_by`, `created_at`, `updated_at`, `customer_id`, `supplier_id`, `warehouse_id`, `invoice_type`, `salesman`, `deal_amount`, `total_cost`, `order_debt`, `payment_status`, `customer_address`, `customer_payment_info`, `discount`) VALUES (10, 'SO20260220001', 'SALE', 2990.00, 0, '小米采购10个键盘', 'admin', '2026-03-04 09:39:54', '2026-03-05 09:39:54', 5, NULL, 1, '增值税普通发票', '孙丽', 2840.50, 2990.00, 2840.50, 0, '北京市海淀区清河', '微信支付', 5.00);
COMMIT;

-- ----------------------------
-- Table structure for payment_records
-- ----------------------------
DROP TABLE IF EXISTS `payment_records`;
CREATE TABLE `payment_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `payment_date` date DEFAULT NULL,
  `payment_no` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source_order_no` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_order_id` bigint DEFAULT NULL,
  `settlement_method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount_due` decimal(10,2) DEFAULT '0.00',
  `amount_paid` decimal(10,2) DEFAULT '0.00',
  `payment_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payee` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_fully_paid` tinyint(1) DEFAULT '0',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='付款收款记录表';

-- ----------------------------
-- Records of payment_records
-- ----------------------------
BEGIN;
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (1, '2026-02-14', 'PAY20260202001', 'PO20260201001', 1, '银行转账', 59990.00, 59990.00, '财务张三', '英特尔芯片厂', NULL, 1, '笔记本采购全款', 'PAYMENT', '2026-02-14 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (2, '2026-02-19', 'PAY20260206001', 'PO20260205001', 2, '银行转账', 12000.00, 10000.00, '财务李四', '京东方显示屏', NULL, 0, '显示器采购预付款', 'PAYMENT', '2026-02-19 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (3, '2026-02-24', 'PAY20260211001', 'PO20260210001', 3, '支票', 50000.00, 30000.00, '财务张三', '英特尔芯片厂', NULL, 0, 'CPU采购预付款', 'PAYMENT', '2026-02-24 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (4, '2026-03-01', 'PAY20260216001', 'PO20260215001', 4, '现金', 5980.00, 5980.00, '财务李四', '顺丰物流服务', NULL, 1, '键盘采购全款', 'PAYMENT', '2026-03-01 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (5, '2026-02-16', 'REC20260204001', 'SO20260203001', 6, '银行转账', 28495.25, 28495.25, '财务李四', NULL, '阿里巴巴科技', 1, '笔记本销售全款', 'RECEIPT', '2026-02-16 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (6, '2026-02-22', 'REC20260209001', 'SO20260208001', 7, '支付宝', 4950.00, 4950.00, '财务李四', NULL, '企鹅网络互动', 1, '鼠标销售全款', 'RECEIPT', '2026-02-22 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (7, '2026-02-26', 'REC20260213001', 'SO20260212001', 8, '银行转账', 17082.90, 15082.90, '财务张三', NULL, '字节跳动无限', 0, '椅子销售首款', 'RECEIPT', '2026-02-26 09:39:54');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (8, '2026-03-05', 'PAY2694225505', 'PO20260220001', 5, '银行转账', 19950.00, 19950.00, '1', '供应商ID:4', NULL, 1, '1', 'PAYMENT', '2026-03-05 15:03:48');
INSERT INTO `payment_records` (`id`, `payment_date`, `payment_no`, `source_order_no`, `source_order_id`, `settlement_method`, `amount_due`, `amount_paid`, `payment_person`, `payee`, `payer`, `is_fully_paid`, `remark`, `type`, `created_at`) VALUES (9, '2026-03-05', 'REC2694239338', 'SO20260220001', 10, '银行转账', 2840.50, 2840.50, '', NULL, '客户ID:5', 1, '', 'RECEIPT', '2026-03-05 15:04:00');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
