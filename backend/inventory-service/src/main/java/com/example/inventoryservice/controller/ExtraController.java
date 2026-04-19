package com.example.inventoryservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.inventoryservice.entity.Category;
import com.example.inventoryservice.entity.Warehouse;
import com.example.inventoryservice.mapper.CategoryMapper;
import com.example.inventoryservice.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Object getCategories() {
        return categoryMapper.selectList(null);
    }

    @PostMapping("/categories")
    public Object addCategory(@RequestBody Category c) {
        return categoryMapper.insert(c);
    }

    @DeleteMapping("/categories/{id}")
    public Object deleteCategory(@PathVariable Long id) {
        return categoryMapper.deleteById(id);
    }

    @GetMapping("/warehouses")
    public Object getWarehouses() {
        return warehouseMapper.selectList(null);
    }

    @PostMapping("/warehouses")
    public Object addWarehouse(@RequestBody Warehouse w) {
        return warehouseMapper.insert(w);
    }

    @DeleteMapping("/warehouses/{id}")
    public Object deleteWarehouse(@PathVariable Long id) {
        return warehouseMapper.deleteById(id);
    }

    @PostMapping("/categories/batch-delete")
    public Object batchDeleteCategories(@RequestBody List<Long> ids) {
        return categoryMapper.deleteBatchIds(ids);
    }

    @PostMapping("/warehouses/batch-delete")
    public Object batchDeleteWarehouses(@RequestBody List<Long> ids) {
        return warehouseMapper.deleteBatchIds(ids);
    }
}
