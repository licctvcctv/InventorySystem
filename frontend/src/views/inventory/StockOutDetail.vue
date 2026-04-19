<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getStockRecord } from '../../api/inventory'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const record = ref<any>(null)

onMounted(async () => {
    const id = route.query.id
    if (!id) { ElMessage.error('缺少ID'); return }
    try { record.value = await getStockRecord(Number(id)) }
    catch { ElMessage.error('获取详情失败') }
})
</script>

<template>
    <el-card>
        <div class="header-actions" style="margin-bottom:20px">
            <el-button type="primary" @click="router.push('/dashboard/inventory/stock-out-create')">新增出库单</el-button>
            <el-button @click="router.push('/dashboard/inventory/stock-out-list')">历史单据</el-button>
        </div>
        <div v-if="record">
            <el-descriptions title="出库单详情" :column="3" border>
                <el-descriptions-item label="ID">{{ record.id }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ record.createdAt }}</el-descriptions-item>
                <el-descriptions-item label="出库单号">{{ record.recordNo }}</el-descriptions-item>
                <el-descriptions-item label="商品名称">{{ record.productName }}</el-descriptions-item>
                <el-descriptions-item label="属性（配置）">{{ record.productAttr }}</el-descriptions-item>
                <el-descriptions-item label="库存单位">{{ record.unit }}</el-descriptions-item>
                <el-descriptions-item label="出库数量">{{ record.quantity }}</el-descriptions-item>
                <el-descriptions-item label="销售单价">¥{{ record.unitPrice || 0 }}</el-descriptions-item>
                <el-descriptions-item label="销售金额">¥{{ record.totalAmount || 0 }}</el-descriptions-item>
                <el-descriptions-item label="供应商">{{ record.supplierId }}</el-descriptions-item>
                <el-descriptions-item label="仓库">{{ record.warehouseId }}</el-descriptions-item>
                <el-descriptions-item label="库存量">{{ record.stockQuantity }}</el-descriptions-item>
            </el-descriptions>
        </div>
        <el-empty v-else description="加载中..." />
    </el-card>
</template>

<style scoped>
.header-actions { display: flex; gap: 10px; }
</style>
