package com.example.baseservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("finance_records")
public class FinanceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long relatedOrderId; // Source Order ID
    private Integer type; // 1-IN, 2-OUT
    private BigDecimal amount;
    private String category;
    private String targetName;
    private String remark;
    private LocalDateTime createdAt;
    private String sourceOrderNo;   // 来源单据单号
    private String incomeSource;    // 金额收入来源
    private String expenseTarget;   // 金额支出去向
}
