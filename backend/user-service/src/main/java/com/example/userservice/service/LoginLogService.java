package com.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.userservice.entity.LoginLog;

public interface LoginLogService extends IService<LoginLog> {
    com.baomidou.mybatisplus.core.metadata.IPage<LoginLog> getLoginLogsWithUser(
            com.baomidou.mybatisplus.core.metadata.IPage<LoginLog> page);
}
