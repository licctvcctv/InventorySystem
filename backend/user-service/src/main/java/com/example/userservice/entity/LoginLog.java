package com.example.userservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("login_logs")
public class LoginLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String ipAddress;
    private LocalDateTime loginTime;
    private Integer status; // 1-Success, 0-Fail

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String username;

    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String fullName;
}
