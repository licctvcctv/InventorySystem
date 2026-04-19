package com.example.inventoryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventoryservice.entity.InventoryLedger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryLedgerMapper extends BaseMapper<InventoryLedger> {
}
