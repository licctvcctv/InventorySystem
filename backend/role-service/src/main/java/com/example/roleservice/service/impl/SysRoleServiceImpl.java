package com.example.roleservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.roleservice.entity.SysPermission;
import com.example.roleservice.entity.SysRole;
import com.example.roleservice.mapper.SysRoleMapper;
import com.example.roleservice.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SysPermission> getPermissions(Long roleId) {
        return baseMapper.selectPermissionsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除旧权限
        jdbcTemplate.update("DELETE FROM sys_role_permissions WHERE role_id = ?", roleId);

        // 插入新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permId : permissionIds) {
                jdbcTemplate.update("INSERT INTO sys_role_permissions (role_id, permission_id) VALUES (?, ?)", roleId,
                        permId);
            }
        }
    }
}
