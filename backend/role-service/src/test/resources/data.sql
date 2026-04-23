MERGE INTO sys_roles (id, name, code, description, status, created_at, updated_at)
KEY(id)
VALUES (1, '管理员', 'ROLE_ADMIN', '系统管理员', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO sys_permissions (id, name, code, type, parent_id, created_at, updated_at)
KEY(id)
VALUES (1, '用户管理', 'user:manage', 'menu', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
MERGE INTO sys_permissions (id, name, code, type, parent_id, created_at, updated_at)
KEY(id)
VALUES (2, '角色管理', 'role:manage', 'menu', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
MERGE INTO sys_permissions (id, name, code, type, parent_id, created_at, updated_at)
KEY(id)
VALUES (3, '权限管理', 'permission:manage', 'menu', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
