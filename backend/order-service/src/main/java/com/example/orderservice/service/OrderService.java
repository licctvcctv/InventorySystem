package com.example.orderservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.orderservice.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    void createOrder(Order order);
    void updateOrder(Order order);
    Order getOrderWithItems(Long id);
    IPage<Order> getOrders(Page<Order> page, String type, String status, String keyword);
    Map<String, Object> getReport(String startDate, String endDate);
    List<Map<String, Object>> getCustomerDebt(Long customerId);
}
