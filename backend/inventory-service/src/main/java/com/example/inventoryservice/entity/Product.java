package com.example.inventoryservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String productCode;    // 商品编码
    private String name;           // 商品名称
    private String brand;          // 商品品牌
    private String productType;    // 商品类型
    private String productAttr;    // 商品属性
    private String unit;           // 计量单位
    private BigDecimal purchasePrice; // 采购价
    private BigDecimal salePrice;  // 售价
    private Long supplierId;       // 供应商ID
    private Long warehouseId;      // 仓库ID
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
