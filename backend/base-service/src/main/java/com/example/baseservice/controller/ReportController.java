package com.example.baseservice.controller;

import com.example.baseservice.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'FINANCE')")
public class ReportController {

    private final FinanceService financeService;

    @GetMapping("/finance")
    public ResponseEntity<Map<String, Object>> getFinanceReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return ResponseEntity.ok(financeService.getReport(startDate, endDate));
    }
}
