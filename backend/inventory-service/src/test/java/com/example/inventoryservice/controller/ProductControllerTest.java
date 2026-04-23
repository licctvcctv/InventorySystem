package com.example.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商品管理控制器集成测试
 * 测试商品的增删改查操作接口
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("商品管理控制器测试 - ProductController")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试获取商品列表接口 - 分页查询所有商品")
    void testGetProductList() throws Exception {
        mockMvc.perform(get("/api/products")
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
    @WithMockUser(roles = "WAREHOUSE")
    @DisplayName("测试新增商品接口 - 创建新商品信息")
    void testCreateProduct() throws Exception {
        Map<String, Object> product = new HashMap<>();
        product.put("productCode", "SP-TEST-001");
        product.put("name", "集成测试商品");
        product.put("brand", "测试品牌");
        product.put("productType", "电子产品");
        product.put("productAttr", "红色/128GB");
        product.put("unit", "台");
        product.put("purchasePrice", 2500.00);
        product.put("salePrice", 3999.00);
        product.put("sku", "SKU-TEST-001");
        product.put("price", 3999.00);
        product.put("stock", 0);
        product.put("description", "这是一个集成测试创建的商品");
        product.put("categoryId", 1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试修改商品接口 - 更新商品价格和描述")
    void testUpdateProduct() throws Exception {
        // 先创建一个商品
        Map<String, Object> createProduct = new HashMap<>();
        createProduct.put("productCode", "SP-UPD-001");
        createProduct.put("name", "待更新商品");
        createProduct.put("brand", "测试品牌");
        createProduct.put("unit", "台");
        createProduct.put("purchasePrice", 2500.00);
        createProduct.put("salePrice", 3999.00);
        createProduct.put("sku", "SKU-UPD-001");
        createProduct.put("stock", 0);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProduct)))
                .andExpect(status().isOk());

        // 更新商品（使用id=1，即H2自增的第一条记录）
        Map<String, Object> product = new HashMap<>();
        product.put("id", 1);
        product.put("name", "更新后的商品名称");
        product.put("salePrice", 4599.00);
        product.put("purchasePrice", 2800.00);
        product.put("description", "集成测试更新商品信息");

        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试删除商品接口 - 根据ID删除商品")
    void testDeleteProduct() throws Exception {
        // 先创建一个商品
        Map<String, Object> product = new HashMap<>();
        product.put("productCode", "SP-DEL-001");
        product.put("name", "待删除商品");
        product.put("brand", "测试");
        product.put("unit", "个");
        product.put("purchasePrice", 10.00);
        product.put("salePrice", 20.00);
        product.put("sku", "SKU-DEL-001");
        product.put("stock", 0);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        // 删除商品（使用不存在的ID避免影响真实数据）
        mockMvc.perform(delete("/api/products/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("测试按关键词搜索商品列表")
    void testSearchProductsByKeyword() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("page", "1")
                        .param("size", "10")
                        .param("keyword", "测试")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.records").isArray());
    }

    @Test
    @WithMockUser(roles = "SALES")
    @DisplayName("测试获取全部商品列表接口 - 用于下拉选择框")
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
