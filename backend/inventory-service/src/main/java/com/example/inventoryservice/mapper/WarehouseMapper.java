package com.example.inventoryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventoryservice.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
}
