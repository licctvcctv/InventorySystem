package com.example.baseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.baseservice.entity.FinanceRecord;
import com.example.baseservice.mapper.FinanceMapper;
import com.example.baseservice.service.FinanceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl extends ServiceImpl<FinanceMapper, FinanceRecord> implements FinanceService {

    @Override
    public Map<String, Object> getReport(String startDate, String endDate) {
        QueryWrapper<FinanceRecord> wrapper = new QueryWrapper<>();
        wrapper.select("type", "sum(amount) as totalAmount");
        if (startDate != null && endDate != null) {
            String start = startDate + " 00:00:00";
            String end = endDate + " 23:59:59";
            wrapper.between("created_at", start, end);
        }
        wrapper.groupBy("type");

        List<Map<String, Object>> list = listMaps(wrapper);

        Map<String, Object> result = new HashMap<>();
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Map<String, Object> map : list) {
            Integer type = (Integer) map.get("type");
            BigDecimal amount = (BigDecimal) map.get("totalAmount");
            if (amount == null)
                amount = BigDecimal.ZERO;

            if (type == 1) { // IN
                income = income.add(amount);
            } else if (type == 2) { // OUT
                expense = expense.add(amount);
            }
        }

        result.put("income", income);
        result.put("expense", expense);
        result.put("profit", income.subtract(expense));
        return result;
    }
}
