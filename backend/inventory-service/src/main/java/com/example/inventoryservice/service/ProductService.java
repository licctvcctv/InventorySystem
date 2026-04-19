package com.example.inventoryservice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {
}
