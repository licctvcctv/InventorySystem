package com.example.userservice.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT工具类单元测试
 * 测试JWT令牌的生成、解析、验证和过期处理功能
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("JWT工具类测试 - JwtUtils")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${jwt.secret}")
    private String secret;

    private UserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        testUserDetails = new User(
                "testadmin",
                "encoded_password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    @Test
    @DisplayName("测试JWT令牌生成 - 使用UserDetails对象生成令牌")
    void testGenerateTokenWithUserDetails() {
        String token = jwtUtils.generateToken(testUserDetails);

        assertNotNull(token, "生成的JWT令牌不应为空");
        assertTrue(token.split("\\.").length == 3, "JWT令牌应包含三个部分（header.payload.signature）");
        assertFalse(token.isEmpty(), "生成的JWT令牌不应为空字符串");
    }

    @Test
    @DisplayName("测试JWT令牌生成 - 使用用户名字符串生成令牌")
    void testGenerateTokenWithUsername() {
        String token = jwtUtils.generateToken("testuser");

        assertNotNull(token, "生成的JWT令牌不应为空");
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("testuser", extractedUsername, "从令牌中提取的用户名应与原始用户名一致");
    }

    @Test
    @DisplayName("测试JWT令牌验证 - 有效令牌应通过验证")
    void testValidateTokenSuccess() {
        String token = jwtUtils.generateToken(testUserDetails);

        Boolean isValid = jwtUtils.validateToken(token, testUserDetails);

        assertTrue(isValid, "使用正确的UserDetails验证有效令牌应返回true");
    }

    @Test
    @DisplayName("测试JWT令牌验证 - 用户名不匹配应验证失败")
    void testValidateTokenWithWrongUser() {
        String token = jwtUtils.generateToken(testUserDetails);

        UserDetails wrongUser = new User(
                "wronguser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Boolean isValid = jwtUtils.validateToken(token, wrongUser);

        assertFalse(isValid, "使用不匹配的UserDetails验证令牌应返回false");
    }

    @Test
    @DisplayName("测试从JWT令牌中提取用户名")
    void testExtractUsernameFromToken() {
        String token = jwtUtils.generateToken(testUserDetails);

        String username = jwtUtils.extractUsername(token);

        assertEquals("testadmin", username, "提取的用户名应为testadmin");
    }

    @Test
    @DisplayName("测试过期JWT令牌 - 解析过期令牌应抛出异常")
    void testExpiredToken() {
        // 手动构建一个已过期的令牌
        Key signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        String expiredToken = Jwts.builder()
                .setSubject("testadmin")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000)) // 2小时前签发
                .setExpiration(new Date(System.currentTimeMillis() - 3600000)) // 1小时前过期
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // 过期令牌验证应返回false
        Boolean isValid = jwtUtils.validateToken(expiredToken, testUserDetails);
        assertFalse(isValid, "过期的JWT令牌验证应返回false");
    }

    @Test
    @DisplayName("测试获取JWT令牌过期时间")
    void testGetExpirationDateFromToken() {
        String token = jwtUtils.generateToken(testUserDetails);

        Date expirationDate = jwtUtils.getExpirationDateFromToken(token);

        assertNotNull(expirationDate, "令牌过期时间不应为空");
        assertTrue(expirationDate.after(new Date()), "令牌过期时间应在当前时间之后");
    }
}
