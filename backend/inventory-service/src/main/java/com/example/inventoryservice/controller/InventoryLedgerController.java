package com.example.inventoryservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.inventoryservice.entity.InventoryLedger;
import com.example.inventoryservice.service.InventoryLedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-ledger")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
public class InventoryLedgerController {

    private final InventoryLedgerService inventoryLedgerService;

    @GetMapping
    public ResponseEntity<IPage<InventoryLedger>> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<InventoryLedger> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(InventoryLedger::getProductName, keyword)
                    .or().like(InventoryLedger::getProductCode, keyword);
        }
        wrapper.orderByDesc(InventoryLedger::getCreatedAt);
        return ResponseEntity.ok(inventoryLedgerService.page(new Page<>(page, size), wrapper));
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody InventoryLedger ledger) {
        return ResponseEntity.ok(inventoryLedgerService.save(ledger));
    }

    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody InventoryLedger ledger) {
        return ResponseEntity.ok(inventoryLedgerService.updateById(ledger));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(inventoryLedgerService.removeById(id));
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(inventoryLedgerService.removeByIds(ids));
    }
}
