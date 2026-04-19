package com.example.inventoryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.inventoryservice.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
