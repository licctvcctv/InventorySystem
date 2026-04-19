package com.example.orderservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.PaymentRecord;
import com.example.orderservice.mapper.PaymentRecordMapper;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin
@org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('ADMIN', 'SALES', 'PROCUREMENT')")
public class OrderController {

    private final OrderService orderService;
    private final PaymentRecordMapper paymentRecordMapper;

    @GetMapping
    public ResponseEntity<IPage<Order>> getOrders(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(orderService.getOrders(new Page<>(page, size), type, status, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Long id) {
        Order order = orderService.getOrderWithItems(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.removeById(id));
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable("id") Long id,
            @RequestParam("status") Integer status) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setPaymentStatus(status);
            orderService.updateById(order);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/invoice")
    public ResponseEntity<Void> updateInvoiceType(@PathVariable("id") Long id,
            @RequestParam("invoiceType") String invoiceType) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setInvoiceType(invoiceType);
            orderService.updateById(order);
        }
        return ResponseEntity.ok().build();
    }

    /** 查询客户欠款 */
    @GetMapping("/customer-debt")
    public ResponseEntity<?> getCustomerDebt(
            @RequestParam(value = "customerId", required = false) Long customerId) {
        return ResponseEntity.ok(orderService.getCustomerDebt(customerId));
    }

    /** 批量删除订单 */
    @PostMapping("/batch-delete")
    public ResponseEntity<Boolean> batchDelete(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(orderService.removeByIds(ids));
    }

    // ==================== 付款记录 ====================

    /** 查询付款记录 */
    @GetMapping("/payments")
    public ResponseEntity<IPage<PaymentRecord>> getPayments(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sourceOrderId", required = false) Long sourceOrderId,
            @RequestParam(value = "type", required = false) String type) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getType, type != null ? type : "PAYMENT");
        if (sourceOrderId != null) {
            wrapper.eq(PaymentRecord::getSourceOrderId, sourceOrderId);
        }
        wrapper.orderByDesc(PaymentRecord::getCreatedAt);
        return ResponseEntity.ok(paymentRecordMapper.selectPage(new Page<>(page, size), wrapper));
    }

    /** 新增付款记录 */
    @PostMapping("/payments")
    public ResponseEntity<Boolean> createPayment(@RequestBody PaymentRecord record) {
        record.setType("PAYMENT");
        return ResponseEntity.ok(paymentRecordMapper.insert(record) > 0);
    }

    // ==================== 收款记录 ====================

    /** 查询收款记录 */
    @GetMapping("/receipts")
    public ResponseEntity<IPage<PaymentRecord>> getReceipts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sourceOrderId", required = false) Long sourceOrderId,
            @RequestParam(value = "type", required = false) String type) {
        LambdaQueryWrapper<PaymentRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentRecord::getType, type != null ? type : "RECEIPT");
        if (sourceOrderId != null) {
            wrapper.eq(PaymentRecord::getSourceOrderId, sourceOrderId);
        }
        wrapper.orderByDesc(PaymentRecord::getCreatedAt);
        return ResponseEntity.ok(paymentRecordMapper.selectPage(new Page<>(page, size), wrapper));
    }

    /** 新增收款记录 */
    @PostMapping("/receipts")
    public ResponseEntity<Boolean> createReceipt(@RequestBody PaymentRecord record) {
        record.setType("RECEIPT");
        return ResponseEntity.ok(paymentRecordMapper.insert(record) > 0);
    }
}
