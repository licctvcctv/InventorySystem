package com.example.inventoryservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.inventoryservice.entity.Category;
import com.example.inventoryservice.entity.Warehouse;
import com.example.inventoryservice.mapper.CategoryMapper;
import com.example.inventoryservice.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extra")
@CrossOrigin
public class ExtraController {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object getCategories() {
        return categoryMapper.selectList(null);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object addCategory(@RequestBody Category c) {
        return categoryMapper.insert(c);
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object deleteCategory(@PathVariable Long id) {
        return categoryMapper.deleteById(id);
    }

    @GetMapping("/warehouses")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE', 'PROCUREMENT')")
    public Object getWarehouses() {
        return warehouseMapper.selectList(null);
    }

    @PostMapping("/warehouses")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object addWarehouse(@RequestBody Warehouse w) {
        return warehouseMapper.insert(w);
    }

    @DeleteMapping("/warehouses/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object deleteWarehouse(@PathVariable Long id) {
        return warehouseMapper.deleteById(id);
    }

    @PostMapping("/categories/batch-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object batchDeleteCategories(@RequestBody List<Long> ids) {
        return categoryMapper.deleteBatchIds(ids);
    }

    @PostMapping("/warehouses/batch-delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public Object batchDeleteWarehouses(@RequestBody List<Long> ids) {
        return warehouseMapper.deleteBatchIds(ids);
    }
}
