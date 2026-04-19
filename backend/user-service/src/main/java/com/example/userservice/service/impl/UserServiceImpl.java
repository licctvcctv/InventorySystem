package com.example.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.userservice.dto.AuthResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import com.example.userservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final com.example.userservice.mapper.LoginLogMapper loginLogMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (count(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())) > 0) {
            throw new RuntimeException("Username already exists");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole() != null ? request.getRole() : "ROLE_USER"); // 默认 ROLE_USER
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1); // 默认启用

        save(user);
        var jwtToken = jwtUtils.generateToken(new UserDetailsImpl(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void createUser(RegisterRequest request) {
        if (count(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())) > 0) {
            throw new RuntimeException("Username already exists");
        }
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole() != null ? request.getRole() : "ROLE_USER");
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        save(user);
    }

    @Override
    public void updateUser(Long id, RegisterRequest request) {
        User user = getById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // 如果密码不为空，则更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        if (request.getRole() != null)
            user.setRole(request.getRole());
        if (request.getStatus() != null)
            user.setStatus(request.getStatus());

        updateById(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        var user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));

        // 记录登录日志
        com.example.userservice.entity.LoginLog log = com.example.userservice.entity.LoginLog.builder()
                .userId(user.getId())
                .loginTime(java.time.LocalDateTime.now())
                .status(1)
                .build();
        loginLogMapper.insert(log);

        var jwtToken = jwtUtils.generateToken(new UserDetailsImpl(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        User user = getById(id);
        if (user != null) {
            user.setStatus(status);
            updateById(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
