package com.example.orderservice.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class BaseFeignFallbackFactory implements FallbackFactory<BaseFeignClient> {

    @Override
    public BaseFeignClient create(Throwable cause) {
        log.error("基础信息服务调用失败: {}", cause.getMessage());
        return new BaseFeignClient() {
            @Override
            public void createFinanceRecord(Map<String, Object> record) {
                throw new RuntimeException("基础信息服务繁忙，请稍后重试");
            }
        };
    }
}
