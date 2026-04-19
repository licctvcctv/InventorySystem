package com.example.baseservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.baseservice.entity.FinanceRecord;
import java.util.Map;

public interface FinanceService extends IService<FinanceRecord> {
    Map<String, Object> getReport(String startDate, String endDate);
}
