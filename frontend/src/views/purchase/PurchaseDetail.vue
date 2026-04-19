<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOrder, type Order } from '../../api/order'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const order = ref<Order | null>(null)

onMounted(async () => {
    const id = route.query.id
    if (!id) { ElMessage.error('缺少订单ID'); return }
    try {
        order.value = await getOrder(Number(id)) as any
    } catch { ElMessage.error('获取详情失败') }
})
</script>

<template>
    <el-card>
        <div class="header-actions" style="margin-bottom:20px">
            <el-button type="primary" @click="router.push('/dashboard/purchase/create')">新增采购订单</el-button>
            <el-button @click="router.push('/dashboard/purchase/list')">历史单据</el-button>
        </div>
        <div v-if="order">
            <el-descriptions title="采购订单详情" :column="3" border>
                <el-descriptions-item label="ID">{{ order.id }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ order.createdAt }}</el-descriptions-item>
                <el-descriptions-item label="采购单号">{{ order.orderNo }}</el-descriptions-item>
                <el-descriptions-item label="供应商">{{ order.supplierName || order.supplierId }}</el-descriptions-item>
                <el-descriptions-item label="仓库">{{ order.warehouseName || order.warehouseId }}</el-descriptions-item>
                <el-descriptions-item label="票据类型">{{ order.invoiceType }}</el-descriptions-item>
                <el-descriptions-item label="业务员">{{ order.salesman }}</el-descriptions-item>
                <el-descriptions-item label="折扣">{{ order.discount || 0 }}%</el-descriptions-item>
                <el-descriptions-item label="本单欠款">¥{{ order.orderDebt || 0 }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="3">{{ order.description }}</el-descriptions-item>
            </el-descriptions>
            <h4 style="margin:20px 0 10px">商品明细</h4>
            <el-table :data="order.items" border style="width:100%">
                <el-table-column prop="id" label="ID" width="60" />
                <el-table-column prop="productName" label="商品名称" />
                <el-table-column prop="productAttr" label="属性（配置）" width="140" />
                <el-table-column prop="unit" label="采购单位" width="90" />
                <el-table-column prop="quantity" label="数量" width="80" />
                <el-table-column label="成本单价" width="100">
                    <template #default="{ row }">¥{{ row.costPrice || 0 }}</template>
                </el-table-column>
                <el-table-column label="进货金额" width="110">
                    <template #default="{ row }">¥{{ row.amount || 0 }}</template>
                </el-table-column>
                <el-table-column label="成本金额" width="110">
                    <template #default="{ row }">¥{{ row.costAmount || 0 }}</template>
                </el-table-column>
            </el-table>
        </div>
        <el-empty v-else description="加载中..." />
    </el-card>
</template>

<style scoped>
.header-actions { display: flex; gap: 10px; }
</style>
