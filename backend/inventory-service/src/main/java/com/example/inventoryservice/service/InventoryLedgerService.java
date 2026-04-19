package com.example.inventoryservice.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.inventoryservice.entity.InventoryLedger;
import com.example.inventoryservice.mapper.InventoryLedgerMapper;
import org.springframework.stereotype.Service;

@Service
public class InventoryLedgerService extends ServiceImpl<InventoryLedgerMapper, InventoryLedger> {
}
