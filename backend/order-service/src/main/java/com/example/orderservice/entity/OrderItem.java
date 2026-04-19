package com.example.orderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_items")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private Long productId;        // 商品ID
    private String productName;    // 商品名称
    private String productAttr;    // 属性（配置）
    private String unit;           // 单位
    private Integer quantity;      // 数量
    private BigDecimal price;      // 单价（销售单价/进货单价）
    private BigDecimal costPrice;  // 成本单价
    private BigDecimal amount;     // 金额（进货金额/实销金额）
    private BigDecimal costAmount; // 成本金额
    private Integer stockQuantity; // 库存量
}
