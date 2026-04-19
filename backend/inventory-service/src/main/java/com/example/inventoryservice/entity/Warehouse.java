package com.example.inventoryservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("warehouses")
public class Warehouse {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String manager;
    private String contact;
    private LocalDateTime createdAt;
}
