package com.example.roleservice.controller;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 角色权限管理控制器集成测试
 * 测试角色CRUD操作及权限树查询接口
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("角色权限管理控制器测试 - RoleController")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
@DisplayName("测试获取角色列表接口 - 分页查询所有角色")
    void testGetRoleList() throws Exception {
        mockMvc.perform(get("/api/roles")
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
@DisplayName("测试新增角色接口 - 创建新系统角色")
    void testCreateRole() throws Exception {
        Map<String, Object> role = new HashMap<>();
        role.put("name", "测试角色");
        role.put("code", "ROLE_TEST_INTEGRATION");
        role.put("description", "集成测试创建的角色");
        role.put("status", 1);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
@DisplayName("测试获取权限树接口 - 查询树形权限结构")
    void testGetPermissionTree() throws Exception {
        mockMvc.perform(get("/api/permissions/tree")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
@DisplayName("测试获取全部角色列表接口 - 不分页查询")
    void testGetAllRoles() throws Exception {
        mockMvc.perform(get("/api/roles/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
@DisplayName("测试更新角色信息接口")
    void testUpdateRole() throws Exception {
        Map<String, Object> role = new HashMap<>();
        role.put("name", "更新后的角色名称");
        role.put("code", "ROLE_UPDATED");
        role.put("description", "集成测试更新角色描述");
        role.put("status", 1);

        mockMvc.perform(put("/api/roles/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
@DisplayName("测试为角色分配权限接口")
    void testAssignPermissionsToRole() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("permissionIds", Arrays.asList(1L, 2L, 3L));

        mockMvc.perform(post("/api/roles/{id}/permissions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
