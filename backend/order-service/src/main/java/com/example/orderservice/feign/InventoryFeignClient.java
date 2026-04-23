package com.example.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 库存服务 Feign 客户端
 * 用于订单服务远程调用库存服务实现入库/出库联动
 */
@FeignClient(name = "inventory-service", fallbackFactory = InventoryFeignFallbackFactory.class)
public interface InventoryFeignClient {

    @PostMapping("/api/stock/in")
    void stockIn(@RequestBody Map<String, Object> record);

    @PostMapping("/api/stock/out")
    void stockOut(@RequestBody Map<String, Object> record);

    @GetMapping("/api/products/all")
    Object getAllProducts();
}
