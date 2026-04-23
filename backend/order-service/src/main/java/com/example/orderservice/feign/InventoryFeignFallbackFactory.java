package com.example.orderservice.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class InventoryFeignFallbackFactory implements FallbackFactory<InventoryFeignClient> {

    @Override
    public InventoryFeignClient create(Throwable cause) {
        log.error("库存服务调用失败: {}", cause.getMessage());
        return new InventoryFeignClient() {
            @Override
            public void stockIn(Map<String, Object> record) {
                throw new RuntimeException("库存服务繁忙，请稍后重试");
            }

            @Override
            public void stockOut(Map<String, Object> record) {
                throw new RuntimeException("库存服务繁忙，请稍后重试");
            }

            @Override
            public Object getAllProducts() {
                return Collections.emptyList();
            }
        };
    }
}
