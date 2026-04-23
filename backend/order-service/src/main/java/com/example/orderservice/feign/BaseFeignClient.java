package com.example.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 基础信息服务 Feign 客户端
 * 用于订单服务远程调用基础服务获取客户/供应商信息
 */
@FeignClient(name = "base-service", fallbackFactory = BaseFeignFallbackFactory.class)
public interface BaseFeignClient {

    @PostMapping("/api/base/finance")
    void createFinanceRecord(@RequestBody Map<String, Object> record);
}
