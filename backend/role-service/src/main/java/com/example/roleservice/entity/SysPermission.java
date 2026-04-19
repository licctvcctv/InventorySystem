package com.example.roleservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_permissions")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String type;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private List<SysPermission> children;
}
