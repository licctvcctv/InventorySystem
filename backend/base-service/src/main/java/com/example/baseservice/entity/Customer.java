package com.example.baseservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("customers")
public class Customer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String paymentInfo;    // 付款信息
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
