<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOrder, getCustomerDebt, type Order } from '../../api/order'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const order = ref<Order | null>(null)
const debtVisible = ref(false)
const debtList = ref<any[]>([])

const loadDebt = async () => {
    const res = await getCustomerDebt({ customerId: order.value?.customerId }) as any
    debtList.value = res || []
    debtVisible.value = true
}

onMounted(async () => {
    const id = route.query.id
    if (!id) { ElMessage.error('缺少订单ID'); return }
    try { order.value = await getOrder(Number(id)) as any }
    catch { ElMessage.error('获取详情失败') }
})
</script>

<template>
    <el-card>
        <div class="header-actions" style="margin-bottom:20px">
            <el-button type="primary" @click="router.push('/dashboard/sales/create')">新增销售订单</el-button>
            <el-button @click="router.push('/dashboard/sales/list')">历史单据</el-button>
            <el-button type="warning" @click="loadDebt">客户欠款</el-button>
        </div>
        <div v-if="order">
            <el-descriptions title="销售订单详情" :column="3" border>
                <el-descriptions-item label="ID">{{ order.id }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ order.createdAt }}</el-descriptions-item>
                <el-descriptions-item label="销售单号">{{ order.orderNo }}</el-descriptions-item>
                <el-descriptions-item label="客户">{{ order.customerName || order.customerId }}</el-descriptions-item>
                <el-descriptions-item label="客户地址">{{ order.customerAddress }}</el-descriptions-item>
                <el-descriptions-item label="客户付款信息">{{ order.customerPaymentInfo }}</el-descriptions-item>
                <el-descriptions-item label="票据类型">{{ order.invoiceType }}</el-descriptions-item>
                <el-descriptions-item label="业务员">{{ order.salesman }}</el-descriptions-item>
                <el-descriptions-item label="成交金额">¥{{ order.dealAmount || order.totalAmount || 0 }}</el-descriptions-item>
                <el-descriptions-item label="折扣">{{ order.discount || 0 }}%</el-descriptions-item>
                <el-descriptions-item label="本单欠款">¥{{ order.orderDebt || 0 }}</el-descriptions-item>
                <el-descriptions-item label="备注">{{ order.description }}</el-descriptions-item>
            </el-descriptions>
            <h4 style="margin:20px 0 10px">商品明细</h4>
            <el-table :data="order.items" border style="width:100%">
                <el-table-column prop="id" label="ID" width="60" />
                <el-table-column prop="productName" label="商品名称" />
                <el-table-column prop="productAttr" label="属性（配置）" width="130" />
                <el-table-column prop="unit" label="销售单位" width="80" />
                <el-table-column prop="stockQuantity" label="库存量" width="80" />
                <el-table-column prop="quantity" label="数量" width="70" />
                <el-table-column label="销售单价" width="100">
                    <template #default="{ row }">¥{{ row.price || 0 }}</template>
                </el-table-column>
                <el-table-column label="实销金额" width="110">
                    <template #default="{ row }">¥{{ row.amount || ((row.quantity||0)*(row.price||0)).toFixed(2) }}</template>
                </el-table-column>
                <el-table-column label="成交金额" width="110">
                    <template #default="{ row }">¥{{ row.costAmount || row.amount || 0 }}</template>
                </el-table-column>
            </el-table>
        </div>
        <el-empty v-else description="加载中..." />

        <el-dialog v-model="debtVisible" title="客户欠款查询" width="500px">
            <el-table :data="debtList" border>
                <el-table-column prop="customer_id" label="客户ID" />
                <el-table-column prop="totalDebt" label="欠款总额">
                    <template #default="{ row }">¥{{ row.totalDebt || 0 }}</template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.header-actions { display: flex; gap: 10px; }
</style>
