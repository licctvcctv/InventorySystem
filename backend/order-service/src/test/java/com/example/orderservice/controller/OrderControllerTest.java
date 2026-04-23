package com.example.orderservice.controller;

import com.example.orderservice.feign.BaseFeignClient;
import com.example.orderservice.feign.InventoryFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 订单管理控制器集成测试
 * 使用与前端契约一致的字段名 (price/costPrice/amount)
 * 验证 Feign 联动调用和 @GlobalTransactional 回滚语义
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("订单管理控制器测试 - OrderController")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryFeignClient inventoryFeignClient;

    @MockBean
    private BaseFeignClient baseFeignClient;

    /**
     * 构建与前端契约一致的采购订单请求体
     */
    private Map<String, Object> buildPurchaseOrder() {
        Map<String, Object> order = new HashMap<>();
        order.put("type", "PURCHASE");
        order.put("supplierId", 1);
        order.put("warehouseId", 1);
        order.put("status", 0);
        order.put("paymentStatus", 0);
        order.put("description", "集成测试 - 采购订单");
        order.put("salesman", "张三");

        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", 1);
        item.put("productName", "测试商品A");
        item.put("productAttr", "i7/16GB/512GB");
        item.put("unit", "台");
        item.put("quantity", 10);
        item.put("price", 5000.00);      // 进货单价
        item.put("costPrice", 5000.00);  // 成本单价
        items.add(item);
        order.put("items", items);

        return order;
    }

    /**
     * 构建与前端契约一致的销售订单请求体
     */
    private Map<String, Object> buildSaleOrder() {
        Map<String, Object> order = new HashMap<>();
        order.put("type", "SALE");
        order.put("customerId", 1);
        order.put("warehouseId", 1);
        order.put("status", 0);
        order.put("paymentStatus", 0);
        order.put("description", "集成测试 - 销售订单");
        order.put("salesman", "李四");
        order.put("invoiceType", "增值税专用发票");

        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("productId", 2);
        item.put("productName", "测试商品B");
        item.put("productAttr", "标准配置");
        item.put("unit", "件");
        item.put("quantity", 5);
        item.put("price", 8000.00);      // 销售单价
        item.put("costPrice", 6000.00);  // 成本单价
        items.add(item);
        order.put("items", items);

        return order;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取订单列表接口 - 分页查询所有订单")
    void testGetOrderList() throws Exception {
        mockMvc.perform(get("/api/orders")
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
    @WithMockUser(roles = "PROCUREMENT")
    @DisplayName("测试创建采购订单 - 验证 Feign 联动入库和财务记录")
    void testCreatePurchaseOrder() throws Exception {
        Map<String, Object> order = buildPurchaseOrder();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isOk());

        // 验证通过 Feign 调用了库存入库（1 个商品明细 → 1 次调用）
        verify(inventoryFeignClient, times(1)).stockIn(argThat(record -> {
            // 验证传递的参数包含正确的字段
            return record.containsKey("productId")
                    && record.containsKey("warehouseId")
                    && record.containsKey("unitPrice")
                    && record.containsKey("quantity")
                    && Integer.valueOf(10).equals(record.get("quantity"));
        }));

        // 验证没有调用出库
        verify(inventoryFeignClient, never()).stockOut(any());

        // 验证调用了财务记录（type=2 表示支出）
        verify(baseFeignClient, times(1)).createFinanceRecord(argThat(record ->
                Integer.valueOf(2).equals(record.get("type"))
                        && "采购支出".equals(record.get("category"))
        ));
    }

    @Test
    @WithMockUser(roles = "SALES")
    @DisplayName("测试创建销售订单 - 验证 Feign 联动出库和财务记录")
    void testCreateSaleOrder() throws Exception {
        Map<String, Object> order = buildSaleOrder();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andDo(print())
                .andExpect(status().isOk());

        // 验证通过 Feign 调用了库存出库
        verify(inventoryFeignClient, times(1)).stockOut(argThat(record ->
                record.containsKey("productId")
                        && Integer.valueOf(5).equals(record.get("quantity"))
        ));

        // 验证没有调用入库
        verify(inventoryFeignClient, never()).stockIn(any());

        // 验证调用了财务记录（type=1 表示收入）
        verify(baseFeignClient, times(1)).createFinanceRecord(argThat(record ->
                Integer.valueOf(1).equals(record.get("type"))
                        && "销售收入".equals(record.get("category"))
        ));
    }

    @Test
    @WithMockUser(roles = "PROCUREMENT")
    @DisplayName("测试 Feign 调用失败时订单创建应失败 - 验证事务回滚语义")
    void testCreateOrderFailsWhenFeignThrows() throws Exception {
        // 模拟库存服务入库失败
        doThrow(new RuntimeException("库存服务不可用"))
                .when(inventoryFeignClient).stockIn(any());

        Map<String, Object> order = buildPurchaseOrder();

        // 异常未被吞掉：直接抛出，@GlobalTransactional 可触发回滚
        assertThrows(Exception.class, () ->
                mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
        );

        // 验证 Feign 确实被调用了（异常发生在调用时）
        verify(inventoryFeignClient, times(1)).stockIn(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试更新订单付款状态接口 - 设置为已付款")
    void testUpdatePaymentStatus() throws Exception {
        // 先创建一个无明细的订单（不触发 Feign）
        Map<String, Object> order = new HashMap<>();
        order.put("type", "PURCHASE");
        order.put("supplierId", 1);
        order.put("warehouseId", 1);
        order.put("totalAmount", 5000.00);
        order.put("status", 0);
        order.put("paymentStatus", 0);
        order.put("items", new ArrayList<>());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/orders/{id}/payment", 1)
                        .param("status", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试按类型筛选订单列表 - 仅查询采购订单")
    void testGetOrdersByType() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .param("page", "1")
                        .param("size", "10")
                        .param("type", "PURCHASE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }
}
