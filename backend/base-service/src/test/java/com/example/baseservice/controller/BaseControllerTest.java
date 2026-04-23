package com.example.baseservice.controller;

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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 基础数据管理控制器集成测试
 * 测试客户、供应商和财务记录管理相关接口
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("基础数据管理控制器测试 - BaseController")
class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取客户列表接口 - 分页查询所有客户")
    void testGetCustomerList() throws Exception {
        mockMvc.perform(get("/api/base/customers")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.current").value(1));
    }

    @Test
    @WithMockUser(roles = "SALES")
    @DisplayName("测试新增客户接口 - 创建新客户信息")
    void testAddCustomer() throws Exception {
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", "集成测试客户有限公司");
        customer.put("contactPerson", "张经理");
        customer.put("phone", "021-88888888");
        customer.put("email", "test@customer.com");
        customer.put("address", "上海市浦东新区测试路100号");
        customer.put("paymentInfo", "月结30天");

        mockMvc.perform(post("/api/base/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取供应商列表接口 - 分页查询所有供应商")
    void testGetSupplierList() throws Exception {
        mockMvc.perform(get("/api/base/suppliers")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    @WithMockUser(roles = "PROCUREMENT")
    @DisplayName("测试新增供应商接口 - 创建新供应商信息")
    void testAddSupplier() throws Exception {
        Map<String, Object> supplier = new HashMap<>();
        supplier.put("name", "集成测试供应商科技有限公司");
        supplier.put("contactPerson", "李供应");
        supplier.put("phone", "0755-66666666");
        supplier.put("email", "test@supplier.com");
        supplier.put("address", "深圳市南山区科技园测试大道88号");

        mockMvc.perform(post("/api/base/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "FINANCE")
    @DisplayName("测试新增财务记录接口 - 创建收入类财务记录")
    void testAddFinanceRecord() throws Exception {
        Map<String, Object> finance = new HashMap<>();
        finance.put("type", 1); // 1-收入
        finance.put("amount", 50000.00);
        finance.put("category", "销售收入");
        finance.put("targetName", "测试客户A");
        finance.put("remark", "集成测试财务记录 - 销售收款");
        finance.put("sourceOrderNo", "SO-TEST-001");
        finance.put("incomeSource", "商品销售");

        mockMvc.perform(post("/api/base/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finance)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取财务记录列表接口 - 分页查询")
    void testGetFinanceRecordList() throws Exception {
        mockMvc.perform(get("/api/base/finance")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    @WithMockUser(roles = "SALES")
    @DisplayName("测试按关键词搜索客户列表")
    void testSearchCustomersByKeyword() throws Exception {
        mockMvc.perform(get("/api/base/customers")
                        .param("page", "1")
                        .param("size", "10")
                        .param("keyword", "测试")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }
}
