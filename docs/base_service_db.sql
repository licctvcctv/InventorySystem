/*
 Navicat Premium Dump SQL

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : base_service_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 05/03/2026 16:01:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customers
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户名称',
  `contact_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `payment_info` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '付款信息',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户信息表';

-- ----------------------------
-- Records of customers
-- ----------------------------
BEGIN;
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (1, '阿里巴巴科技', '马先生', '13800138000', 'ali@demo.com', '杭州市余杭区文一西路', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (2, '企鹅网络互动', '马化腾', '13800138001', 'pony@demo.com', '深圳市南山区科技园', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (3, '字节跳动无限', '张先生', '13800138002', 'byte@demo.com', '北京市海淀区知春路', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (4, '华为技术有限公司', '任先生', '13800138003', 'huawei@demo.com', '深圳市龙岗区华为基地', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (5, '小米科技有限公司', '雷先生', '13800138004', 'xiaomi@demo.com', '北京市海淀区清河', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (6, '京东集团', '刘先生', '13800138005', 'jd@demo.com', '北京市亦庄经济开发区', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (7, '美团科技', '王先生', '13800138006', 'meituan@demo.com', '北京市朝阳区望京', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (8, '网易公司', '丁先生', '13800138007', 'netease@demo.com', '杭州市滨江区网商路', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (9, '百度在线', '李先生', '13800138008', 'baidu@demo.com', '北京市海淀区上地十街', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `customers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `payment_info`, `created_at`, `updated_at`) VALUES (10, '拼多多科技', '黄先生', '13800138009', 'pdd@demo.com', '上海市长宁区娄山关路', NULL, '2026-03-05 09:39:54', '2026-03-05 09:39:54');
COMMIT;

-- ----------------------------
-- Table structure for finance_records
-- ----------------------------
DROP TABLE IF EXISTS `finance_records`;
CREATE TABLE `finance_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL COMMENT '类型：1-收入, 2-支出',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `category` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '科目类别',
  `target_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '往来单位(客户/供应商)',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `related_order_id` bigint DEFAULT NULL COMMENT 'Related Order ID',
  `source_order_no` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '来源单据单号',
  `income_source` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '金额收入来源',
  `expense_target` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '金额支出去向',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务流水表';

-- ----------------------------
-- Records of finance_records
-- ----------------------------
BEGIN;
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (5, 2, 59990.00, '采购付款', '英特尔芯片厂', '采购笔记本全款支付', '2026-02-14 09:39:54', 1, 'PO20260201001', NULL, '英特尔芯片厂-笔记本采购');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (6, 1, 28495.25, '销售回款', '阿里巴巴科技', '笔记本销售回款', '2026-02-16 09:39:54', 6, 'SO20260203001', '阿里巴巴科技-笔记本订单', NULL);
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (7, 2, 10000.00, '采购付款', '京东方显示屏', '显示器采购预付款', '2026-02-19 09:39:54', 2, 'PO20260205001', NULL, '京东方显示屏-显示器采购');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (8, 1, 4950.00, '销售回款', '企鹅网络互动', '鼠标销售全款', '2026-02-22 09:39:54', 7, 'SO20260208001', '企鹅网络互动-鼠标订单', NULL);
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (9, 2, 30000.00, '采购付款', '英特尔芯片厂', 'CPU采购预付款', '2026-02-24 09:39:54', 3, 'PO20260210001', NULL, '英特尔芯片厂-CPU采购');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (10, 1, 15082.90, '销售回款', '字节跳动无限', '椅子销售首款', '2026-02-26 09:39:54', 8, 'SO20260212001', '字节跳动无限-椅子订单', NULL);
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (11, 2, 5980.00, '采购付款', '顺丰物流服务', '键盘采购全款', '2026-03-01 09:39:54', 4, 'PO20260215001', NULL, '顺丰物流服务-键盘采购');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (12, 2, 2000.00, '日常报销', '公司运营', '办公室水电费', '2026-03-02 09:39:54', NULL, NULL, NULL, '公司运营-水电费');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (13, 2, 1500.00, '日常报销', '公司运营', '办公用品采购', '2026-03-03 09:39:54', NULL, NULL, NULL, '公司运营-办公用品');
INSERT INTO `finance_records` (`id`, `type`, `amount`, `category`, `target_name`, `remark`, `created_at`, `related_order_id`, `source_order_no`, `income_source`, `expense_target`) VALUES (14, 1, 5600.00, '其他收入', '公司运营', '设备租赁收入', '2026-03-04 09:39:54', NULL, NULL, '设备租赁-办公设备', NULL);
COMMIT;

-- ----------------------------
-- Table structure for suppliers
-- ----------------------------
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人',
  `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商信息表';

-- ----------------------------
-- Records of suppliers
-- ----------------------------
BEGIN;
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (1, '英特尔芯片厂', '基辛格', '13900139000', 'intel@demo.com', '上海市浦东新区张江高科', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (2, '京东方显示屏', '王厂长', '13900139001', 'boe@demo.com', '北京市亦庄经济开发区', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (3, '顺丰物流服务', '王卫', '13900139002', 'sf@demo.com', '深圳市宝安区航城街道', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (4, '三星电子', '李厂长', '13900139003', 'samsung@demo.com', '苏州市工业园区', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (5, '台积电半导体', '刘总', '13900139004', 'tsmc@demo.com', '南京市江北新区', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (6, '富士康科技', '郭总', '13900139005', 'foxconn@demo.com', '郑州市航空港区', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (7, '联想集团', '杨总', '13900139006', 'lenovo@demo.com', '北京市海淀区上地', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `created_at`, `updated_at`) VALUES (8, '比亚迪电子', '王总', '13900139007', 'byd@demo.com', '深圳市坪山区', '2026-03-05 09:39:54', '2026-03-05 09:39:54');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
