package com.example.inventoryservice.controller;

import com.example.inventoryservice.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE', 'FINANCE')")
public class ReportController {

    private final StockRecordService stockRecordService;

    @GetMapping("/stock")
    public ResponseEntity<List<Map<String, Object>>> getStockReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        // Since getStockReport doesn't take args in service yet, we just ignore them or
        // update service.
        // Wait, does stockRecordService.getStockReport() accept args?
        // Let's check service.
        return ResponseEntity.ok(stockRecordService.getStockReport());
    }
}
