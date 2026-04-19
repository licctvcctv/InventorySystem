package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin
// Allow Finance to see reports too
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES', 'PROCUREMENT', 'FINANCE')")
public class ReportController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getOrderReport(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return ResponseEntity.ok(orderService.getReport(startDate, endDate));
    }
}
