package com.example.orderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;
    private Long customerId;
    private Long supplierId;
    private Long warehouseId;
    private String type; // PURCHASE, SALE
    private BigDecimal totalAmount;
    private Integer status; // 0-Pending, 1-Completed, 2-Cancelled
    private String description;
    private String createdBy;

    // 新增字段
    private String invoiceType;       // 票据类型
    private String salesman;          // 业务员
    private BigDecimal dealAmount;    // 成交金额
    private BigDecimal totalCost;     // 整单成本
    private BigDecimal orderDebt;     // 本单欠款
    private BigDecimal discount;      // 折扣百分比
    private Integer paymentStatus;    // 收/付款状态：0-未付, 1-部分, 2-已付
    private String customerAddress;   // 客户地址
    private String customerPaymentInfo; // 客户付款信息

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private List<OrderItem> items;

    @TableField(exist = false)
    private String supplierName;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String warehouseName;
}
