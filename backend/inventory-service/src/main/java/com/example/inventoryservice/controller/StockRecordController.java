package com.example.inventoryservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.inventoryservice.entity.StockRecord;
import com.example.inventoryservice.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
public class StockRecordController {

    private final StockRecordService stockRecordService;

    @PostMapping("/in")
    public ResponseEntity<Void> stockIn(@RequestBody StockRecord record) {
        stockRecordService.stockIn(record);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/out")
    public ResponseEntity<Void> stockOut(@RequestBody StockRecord record) {
        stockRecordService.stockOut(record);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Boolean> updateRecord(@RequestBody StockRecord record) {
        return ResponseEntity.ok(stockRecordService.updateById(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRecord(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockRecordService.removeById(id));
    }

    @GetMapping("/records")
    public ResponseEntity<IPage<StockRecord>> getRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "keyword", required = false) String keyword) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        if (type != null) wrapper.eq(StockRecord::getType, type);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(StockRecord::getRecordNo, keyword)
                    .or().like(StockRecord::getProductName, keyword));
        }
        wrapper.orderByDesc(StockRecord::getCreatedAt);
        return ResponseEntity.ok(stockRecordService.page(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockRecord> getRecord(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockRecordService.getById(id));
    }

    @PostMapping("/batch-delete")
    public ResponseEntity<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(stockRecordService.removeByIds(ids));
    }
}
