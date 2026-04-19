package com.example.inventoryservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE', 'SALES', 'PROCUREMENT')")
    public ResponseEntity<IPage<Product>> getProducts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword)
                    .or().like(Product::getSku, keyword)
                    .or().like(Product::getProductCode, keyword);
        }
        wrapper.orderByDesc(Product::getCreatedAt);
        return ResponseEntity.ok(productService.page(new Page<>(page, size), wrapper));
    }

    /** 获取全部商品列表（不分页，用于下拉选择） */
    @GetMapping("/all")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE', 'SALES', 'PROCUREMENT')")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.list());
    }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public ResponseEntity<Boolean> createProduct(@RequestBody Product product) {
        if (product.getStock() == null) product.setStock(0);
        return ResponseEntity.ok(productService.save(product));
    }

    @PutMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public ResponseEntity<Boolean> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.updateById(product));
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.removeById(id));
    }

    @PostMapping("/batch-delete")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public ResponseEntity<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(productService.removeByIds(ids));
    }
}
