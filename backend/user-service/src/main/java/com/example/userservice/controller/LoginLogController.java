package com.example.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.userservice.entity.LoginLog;
import com.example.userservice.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login-logs")
@CrossOrigin
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @GetMapping
    public ResponseEntity<IPage<LoginLog>> getLoginLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<LoginLog> pageConfig = new Page<>(page, size);
        return ResponseEntity.ok(loginLogService.getLoginLogsWithUser(pageConfig));
    }
}
