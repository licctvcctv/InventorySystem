package com.example.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.userservice.dto.AuthResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.User;

public interface UserService extends IService<User> {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void updateUserStatus(Long id, Integer status);

    void createUser(RegisterRequest request);

    void updateUser(Long id, RegisterRequest request);
}
