package com.example.inventoryservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("stock_records")
public class StockRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String recordNo;       // 单据编号
    private Long productId;
    private String productName;    // 商品名称
    private String productAttr;    // 属性（配置）
    private String unit;           // 单位
    private Long warehouseId;
    private Long relatedOrderId;
    private Integer type;          // 1-IN, 2-OUT
    private Integer quantity;
    private BigDecimal unitPrice;  // 单价
    private BigDecimal totalAmount;// 金额
    private Long supplierId;       // 供应商ID
    private Integer stockQuantity; // 当前库存量
    private String remark;
    private String createdBy;
    private LocalDateTime createdAt;
}
