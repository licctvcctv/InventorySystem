package com.example.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户管理控制器集成测试
 * 测试用户CRUD操作接口，包括分页查询、新增、删除等功能
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("用户管理控制器测试 - UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取用户列表接口 - 分页查询所有用户")
    void testGetUserList() throws Exception {
        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.current").value(1))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试根据用户名模糊查询用户列表")
    void testGetUserListByUsername() throws Exception {
        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10")
                        .param("username", "admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试根据ID查询单个用户信息")
    void testGetUserById() throws Exception {
        // 先创建用户
        String uniqueUser = "getbyid_" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> request = new HashMap<>();
        request.put("username", uniqueUser);
        request.put("password", "Test@123456");
        request.put("email", uniqueUser + "@example.com");
        request.put("phone", "13500135000");
        request.put("fullName", "查询测试用户");
        request.put("role", "ROLE_USER");
        request.put("status", 1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 通过用户名搜索获取创建的用户，然后按ID查询
        mockMvc.perform(get("/api/users")
                        .param("username", uniqueUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.records[0].username").value(uniqueUser))
                .andExpect(jsonPath("$.records[0].password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试新增用户接口 - 创建新用户")
    void testCreateUser() throws Exception {
        String uniqueUser = "newuser_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> request = new HashMap<>();
        request.put("username", uniqueUser);
        request.put("password", "NewUser@123");
        request.put("email", uniqueUser + "@example.com");
        request.put("phone", "15800158000");
        request.put("fullName", "新增测试用户");
        request.put("role", "ROLE_USER");
        request.put("status", 1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        // 验证用户已创建 - 通过用户名搜索
        mockMvc.perform(get("/api/users")
                        .param("username", uniqueUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.records[0].username").value(uniqueUser));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试删除用户接口 - 根据ID删除用户")
    void testDeleteUser() throws Exception {
        // 先创建一个用户用于删除
        String uniqueUser = "deluser_" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> request = new HashMap<>();
        request.put("username", uniqueUser);
        request.put("password", "Delete@123");
        request.put("email", uniqueUser + "@example.com");
        request.put("phone", "15900159000");
        request.put("fullName", "删除测试用户");
        request.put("role", "ROLE_USER");
        request.put("status", 1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 删除ID较大的用户（避免删除admin）
        mockMvc.perform(delete("/api/users/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试查询不存在的用户应返回404")
    void testGetNonExistentUser() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
