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
@TableName("inventory_ledger")
public class InventoryLedger {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String productType;
    private String productAttr;
    private String unit;
    private Integer finalStock;
    private BigDecimal salePrice;
    private BigDecimal saleAmount;
    private Long supplierId;
    private Long warehouseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
