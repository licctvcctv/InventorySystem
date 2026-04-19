package com.example.baseservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.baseservice.entity.Customer;
import com.example.baseservice.entity.FinanceRecord;
import com.example.baseservice.entity.Supplier;
import com.example.baseservice.mapper.CustomerMapper;
import com.example.baseservice.mapper.FinanceMapper;
import com.example.baseservice.mapper.SupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base")
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
public class BaseController {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private FinanceMapper financeMapper;

    // --- Customer ---
    @GetMapping("/customers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    public Object getCustomers(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<Customer> w = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty())
            w.like(Customer::getName, keyword);
        return customerMapper.selectPage(new Page<>(page, size), w);
    }

    @PostMapping("/customers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    public Object addCustomer(@RequestBody Customer c) {
        return customerMapper.insert(c);
    }

    @PutMapping("/customers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    public Object updateCustomer(@RequestBody Customer c) {
        return customerMapper.updateById(c);
    }

    @DeleteMapping("/customers/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN')")
    public Object deleteCustomer(@PathVariable("id") Long id) {
        return customerMapper.deleteById(id);
    }

    // --- Supplier ---
    @GetMapping("/suppliers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT')")
    public Object getSuppliers(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<Supplier> w = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty())
            w.like(Supplier::getName, keyword);
        return supplierMapper.selectPage(new Page<>(page, size), w);
    }

    @PostMapping("/suppliers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT')")
    public Object addSupplier(@RequestBody Supplier s) {
        return supplierMapper.insert(s);
    }

    @PutMapping("/suppliers")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT')")
    public Object updateSupplier(@RequestBody Supplier s) {
        return supplierMapper.updateById(s);
    }

    @DeleteMapping("/suppliers/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN')")
    public Object deleteSupplier(@PathVariable("id") Long id) {
        return supplierMapper.deleteById(id);
    }

    // --- Finance ---
    @GetMapping("/finance")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    public Object getFinance(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return financeMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<FinanceRecord>().orderByDesc(FinanceRecord::getCreatedAt));
    }

    @PostMapping("/finance")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    public Object addFinance(@RequestBody FinanceRecord f) {
        return financeMapper.insert(f);
    }

    @PutMapping("/finance")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    public Object updateFinance(@RequestBody FinanceRecord f) {
        return financeMapper.updateById(f);
    }

    @DeleteMapping("/finance/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    public Object deleteFinance(@PathVariable("id") Long id) {
        return financeMapper.deleteById(id);
    }

    @PostMapping("/finance/batch-delete")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
    public Object batchDeleteFinance(@RequestBody List<Long> ids) {
        return financeMapper.deleteBatchIds(ids);
    }

    // --- Batch Delete ---
    @PostMapping("/customers/batch-delete")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN')")
    public Object batchDeleteCustomers(@RequestBody List<Long> ids) {
        return customerMapper.deleteBatchIds(ids);
    }

    @PostMapping("/suppliers/batch-delete")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN')")
    public Object batchDeleteSuppliers(@RequestBody List<Long> ids) {
        return supplierMapper.deleteBatchIds(ids);
    }
}
