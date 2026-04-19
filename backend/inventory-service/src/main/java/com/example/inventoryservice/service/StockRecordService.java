package com.example.inventoryservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.entity.StockRecord;
import com.example.inventoryservice.entity.Warehouse;
import com.example.inventoryservice.mapper.ProductMapper;
import com.example.inventoryservice.mapper.StockRecordMapper;
import com.example.inventoryservice.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockRecordService extends ServiceImpl<StockRecordMapper, StockRecord> {

    private final ProductMapper productMapper;
    private final WarehouseMapper warehouseMapper;

    private String generateRecordNo(String prefix) {
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 900) + 100;
        return prefix + timeStr + random;
    }

    @Transactional
    public void stockIn(StockRecord record) {
        Product product = record.getProductId() != null ? productMapper.selectById(record.getProductId()) : null;
        if (product != null) {
            product.setStock(product.getStock() + record.getQuantity());
            productMapper.updateById(product);
            record.setStockQuantity(product.getStock());
            if (record.getProductName() == null) record.setProductName(product.getName());
        }
        record.setType(1);
        if (record.getRecordNo() == null) record.setRecordNo(generateRecordNo("RK"));
        if (record.getUnitPrice() != null && record.getQuantity() != null) {
            record.setTotalAmount(record.getUnitPrice().multiply(new BigDecimal(record.getQuantity())));
        }
        record.setCreatedAt(LocalDateTime.now());
        save(record);
    }

    @Transactional
    public void stockOut(StockRecord record) {
        Product product = record.getProductId() != null ? productMapper.selectById(record.getProductId()) : null;
        if (product != null) {
            if (product.getStock() < record.getQuantity())
                throw new RuntimeException("库存不足");
            product.setStock(product.getStock() - record.getQuantity());
            productMapper.updateById(product);
            record.setStockQuantity(product.getStock());
            if (record.getProductName() == null) record.setProductName(product.getName());
        }
        record.setType(2);
        if (record.getRecordNo() == null) record.setRecordNo(generateRecordNo("CK"));
        if (record.getUnitPrice() != null && record.getQuantity() != null) {
            record.setTotalAmount(record.getUnitPrice().multiply(new BigDecimal(record.getQuantity())));
        }
        record.setCreatedAt(LocalDateTime.now());
        save(record);
    }

    // 保留旧的简单接口兼容
    @Transactional
    public void stockIn(Long productId, Long warehouseId, Long relatedOrderId, Integer quantity, String remark) {
        StockRecord record = StockRecord.builder()
                .productId(productId).warehouseId(warehouseId)
                .relatedOrderId(relatedOrderId).quantity(quantity).remark(remark).build();
        stockIn(record);
    }

    @Transactional
    public void stockOut(Long productId, Long warehouseId, Long relatedOrderId, Integer quantity, String remark) {
        StockRecord record = StockRecord.builder()
                .productId(productId).warehouseId(warehouseId)
                .relatedOrderId(relatedOrderId).quantity(quantity).remark(remark).build();
        stockOut(record);
    }

    public List<Map<String, Object>> getStockReport() {
        QueryWrapper<StockRecord> wrapper = new QueryWrapper<>();
        wrapper.select("product_id", "warehouse_id",
                "sum(case when type = 1 then quantity else -quantity end) as stock");
        wrapper.groupBy("product_id", "warehouse_id");
        wrapper.having("stock > 0");

        List<Map<String, Object>> result = listMaps(wrapper);
        for (Map<String, Object> map : result) {
            Long productId = (Long) map.get("product_id");
            Long warehouseId = (Long) map.get("warehouse_id");
            if (productId != null) {
                Product p = productMapper.selectById(productId);
                map.put("product_name", p != null ? p.getName() : "Unknown");
            }
            if (warehouseId != null) {
                Warehouse w = warehouseMapper.selectById(warehouseId);
                map.put("warehouse_name", w != null ? w.getName() : "Unknown");
            }
        }
        return result;
    }
}
