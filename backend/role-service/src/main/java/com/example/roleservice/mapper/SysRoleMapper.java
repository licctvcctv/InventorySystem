package com.example.roleservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.roleservice.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT p.* FROM sys_permissions p " +
            "INNER JOIN sys_role_permissions rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<com.example.roleservice.entity.SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}
