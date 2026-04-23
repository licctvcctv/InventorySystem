package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

/**
 * Sentinel 网关限流/降级配置
 * - 注册自定义限流响应（GatewayCallbackManager）
 * - 加载初始限流规则（可通过 Sentinel 控制台动态修改）
 */
@Configuration
public class SentinelConfig {

    @PostConstruct
    public void init() {
        initCustomBlockHandler();
        initGatewayRules();
    }

    /**
     * 自定义限流/降级返回：统一返回 JSON
     */
    private void initCustomBlockHandler() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"code\":429,\"message\":\"服务繁忙，请稍后重试\"}");
            }
        });
    }

    /**
     * 初始网关限流规则（按路由 ID 限流）
     * 可通过 Sentinel Dashboard 动态调整
     */
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 订单服务：每秒最多 50 个请求
        rules.add(new GatewayFlowRule("order-service")
                .setCount(50)
                .setIntervalSec(1)
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID));

        // 库存服务：每秒最多 100 个请求
        rules.add(new GatewayFlowRule("inventory-service")
                .setCount(100)
                .setIntervalSec(1)
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID));

        // 用户认证：每秒最多 30 个请求（防暴力登录）
        rules.add(new GatewayFlowRule("user-auth")
                .setCount(30)
                .setIntervalSec(1)
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID));

        GatewayRuleManager.loadRules(rules);
    }
}
