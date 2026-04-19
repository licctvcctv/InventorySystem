<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getOrder, createReceipt, getReceipts, type Order, type PaymentRecord } from '../../api/order'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const order = ref<Order | null>(null)
const receipts = ref<PaymentRecord[]>([])
const dialogVisible = ref(false)

const form = reactive<PaymentRecord>({
    paymentDate: new Date().toISOString().slice(0, 10),
    paymentNo: '',
    sourceOrderNo: '',
    sourceOrderId: undefined,
    settlementMethod: '银行转账',
    amountDue: 0,
    amountPaid: 0,
    paymentPerson: '',
    payer: '',
    isFullyPaid: false,
    remark: '',
    type: 'RECEIPT'
})

const generateNo = () => 'REC' + Date.now().toString().slice(-10)

const loadOrder = async () => {
    const id = route.query.orderId
    if (!id) return
    try {
        order.value = await getOrder(Number(id)) as any
        if (order.value) {
            form.sourceOrderNo = order.value.orderNo || ''
            form.sourceOrderId = order.value.id
            form.amountDue = order.value.orderDebt || order.value.dealAmount || order.value.totalAmount || 0
            form.payer = order.value.customerName || `客户ID:${order.value.customerId}`
        }
    } catch { ElMessage.error('获取订单失败') }
}

const loadReceipts = async () => {
    try {
        const res = await getReceipts({ sourceOrderId: route.query.orderId, type: 'RECEIPT' }) as any
        receipts.value = res?.records || res || []
    } catch { /* ignore */ }
}

const handleAdd = () => {
    form.paymentNo = generateNo()
    form.amountPaid = form.amountDue
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if (form.amountPaid <= 0) return ElMessage.warning('请输入收款金额')
    form.isFullyPaid = form.amountPaid >= (form.amountDue || 0)
    await createReceipt(form)
    ElMessage.success('收款成功')
    dialogVisible.value = false
    loadReceipts()
}

onMounted(() => { loadOrder(); loadReceipts() })
</script>

<template>
    <el-card>
        <template #header>
            <div style="display:flex;justify-content:space-between;align-items:center">
                <span>收款管理</span>
                <div>
                    <el-button type="primary" @click="handleAdd">新增收款</el-button>
                    <el-button @click="router.push('/dashboard/sales/list')">返回历史单据</el-button>
                </div>
            </div>
        </template>

        <div v-if="order" style="margin-bottom:20px">
            <el-descriptions title="关联销售订单" :column="3" border size="small">
                <el-descriptions-item label="销售单号">{{ order.orderNo }}</el-descriptions-item>
                <el-descriptions-item label="客户">{{ order.customerName || order.customerId }}</el-descriptions-item>
                <el-descriptions-item label="应收金额">¥{{ order.orderDebt || order.dealAmount || order.totalAmount || 0 }}</el-descriptions-item>
            </el-descriptions>
        </div>

        <el-table :data="receipts" border stripe>
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="paymentDate" label="收款日期" width="120" />
            <el-table-column prop="paymentNo" label="收款单编号" width="180" />
            <el-table-column prop="sourceOrderNo" label="来源单据编号" width="180" />
            <el-table-column prop="settlementMethod" label="结算方式" width="120" />
            <el-table-column label="应收款金额" width="120">
                <template #default="{ row }">¥{{ row.amountDue || 0 }}</template>
            </el-table-column>
            <el-table-column label="实际收金额" width="120">
                <template #default="{ row }">¥{{ row.amountPaid || 0 }}</template>
            </el-table-column>
            <el-table-column prop="paymentPerson" label="收款处理人员" width="120" />
            <el-table-column prop="payer" label="收款来源" width="140" />
            <el-table-column label="是否收款完全" width="120">
                <template #default="{ row }">
                    <el-tag :type="row.isFullyPaid ? 'success' : 'warning'">{{ row.isFullyPaid ? '是' : '否' }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
        </el-table>

        <el-dialog v-model="dialogVisible" title="新增收款" width="600px">
            <el-form :model="form" label-width="120px">
                <el-form-item label="收款日期"><el-date-picker v-model="form.paymentDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
                <el-form-item label="收款单编号"><el-input v-model="form.paymentNo" /></el-form-item>
                <el-form-item label="来源单据编号"><el-input v-model="form.sourceOrderNo" disabled /></el-form-item>
                <el-form-item label="结算方式">
                    <el-select v-model="form.settlementMethod" style="width:100%">
                        <el-option label="银行转账" value="银行转账" />
                        <el-option label="现金" value="现金" />
                        <el-option label="支票" value="支票" />
                        <el-option label="微信" value="微信" />
                        <el-option label="支付宝" value="支付宝" />
                    </el-select>
                </el-form-item>
                <el-form-item label="应收款金额"><el-input-number v-model="form.amountDue" :precision="2" disabled style="width:100%" /></el-form-item>
                <el-form-item label="实际收金额"><el-input-number v-model="form.amountPaid" :precision="2" :min="0" style="width:100%" /></el-form-item>
                <el-form-item label="收款处理人员"><el-input v-model="form.paymentPerson" /></el-form-item>
                <el-form-item label="收款来源"><el-input v-model="form.payer" /></el-form-item>
                <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确认收款</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>
