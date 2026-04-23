package com.example.orderservice.service;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.feign.BaseFeignClient;
import com.example.orderservice.feign.InventoryFeignClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 订单事务回滚测试
 * 不使用 @Transactional 注解，直接观察生产代码的本地事务行为
 * 验证当 Feign 调用失败时，本地 @Transactional 回滚订单插入
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("订单事务回滚测试 - OrderTransactionTest")
class OrderTransactionTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private InventoryFeignClient inventoryFeignClient;

    @MockBean
    private BaseFeignClient baseFeignClient;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("DELETE FROM order_items");
        jdbcTemplate.execute("DELETE FROM orders");
    }

    /**
     * 构建用于测试的采购订单（包含明细以触发 Feign 调用）
     */
    private Order buildPurchaseOrder() {
        OrderItem item = new OrderItem();
        item.setProductId(1L);
        item.setProductName("测试商品A");
        item.setProductAttr("i7/16GB/512GB");
        item.setUnit("台");
        item.setQuantity(10);
        item.setPrice(new BigDecimal("5000.00"));
        item.setCostPrice(new BigDecimal("5000.00"));

        Order order = new Order();
        order.setType("PURCHASE");
        order.setSupplierId(1L);
        order.setWarehouseId(1L);
        order.setStatus(0);
        order.setPaymentStatus(0);
        order.setDescription("事务回滚测试 - 采购订单");
        order.setSalesman("张三");
        order.setItems(List.of(item));

        return order;
    }

    @Test
    @DisplayName("Feign 调用失败时订单数据应被回滚 - 验证本地 @Transactional 回滚行为")
    void testOrderRolledBackWhenFeignFails() {
        // 1. 记录操作前的订单数量
        Integer countBefore = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders", Integer.class);

        // 2. 模拟库存服务入库失败
        doThrow(new RuntimeException("库存服务不可用"))
                .when(inventoryFeignClient).stockIn(any());

        // 3. 调用 createOrder，预期抛出异常
        Order order = buildPurchaseOrder();
        assertThrows(Exception.class, () -> orderService.createOrder(order));

        // 4. 记录操作后的订单数量
        Integer countAfter = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders", Integer.class);

        // 5. 断言：订单数量未变化，证明本地 @Transactional 回滚了插入操作
        assertEquals(countBefore, countAfter,
                "Feign 调用失败后订单数量应保持不变，证明事务已回滚");

        // 6. 同时验证 order_items 也被回滚
        Integer itemCountAfter = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM order_items", Integer.class);
        assertEquals(0, itemCountAfter,
                "Feign 调用失败后订单明细数量应为 0，证明事务已回滚");

        // 7. 验证 Feign 确实被调用了（异常发生在调用时）
        verify(inventoryFeignClient, times(1)).stockIn(any());

        // 8. 验证财务记录未被调用（stockIn 失败后不应继续执行）
        verify(baseFeignClient, never()).createFinanceRecord(any());
    }

    @Test
    @DisplayName("正常创建订单成功 - 作为回滚测试的对照组")
    void testOrderCreatedSuccessfullyWhenFeignSucceeds() {
        // 1. 记录操作前的订单数量
        Integer countBefore = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders", Integer.class);

        // 2. Feign 调用正常（默认 mock 不抛异常）
        Order order = buildPurchaseOrder();
        orderService.createOrder(order);

        // 3. 记录操作后的订单数量
        Integer countAfter = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders", Integer.class);

        // 4. 断言：订单数量增加了 1
        assertEquals(countBefore + 1, countAfter,
                "Feign 调用成功时订单应被持久化");

        // 5. 验证订单明细也被插入
        Integer itemCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM order_items", Integer.class);
        assertEquals(1, itemCount,
                "订单明细应被持久化");

        // 6. 验证 Feign 联动调用
        verify(inventoryFeignClient, times(1)).stockIn(any());
        verify(baseFeignClient, times(1)).createFinanceRecord(any());
    }
}
