package com.example.baseservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.baseservice.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
}
