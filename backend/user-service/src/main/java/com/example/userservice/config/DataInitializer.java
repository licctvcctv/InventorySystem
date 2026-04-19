package com.example.userservice.config;

import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "admin"));
        if (admin == null) {
            admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("123456"))
                    .email("admin@example.com")
                    .phone("13800000000")
                    .role("ROLE_ADMIN")
                    .status(1)
                    .build();
            userMapper.insert(admin);
            System.out.println("Default admin account created: admin / 123456");
        } else {
            // 强制重置密码，确保密码与加密方式一致
            admin.setPassword(passwordEncoder.encode("123456"));
            userMapper.updateById(admin);
            System.out.println("Admin account password reset to: 123456");
        }
    }
}
