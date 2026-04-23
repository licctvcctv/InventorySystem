package com.example.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * AuthGlobalFilter 单元测试
 * 直接测试过滤器 bean，使用 mock GatewayFilterChain
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("网关鉴权过滤器测试 - AuthGlobalFilter")
class AuthGlobalFilterTest {

    @Autowired
    private AuthGlobalFilter authGlobalFilter;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private GatewayFilterChain chain;

    @BeforeEach
    void setUp() {
        chain = mock(GatewayFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 生成有效的 JWT token
     */
    private String generateValidToken(String userId, List<String> roles) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 使用错误密钥生成 token（模拟签名不匹配）
     */
    private String generateTokenWithWrongKey(String userId) {
        Key wrongKey = Keys.hmacShaKeyFor(
                "WrongSecretKeyThatIsLongEnoughForHS256Algorithm!".getBytes());
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(wrongKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    @DisplayName("白名单路径 /api/auth/login 无需 token 即可通过")
    void testWhitelistPathPassesWithoutToken() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/login")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        // chain.filter 被调用说明请求被放行
        verify(chain, times(1)).filter(any());
        // 未设置 401
        assertNotEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("白名单路径 /api/auth/register 无需 token 即可通过")
    void testWhitelistRegisterPathPassesWithoutToken() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/auth/register")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, times(1)).filter(any());
    }

    @Test
    @DisplayName("缺少 Authorization 请求头返回 401")
    void testMissingAuthorizationHeaderReturns401() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/orders")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        // chain 不应被调用（请求被拦截）
        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("Authorization 格式不正确（缺少 Bearer 前缀）返回 401")
    void testMalformedTokenReturns401() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/orders")
                .header("Authorization", "InvalidTokenWithoutBearerPrefix")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("无效 JWT 签名返回 401")
    void testInvalidJwtSignatureReturns401() {
        String tokenWithWrongKey = generateTokenWithWrongKey("1");

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/orders")
                .header("Authorization", "Bearer " + tokenWithWrongKey)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain, never()).filter(any());
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    @DisplayName("有效 JWT 通过验证并设置 X-User-Id 和 X-User-Roles 请求头")
    void testValidJwtPassesAndSetsHeaders() {
        String validToken = generateValidToken("42", List.of("ROLE_ADMIN", "ROLE_USER"));

        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/orders")
                .header("Authorization", "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // 捕获传递给 chain 的 exchange，以验证下游请求头
        when(chain.filter(any())).thenAnswer(invocation -> {
            org.springframework.web.server.ServerWebExchange mutatedExchange =
                    invocation.getArgument(0);
            // 验证下游请求中包含用户信息头
            String userId = mutatedExchange.getRequest().getHeaders().getFirst("X-User-Id");
            String roles = mutatedExchange.getRequest().getHeaders().getFirst("X-User-Roles");
            assertEquals("42", userId);
            assertEquals("ROLE_ADMIN,ROLE_USER", roles);
            return Mono.empty();
        });

        StepVerifier.create(authGlobalFilter.filter(exchange, chain))
                .verifyComplete();

        // chain.filter 被调用说明请求被放行
        verify(chain, times(1)).filter(any());
    }
}
