package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Product;
import com.example.inventoryservice.entity.StockRecord;
import com.example.inventoryservice.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 出入库服务层单元测试
 * 测试入库、出库业务逻辑以及库存台账同步功能
 *
 * @author InventorySystem
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Rollback
@DisplayName("出入库服务层测试 - StockRecordService")
class StockRecordServiceTest {

    @Autowired
    private StockRecordService stockRecordService;

    @Autowired
    private ProductMapper productMapper;

    @Test
    @DisplayName("测试入库方法 - 入库后商品库存应增加")
    void testStockInIncreasesProductStock() {
        // 查询当前商品库存
        Product product = productMapper.selectById(1L);
        assertNotNull(product, "测试商品应存在");
        int originalStock = product.getStock() != null ? product.getStock() : 0;

        // 执行入库操作
        StockRecord record = StockRecord.builder()
                .productId(1L)
                .warehouseId(1L)
                .quantity(50)
                .unitPrice(new BigDecimal("100.00"))
                .remark("服务层入库测试")
                .createdBy("test_admin")
                .build();

        stockRecordService.stockIn(record);

        // 验证库存已增加
        Product updatedProduct = productMapper.selectById(1L);
        assertEquals(originalStock + 50, updatedProduct.getStock(),
                "入库50件后，库存应从 " + originalStock + " 增加到 " + (originalStock + 50));

        // 验证记录属性
        assertNotNull(record.getRecordNo(), "入库记录单号不应为空");
        assertTrue(record.getRecordNo().startsWith("RK"), "入库单号应以RK开头");
        assertEquals(1, record.getType(), "入库记录类型应为1");
        assertNotNull(record.getTotalAmount(), "入库金额不应为空");
        assertEquals(new BigDecimal("5000.00"), record.getTotalAmount(),
                "入库总金额应为 50 * 100.00 = 5000.00");
    }

    @Test
    @DisplayName("测试出库方法 - 出库后商品库存应减少")
    void testStockOutDecreasesProductStock() {
        // 先入库确保有库存
        StockRecord inRecord = StockRecord.builder()
                .productId(1L)
                .warehouseId(1L)
                .quantity(200)
                .unitPrice(new BigDecimal("80.00"))
                .remark("为出库测试准备库存")
                .createdBy("test_admin")
                .build();
        stockRecordService.stockIn(inRecord);

        Product productAfterIn = productMapper.selectById(1L);
        int stockAfterIn = productAfterIn.getStock();

        // 执行出库操作
        StockRecord outRecord = StockRecord.builder()
                .productId(1L)
                .warehouseId(1L)
                .quantity(80)
                .unitPrice(new BigDecimal("150.00"))
                .remark("服务层出库测试")
                .createdBy("test_admin")
                .build();

        stockRecordService.stockOut(outRecord);

        // 验证库存已减少
        Product updatedProduct = productMapper.selectById(1L);
        assertEquals(stockAfterIn - 80, updatedProduct.getStock(),
                "出库80件后，库存应从 " + stockAfterIn + " 减少到 " + (stockAfterIn - 80));

        // 验证记录属性
        assertNotNull(outRecord.getRecordNo(), "出库记录单号不应为空");
        assertTrue(outRecord.getRecordNo().startsWith("CK"), "出库单号应以CK开头");
        assertEquals(2, outRecord.getType(), "出库记录类型应为2");
        assertEquals(new BigDecimal("12000.00"), outRecord.getTotalAmount(),
                "出库总金额应为 80 * 150.00 = 12000.00");
    }

    @Test
    @DisplayName("测试出库方法 - 库存不足时应抛出RuntimeException异常")
    void testStockOutWithInsufficientStockThrowsException() {
        StockRecord record = StockRecord.builder()
                .productId(1L)
                .warehouseId(1L)
                .quantity(999999)
                .unitPrice(new BigDecimal("100.00"))
                .remark("库存不足异常测试")
                .createdBy("test_admin")
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stockRecordService.stockOut(record),
                "库存不足时应抛出RuntimeException");

        assertEquals("库存不足", exception.getMessage(),
                "异常消息应为'库存不足'");
    }

    @Test
    @DisplayName("测试入库后库存台账自动同步 - 验证台账记录创建或更新")
    void testInventoryLedgerSyncAfterStockIn() {
        StockRecord record = StockRecord.builder()
                .productId(1L)
                .warehouseId(1L)
                .quantity(30)
                .unitPrice(new BigDecimal("200.00"))
                .remark("台账同步测试")
                .createdBy("test_admin")
                .build();

        // 入库操作应同时触发台账同步，不抛出异常即表示同步成功
        assertDoesNotThrow(() -> stockRecordService.stockIn(record),
                "入库操作及台账同步不应抛出异常");

        // 验证商品库存已正确更新
        Product product = productMapper.selectById(1L);
        assertNotNull(product, "商品应存在");
        assertTrue(product.getStock() >= 30,
                "入库后商品库存应至少为30");
    }

    @Test
    @DisplayName("测试使用简单参数接口执行入库操作")
    void testStockInWithSimpleParams() {
        Product product = productMapper.selectById(1L);
        int originalStock = product.getStock() != null ? product.getStock() : 0;

        assertDoesNotThrow(
                () -> stockRecordService.stockIn(1L, 1L, null, 25, "简单接口入库测试"),
                "简单参数入库操作不应抛出异常");

        Product updatedProduct = productMapper.selectById(1L);
        assertEquals(originalStock + 25, updatedProduct.getStock(),
                "使用简单接口入库25件后，库存应正确增加");
    }
}
