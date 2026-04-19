package com.example.baseservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.baseservice.entity.FinanceRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FinanceMapper extends BaseMapper<FinanceRecord> {
}
