package com.example.orderservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("payment_records")
public class PaymentRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private LocalDate paymentDate;
    private String paymentNo;
    private String sourceOrderNo;
    private Long sourceOrderId;
    private String settlementMethod;
    private BigDecimal amountDue;
    private BigDecimal amountPaid;
    private String paymentPerson;
    private String payee;
    private String payer;
    private Boolean isFullyPaid;
    private String remark;
    private String type; // PAYMENT or RECEIPT
    private LocalDateTime createdAt;
}
