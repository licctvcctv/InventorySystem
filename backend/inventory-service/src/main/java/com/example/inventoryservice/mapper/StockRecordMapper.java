package com.example.inventoryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventoryservice.entity.StockRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockRecordMapper extends BaseMapper<StockRecord> {
}
