package com.example.inventoryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventoryservice.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
