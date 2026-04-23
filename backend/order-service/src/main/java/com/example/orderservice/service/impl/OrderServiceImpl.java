package com.example.orderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.feign.BaseFeignClient;
import com.example.orderservice.feign.InventoryFeignClient;
import com.example.orderservice.mapper.OrderItemMapper;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderItemMapper orderItemMapper;
    private final InventoryFeignClient inventoryFeignClient;
    private final BaseFeignClient baseFeignClient;

    @Override
    @io.seata.spring.annotation.GlobalTransactional(rollbackFor = Exception.class)
    @Transactional
    public void createOrder(Order order) {
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 900) + 100;
        String prefix = "PURCHASE".equals(order.getType()) ? "PUR" : "SAL";
        order.setOrderNo(prefix + timeStr + random);

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        if (order.getStatus() == null) order.setStatus(0);
        if (order.getPaymentStatus() == null) order.setPaymentStatus(0);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                if (item.getPrice() != null && item.getQuantity() != null) {
                    BigDecimal amount = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                    item.setAmount(amount);
                    total = total.add(amount);
                }
                if (item.getCostPrice() != null && item.getQuantity() != null) {
                    BigDecimal costAmt = item.getCostPrice().multiply(new BigDecimal(item.getQuantity()));
                    item.setCostAmount(costAmt);
                    totalCost = totalCost.add(costAmt);
                }
            }
        }
        order.setTotalAmount(total);
        if (order.getTotalCost() == null) order.setTotalCost(totalCost);
        if (order.getDealAmount() == null) order.setDealAmount(total);
        if (order.getOrderDebt() == null) {
            order.setOrderDebt(order.getDealAmount().subtract(BigDecimal.ZERO));
        }

        save(order);

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }

        // ========== 通过 OpenFeign 联动库存服务和财务服务 ==========
        // 不 catch 异常：下游失败时 @GlobalTransactional 会回滚整个分布式事务
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                if (item.getProductId() == null) continue;

                Map<String, Object> stockRecord = new HashMap<>();
                stockRecord.put("productId", item.getProductId());
                stockRecord.put("warehouseId", order.getWarehouseId());
                stockRecord.put("relatedOrderId", order.getId());
                stockRecord.put("quantity", item.getQuantity());
                stockRecord.put("unitPrice", item.getPrice());
                stockRecord.put("productName", item.getProductName());
                stockRecord.put("productAttr", item.getProductAttr());
                stockRecord.put("unit", item.getUnit());
                stockRecord.put("supplierId", order.getSupplierId());
                stockRecord.put("remark", "订单 " + order.getOrderNo() + " 自动联动");

                if ("PURCHASE".equals(order.getType())) {
                    inventoryFeignClient.stockIn(stockRecord);
                } else if ("SALE".equals(order.getType())) {
                    inventoryFeignClient.stockOut(stockRecord);
                }
            }
        }

        // 联动财务记录
        Map<String, Object> financeRecord = new HashMap<>();
        financeRecord.put("relatedOrderId", order.getId());
        financeRecord.put("sourceOrderNo", order.getOrderNo());
        financeRecord.put("amount", order.getTotalAmount());
        if ("PURCHASE".equals(order.getType())) {
            financeRecord.put("type", 2); // 支出
            financeRecord.put("category", "采购支出");
            financeRecord.put("expenseTarget", order.getSupplierId());
        } else {
            financeRecord.put("type", 1); // 收入
            financeRecord.put("category", "销售收入");
            financeRecord.put("incomeSource", order.getCustomerId());
        }
        baseFeignClient.createFinanceRecord(financeRecord);
    }

    @Override
    @Transactional
    public void updateOrder(Order order) {
        order.setUpdatedAt(LocalDateTime.now());

        // 重新计算金额
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                if (item.getPrice() != null && item.getQuantity() != null) {
                    BigDecimal amount = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                    item.setAmount(amount);
                    total = total.add(amount);
                }
                if (item.getCostPrice() != null && item.getQuantity() != null) {
                    BigDecimal costAmt = item.getCostPrice().multiply(new BigDecimal(item.getQuantity()));
                    item.setCostAmount(costAmt);
                    totalCost = totalCost.add(costAmt);
                }
            }
        }
        order.setTotalAmount(total);
        order.setTotalCost(totalCost);
        if (order.getDealAmount() == null) order.setDealAmount(total);

        updateById(order);

        // 删除旧明细，重新插入
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>()
                .eq(OrderItem::getOrderId, order.getId()));
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setId(null);
                item.setOrderId(order.getId());
                orderItemMapper.insert(item);
            }
        }
    }

    @Override
    public Order getOrderWithItems(Long id) {
        Order order = getById(id);
        if (order != null) {
            List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
            order.setItems(items);
        }
        return order;
    }

    @Override
    public IPage<Order> getOrders(Page<Order> page, String type, String status, String keyword) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (type != null && !type.isEmpty()) wrapper.eq(Order::getType, type);
        if (status != null && !status.isEmpty()) wrapper.eq(Order::getStatus, status);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Order::getOrderNo, keyword)
                    .or().like(Order::getDescription, keyword));
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public Map<String, Object> getReport(String startDate, String endDate) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.select("type", "count(*) as count", "sum(total_amount) as totalAmount");
        if (startDate != null && endDate != null) {
            wrapper.between("created_at", startDate + " 00:00:00", endDate + " 23:59:59");
        }
        wrapper.groupBy("type");

        List<Map<String, Object>> list = listMaps(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("details", list);

        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalPurchases = BigDecimal.ZERO;
        for (Map<String, Object> map : list) {
            String t = (String) map.get("type");
            BigDecimal amount = map.get("totalAmount") != null ? (BigDecimal) map.get("totalAmount") : BigDecimal.ZERO;
            if ("SALE".equals(t)) totalSales = totalSales.add(amount);
            else if ("PURCHASE".equals(t)) totalPurchases = totalPurchases.add(amount);
        }
        result.put("totalSales", totalSales);
        result.put("totalPurchases", totalPurchases);
        result.put("netIncome", totalSales.subtract(totalPurchases));
        return result;
    }

    @Override
    public List<Map<String, Object>> getCustomerDebt(Long customerId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.select("customer_id", "sum(order_debt) as totalDebt");
        wrapper.eq("type", "SALE");
        wrapper.gt("order_debt", 0);
        if (customerId != null) wrapper.eq("customer_id", customerId);
        wrapper.groupBy("customer_id");
        return listMaps(wrapper);
    }
}
