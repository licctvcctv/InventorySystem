/*
 Navicat Premium Dump SQL

 Source Server         : localhost-mysql
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : localhost:3306
 Source Schema         : role_service_db

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 05/03/2026 16:02:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `role_service_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `role_service_db`;

-- ----------------------------
-- Table structure for sys_permissions
-- ----------------------------
DROP TABLE IF EXISTS `sys_permissions`;
CREATE TABLE `sys_permissions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限名称',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限标识',
  `type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型 MENU/BUTTON',
  `parent_id` bigint DEFAULT '0' COMMENT '父ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ----------------------------
-- Records of sys_permissions
-- ----------------------------
BEGIN;
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (1, '系统管理', 'system:manage', 'MENU', 0, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (2, '用户管理', 'user:manage', 'MENU', 1, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (3, '用户列表', 'user:list', 'BUTTON', 2, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (4, '用户新增', 'user:add', 'BUTTON', 2, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (5, '用户修改', 'user:edit', 'BUTTON', 2, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_permissions` (`id`, `name`, `code`, `type`, `parent_id`, `created_at`, `updated_at`) VALUES (6, '登录日志', 'log:login', 'MENU', 1, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permissions`;
CREATE TABLE `sys_role_permissions` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `sys_role_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_roles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sys_role_permissions_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `sys_permissions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ----------------------------
-- Records of sys_role_permissions
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 1);
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 2);
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 3);
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 4);
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 5);
INSERT INTO `sys_role_permissions` (`role_id`, `permission_id`) VALUES (1, 6);
COMMIT;

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `status` int DEFAULT '1' COMMENT '状态 1:启用 0:禁用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- Records of sys_roles
-- ----------------------------
BEGIN;
INSERT INTO `sys_roles` (`id`, `name`, `code`, `description`, `status`, `created_at`, `updated_at`) VALUES (1, '管理员', 'ROLE_ADMIN', '系统管理员，拥有所有权限', 1, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
INSERT INTO `sys_roles` (`id`, `name`, `code`, `description`, `status`, `created_at`, `updated_at`) VALUES (2, '普通用户', 'ROLE_USER', '普通操作员', 1, '2026-01-01 15:00:27', '2026-01-01 15:00:27');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
