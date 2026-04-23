package com.example.inventoryservice.controller;

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
 * 出入库记录控制器集成测试
 * 测试商品入库、出库操作及出入库记录查询接口
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("出入库记录控制器测试 - StockRecordController")
class StockRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "WAREHOUSE")
    @DisplayName("测试商品入库接口 - 正常入库操作")
    void testStockIn() throws Exception {
        Map<String, Object> record = new HashMap<>();
        record.put("productId", 1);
        record.put("productName", "测试入库商品");
        record.put("productAttr", "默认规格");
        record.put("unit", "台");
        record.put("warehouseId", 1);
        record.put("quantity", 100);
        record.put("unitPrice", 150.00);
        record.put("supplierId", 1);
        record.put("remark", "集成测试入库");
        record.put("createdBy", "warehouse_admin");

        mockMvc.perform(post("/api/stock/in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "WAREHOUSE")
    @DisplayName("测试商品出库接口 - 正常出库操作")
    void testStockOut() throws Exception {
        // 先入库以确保有足够库存
        Map<String, Object> inRecord = new HashMap<>();
        inRecord.put("productId", 1);
        inRecord.put("productName", "出库测试商品");
        inRecord.put("unit", "台");
        inRecord.put("warehouseId", 1);
        inRecord.put("quantity", 200);
        inRecord.put("unitPrice", 100.00);
        inRecord.put("remark", "为出库测试准备库存");
        inRecord.put("createdBy", "warehouse_admin");

        mockMvc.perform(post("/api/stock/in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inRecord)))
                .andExpect(status().isOk());

        // 然后执行出库
        Map<String, Object> outRecord = new HashMap<>();
        outRecord.put("productId", 1);
        outRecord.put("productName", "出库测试商品");
        outRecord.put("unit", "台");
        outRecord.put("warehouseId", 1);
        outRecord.put("quantity", 50);
        outRecord.put("unitPrice", 200.00);
        outRecord.put("remark", "集成测试出库");
        outRecord.put("createdBy", "warehouse_admin");

        mockMvc.perform(post("/api/stock/out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outRecord)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "WAREHOUSE")
    @DisplayName("测试商品出库接口 - 库存不足应返回错误")
    void testStockOutWithInsufficientStock() throws Exception {
        Map<String, Object> record = new HashMap<>();
        record.put("productId", 1);
        record.put("productName", "库存不足测试");
        record.put("unit", "台");
        record.put("warehouseId", 1);
        record.put("quantity", 999999); // 超大数量，确保库存不足
        record.put("unitPrice", 100.00);
        record.put("remark", "库存不足测试");
        record.put("createdBy", "warehouse_admin");

        mockMvc.perform(post("/api/stock/out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试查询出入库记录列表接口 - 分页查询")
    void testGetStockRecords() throws Exception {
        mockMvc.perform(get("/api/stock/records")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray())
                .andExpect(jsonPath("$.current").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试按类型筛选出入库记录 - 仅查询入库记录")
    void testGetStockRecordsByType() throws Exception {
        mockMvc.perform(get("/api/stock/records")
                        .param("page", "1")
                        .param("size", "10")
                        .param("type", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试按关键词搜索出入库记录")
    void testSearchStockRecords() throws Exception {
        mockMvc.perform(get("/api/stock/records")
                        .param("page", "1")
                        .param("size", "10")
                        .param("keyword", "测试")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }
}
