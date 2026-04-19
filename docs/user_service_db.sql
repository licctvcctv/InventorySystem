/*
 Navicat Premium Dump SQL

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : user_service_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 05/03/2026 16:03:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for login_logs
-- ----------------------------
DROP TABLE IF EXISTS `login_logs`;
CREATE TABLE `login_logs` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '登录状态：1-成功，0-失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录日志表';

-- ----------------------------
-- Records of login_logs
-- ----------------------------
BEGIN;
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (1, 1, NULL, '2026-01-01 15:01:25', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (2, 1, NULL, '2026-01-01 18:31:06', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (3, 1, NULL, '2026-01-01 18:44:02', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (4, 1, NULL, '2026-01-01 18:46:11', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (5, 1, NULL, '2026-01-01 18:56:27', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (6, 1, NULL, '2026-01-01 18:57:27', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (7, 5, NULL, '2026-01-01 18:59:28', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (8, 1, NULL, '2026-01-01 19:07:24', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (9, 6, NULL, '2026-01-01 19:07:38', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (10, 1, NULL, '2026-01-01 19:10:34', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (11, 4, NULL, '2026-01-01 19:10:47', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (12, 5, NULL, '2026-01-01 19:11:30', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (13, 5, NULL, '2026-01-01 19:13:46', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (14, 6, NULL, '2026-01-01 19:13:59', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (15, 7, NULL, '2026-01-01 19:14:22', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (16, 4, NULL, '2026-01-01 19:14:41', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (17, 1, NULL, '2026-01-01 19:14:52', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (18, 4, NULL, '2026-01-01 19:16:37', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (19, 5, NULL, '2026-01-01 19:16:47', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (20, 1, NULL, '2026-01-01 19:18:50', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (21, 1, NULL, '2026-03-02 21:36:01', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (22, 1, NULL, '2026-03-02 21:50:13', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (23, 1, NULL, '2026-03-02 21:50:37', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (24, 1, NULL, '2026-03-02 21:50:45', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (25, 1, NULL, '2026-03-05 09:22:32', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (26, 1, NULL, '2026-03-05 09:22:40', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (27, 1, NULL, '2026-03-05 09:24:38', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (28, 1, NULL, '2026-03-05 09:28:15', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (29, 1, NULL, '2026-03-05 09:28:38', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (30, 1, NULL, '2026-03-05 09:29:09', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (31, 1, NULL, '2026-03-05 09:29:38', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (32, 1, NULL, '2026-03-05 09:30:46', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (33, 1, NULL, '2026-03-05 09:32:28', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (34, 1, NULL, '2026-03-05 09:32:44', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (35, 1, NULL, '2026-03-05 09:33:07', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (36, 1, NULL, '2026-03-05 09:33:43', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (37, 1, NULL, '2026-03-05 09:34:00', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (38, 1, NULL, '2026-03-05 09:34:40', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (39, 1, NULL, '2026-03-05 09:43:26', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (40, 1, NULL, '2026-03-05 09:43:49', 1);
INSERT INTO `login_logs` (`id`, `user_id`, `ip_address`, `login_time`, `status`) VALUES (41, 1, NULL, '2026-03-05 09:44:35', 1);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码（加密存储）',
  `full_name` varchar(50) DEFAULT '' COMMENT '用户姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `role` varchar(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '角色：ROLE_USER, ROLE_ADMIN',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (1, 'admin', '$2a$10$LKZYbe9XpCS6XNsJXSbJjuosUaw1umQPcfbv/L8l0dCaGNkdDALmW', '系统管理员', 'admin@example.com', '13800000000', 'ROLE_ADMIN', 1, '2026-01-01 15:00:51', '2026-03-05 09:39:54');
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (2, 'zhangsan', '$2a$10$6LQuHc6/ec0M2sAfz3BdAuveRil1qz3Ec5Jxl.GyGpl0zV07xdkae', '张三', '', '', 'ROLE_USER', 1, '2026-01-01 15:01:40', '2026-01-01 15:01:40');
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (4, 'sales', '$2a$10$xx8EyaemhzzZoKLu7eGWE.alV/NeUTkuutuAQI8PoNNuIDwtGJYTm', '销售专员', 'sales@example.com', NULL, 'ROLE_SALES', 1, '2026-01-01 18:55:56', '2026-01-01 18:58:58');
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (5, 'procurement', '$2a$10$xx8EyaemhzzZoKLu7eGWE.alV/NeUTkuutuAQI8PoNNuIDwtGJYTm', '采购专员', 'procurement@example.com', NULL, 'ROLE_PROCUREMENT', 1, '2026-01-01 18:55:56', '2026-01-01 18:58:58');
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (6, 'warehouse', '$2a$10$xx8EyaemhzzZoKLu7eGWE.alV/NeUTkuutuAQI8PoNNuIDwtGJYTm', '仓库管理员', 'warehouse@example.com', NULL, 'ROLE_WAREHOUSE', 1, '2026-01-01 18:55:56', '2026-01-01 18:58:58');
INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `role`, `status`, `created_at`, `updated_at`) VALUES (7, 'finance', '$2a$10$xx8EyaemhzzZoKLu7eGWE.alV/NeUTkuutuAQI8PoNNuIDwtGJYTm', '财务专员', 'finance@example.com', NULL, 'ROLE_FINANCE', 1, '2026-01-01 18:55:56', '2026-01-01 18:58:58');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
