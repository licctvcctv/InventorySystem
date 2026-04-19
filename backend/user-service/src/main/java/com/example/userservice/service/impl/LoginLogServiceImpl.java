package com.example.userservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.userservice.entity.LoginLog;
import com.example.userservice.mapper.LoginLogMapper;
import com.example.userservice.service.LoginLogService;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<LoginLog> getLoginLogsWithUser(
            com.baomidou.mybatisplus.core.metadata.IPage<LoginLog> page) {
        return baseMapper.selectPageWithUser(page);
    }
}
