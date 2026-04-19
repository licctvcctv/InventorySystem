<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOrder, type Order } from '../../api/order'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Printer } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const order = ref<Order | null>(null)
const invoiceDate = new Date().toISOString().slice(0, 10)

const handlePrint = () => {
    if (!order.value) return
    const o = order.value
    const items = o.items || []
    const rows = items.map((item, idx) =>
        `<tr><td>${idx + 1}</td><td>${item.productName}</td><td>${item.quantity}</td>
        <td>¥${item.price || 0}</td><td>¥${(item.quantity * (item.price || 0)).toFixed(2)}</td>
        <td>¥${item.amount || 0}</td><td></td><td></td></tr>`).join('')
    const html = `<html><head><title>销售发票</title>
        <style>body{font-family:sans-serif;padding:40px}table{border-collapse:collapse;width:100%}
        th,td{border:1px solid #333;padding:8px;text-align:center}th{background:#f0f0f0}
        h1{text-align:center}
        .info{display:flex;justify-content:space-between;margin:20px 0;flex-wrap:wrap}
        .info span{margin:5px 15px}</style></head>
        <body><h1>销售发票</h1>
        <div class="info">
            <span>开票日期: ${invoiceDate}</span>
            <span>票据类型: ${o.invoiceType || '无'}</span>
            <span>销售单号: ${o.orderNo}</span>
            <span>客户: ${o.customerName || o.customerId || ''}</span>
            <span>业务员: ${o.salesman || ''}</span>
        </div>
        <table><tr><th>序号</th><th>商品名称</th><th>销售数量</th><th>销售单价</th>
        <th>应销售金额</th><th>实际销售金额</th><th>折扣</th><th>欠款</th></tr>
        ${rows}
        <tr><td colspan="4" style="text-align:right;font-weight:bold">合计</td>
        <td>¥${o.totalAmount || 0}</td>
        <td>¥${o.dealAmount || o.totalAmount || 0}</td><td>${o.discount || 0}%</td>
        <td>¥${o.orderDebt || 0}</td></tr></table>
        <script>window.print();<\/script></body></html>`
    const win = window.open('', '_blank')
    if (win) { win.document.write(html); win.document.close() }
}

onMounted(async () => {
    const id = route.query.orderId
    if (!id) { ElMessage.error('缺少订单ID'); return }
    try { order.value = await getOrder(Number(id)) as any }
    catch { ElMessage.error('获取订单失败') }
})
</script>

<template>
    <el-card>
        <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
                <span>销售发票</span>
                <div>
                    <el-button type="primary" :icon="Printer" @click="handlePrint">打印</el-button>
                    <el-button @click="router.push('/dashboard/sales/list')">返回历史单据</el-button>
                </div>
            </div>
        </template>
        <div v-if="order">
            <el-descriptions :column="3" border>
                <el-descriptions-item label="ID">{{ order.id }}</el-descriptions-item>
                <el-descriptions-item label="开票日期">{{ invoiceDate }}</el-descriptions-item>
                <el-descriptions-item label="票据类型">{{ order.invoiceType }}</el-descriptions-item>
                <el-descriptions-item label="销售单号">{{ order.orderNo }}</el-descriptions-item>
                <el-descriptions-item label="客户">{{ order.customerName || order.customerId }}</el-descriptions-item>
                <el-descriptions-item label="业务员">{{ order.salesman }}</el-descriptions-item>
            </el-descriptions>
            <h4 style="margin:20px 0 10px">商品明细</h4>
            <el-table :data="order.items" border>
                <el-table-column type="index" label="序号" width="60" />
                <el-table-column prop="productName" label="商品名称" />
                <el-table-column prop="quantity" label="销售数量" width="100" />
                <el-table-column label="销售单价" width="110">
                    <template #default="{ row }">¥{{ row.price || 0 }}</template>
                </el-table-column>
                <el-table-column label="应销售金额" width="120">
                    <template #default="{ row }">¥{{ ((row.quantity || 0) * (row.price || 0)).toFixed(2) }}</template>
                </el-table-column>
                <el-table-column label="实际销售金额" width="130">
                    <template #default="{ row }">¥{{ row.amount || 0 }}</template>
                </el-table-column>
                <el-table-column label="折扣" width="80">
                    <template #default>{{ order!.discount || 0 }}%</template>
                </el-table-column>
                <el-table-column label="欠款" width="110">
                    <template #default>¥{{ order!.orderDebt || 0 }}</template>
                </el-table-column>
            </el-table>
            <div style="text-align:right;margin-top:15px;font-size:16px">
                合计金额: <span style="color:#f56c6c;font-weight:bold">¥{{ order.dealAmount || order.totalAmount || 0 }}</span>
            </div>
        </div>
        <el-empty v-else description="加载中..." />
    </el-card>
</template>
