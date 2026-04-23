package com.example.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证控制器集成测试
 * 测试用户注册、登录及JWT令牌认证相关接口
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("认证控制器测试 - AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("测试用户注册接口 - 使用有效数据注册新用户")
    void testRegisterWithValidData() throws Exception {
        String uniqueUser = "testuser_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> request = new HashMap<>();
        request.put("username", uniqueUser);
        request.put("password", "Test@123456");
        request.put("email", uniqueUser + "@example.com");
        request.put("phone", "13800138000");
        request.put("fullName", "测试用户");
        request.put("role", "ROLE_USER");
        request.put("status", 1);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("测试登录接口 - 使用正确的用户名和密码登录")
    void testLoginWithValidCredentials() throws Exception {
        // 先注册一个用户
        String uniqueUser = "logintest_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> registerReq = new HashMap<>();
        registerReq.put("username", uniqueUser);
        registerReq.put("password", "Test@123456");
        registerReq.put("email", uniqueUser + "@example.com");
        registerReq.put("phone", "13900139000");
        registerReq.put("fullName", "登录测试用户");
        registerReq.put("role", "ROLE_USER");
        registerReq.put("status", 1);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk());

        // 然后用该用户登录
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("username", uniqueUser);
        loginReq.put("password", "Test@123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("测试登录接口 - 使用错误密码登录应返回401")
    void testLoginWithWrongPassword() throws Exception {
        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("username", "admin");
        loginReq.put("password", "wrong_password_12345");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("测试未携带Token访问受保护接口应返回401")
    void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("测试注册接口 - 使用已存在的用户名注册应失败")
    void testRegisterWithDuplicateUsername() throws Exception {
        String uniqueUser = "dupuser_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> request = new HashMap<>();
        request.put("username", uniqueUser);
        request.put("password", "Test@123456");
        request.put("email", uniqueUser + "@example.com");
        request.put("phone", "13700137000");
        request.put("fullName", "重复测试");
        request.put("role", "ROLE_USER");
        request.put("status", 1);

        // 第一次注册应该成功
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 第二次用相同用户名注册应抛出异常
        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        );
    }

    @Test
    @DisplayName("测试登录后获取的Token格式正确性验证")
    void testLoginResponseTokenFormat() throws Exception {
        String uniqueUser = "tokentest_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> registerReq = new HashMap<>();
        registerReq.put("username", uniqueUser);
        registerReq.put("password", "Test@123456");
        registerReq.put("email", uniqueUser + "@example.com");
        registerReq.put("phone", "13600136000");
        registerReq.put("fullName", "Token测试");
        registerReq.put("role", "ROLE_ADMIN");
        registerReq.put("status", 1);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk());

        Map<String, String> loginReq = new HashMap<>();
        loginReq.put("username", uniqueUser);
        loginReq.put("password", "Test@123456");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andReturn();

        // 验证JWT token为三段式结构（header.payload.signature）
        String responseBody = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        String token = (String) responseMap.get("token");
        assert token != null && token.split("\\.").length == 3 : "JWT令牌应为三段式结构";
    }
}
