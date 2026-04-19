package com.example.roleservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.roleservice.entity.SysPermission;
import com.example.roleservice.entity.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    List<SysPermission> getPermissions(Long roleId);

    void assignPermissions(Long roleId, List<Long> permissionIds);
}
