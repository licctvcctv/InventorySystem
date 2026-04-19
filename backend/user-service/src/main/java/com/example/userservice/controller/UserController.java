package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<com.baomidou.mybatisplus.core.metadata.IPage<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {

        com.baomidou.mybatisplus.core.metadata.IPage<User> pageConfig = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                page, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        if (username != null && !username.isEmpty()) {
            queryWrapper.like(User::getUsername, username);
        }

        com.baomidou.mybatisplus.core.metadata.IPage<User> result = userService.page(pageConfig, queryWrapper);
        result.getRecords().forEach(u -> u.setPassword(null));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody com.example.userservice.dto.RegisterRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
            @RequestBody com.example.userservice.dto.RegisterRequest request) {
        userService.updateUser(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.removeById(id));
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<Boolean> batchDeleteUsers(@RequestBody java.util.List<Long> ids) {
        return ResponseEntity.ok(userService.removeByIds(ids));
    }
}
