<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { createOrder, updateOrder, getOrder, type Order, type OrderItem } from '../../api/order'
import { getAllProducts } from '../../api/inventory'
import { getCustomers } from '../../api/base'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const submitting = ref(false)
const isEdit = ref(false)
const products = ref<any[]>([])
const customers = ref<any[]>([])

const form = reactive<Order>({
    type: 'SALE',
    description: '',
    customerId: undefined,
    customerAddress: '',
    customerPaymentInfo: '',
    invoiceType: '',
    salesman: '',
    items: [{ productName: '', productAttr: '', unit: '', quantity: 1, price: 0, costPrice: 0, stockQuantity: 0 }]
})

const totalAmount = computed(() =>
    form.items.reduce((s, i) => s + (i.quantity || 0) * (i.price || 0), 0))
const totalCost = computed(() =>
    form.items.reduce((s, i) => s + (i.quantity || 0) * (i.costPrice || 0), 0))

const addItem = () => {
    form.items.push({ productName: '', productAttr: '', unit: '', quantity: 1, price: 0, costPrice: 0, stockQuantity: 0 })
}
const removeItem = (idx: number) => { if (form.items.length > 1) form.items.splice(idx, 1) }

const onProductSelect = (item: OrderItem, productId: number) => {
    const p = products.value.find((x: any) => x.id === productId)
    if (p) {
        item.productName = p.name
        item.productAttr = p.productAttr || ''
        item.unit = p.unit || ''
        item.price = p.salePrice || p.price || 0
        item.costPrice = p.purchasePrice || p.price || 0
        item.stockQuantity = p.stock || 0
        item.productId = p.id
    }
}

const onCustomerSelect = (customerId: number) => {
    const c = customers.value.find((x: any) => x.id === customerId)
    if (c) {
        form.customerAddress = c.address || ''
    }
}

const handleSubmit = async () => {
    if (!form.customerId) return ElMessage.warning('请选择客户')
    if (form.items.some(i => !i.productName)) return ElMessage.warning('请填写商品名称')
    submitting.value = true
    try {
        const dealAmt = totalAmount.value
        const debt = dealAmt // 初始欠款=成交金额
        const data = { ...form, dealAmount: dealAmt, totalCost: totalCost.value, orderDebt: debt }
        if (isEdit.value) await updateOrder(data as Order)
        else await createOrder(data as Order)
        ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
        router.push('/dashboard/sales/list')
    } finally { submitting.value = false }
}

onMounted(async () => {
    const [pRes, cRes] = await Promise.all([
        getAllProducts(), getCustomers({ page: 1, size: 1000 })])
    products.value = pRes as any || []
    customers.value = (cRes as any)?.records || cRes || []

    const id = route.query.id
    if (id) {
        isEdit.value = true
        const res = await getOrder(Number(id)) as any
        Object.assign(form, res)
    }
})
</script>

<template>
    <el-card>
        <template #header><span>{{ isEdit ? '编辑' : '新增' }}销售订单</span></template>
        <el-form label-width="110px" style="max-width:1200px">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-form-item label="客户">
                        <el-select v-model="form.customerId" placeholder="选择客户" filterable style="width:100%"
                            @change="onCustomerSelect">
                            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="客户地址">
                        <el-input v-model="form.customerAddress" />
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="客户付款信息">
                        <el-input v-model="form.customerPaymentInfo" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-form-item label="票据类型">
                        <el-select v-model="form.invoiceType" placeholder="选择票据类型" style="width:100%">
                            <el-option label="增值税专用发票" value="增值税专用发票" />
                            <el-option label="增值税普通发票" value="增值税普通发票" />
                            <el-option label="收据" value="收据" />
                            <el-option label="无" value="无" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="业务员">
                        <el-input v-model="form.salesman" />
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="折扣(%)">
                        <el-input-number v-model="(form as any).discount" :min="0" :max="100" :precision="1" style="width:100%" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-form-item label="备注">
                        <el-input v-model="form.description" />
                    </el-form-item>
                </el-col>
            </el-row>

            <div class="items-section">
                <div class="items-header">
                    <h4>商品明细</h4>
                    <el-button type="primary" size="small" :icon="Plus" @click="addItem">添加商品</el-button>
                </div>
                <el-table :data="form.items" border style="width:100%">
                    <el-table-column label="商品" width="180">
                        <template #default="{ row }">
                            <el-select v-model="row.productId" placeholder="选择商品" filterable
                                @change="(v: number) => onProductSelect(row, v)" style="width:100%">
                                <el-option v-for="p in products" :key="p.id" :label="p.name" :value="p.id" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="属性" width="120">
                        <template #default="{ row }"><el-input v-model="row.productAttr" /></template>
                    </el-table-column>
                    <el-table-column label="单位" width="80">
                        <template #default="{ row }"><el-input v-model="row.unit" /></template>
                    </el-table-column>
                    <el-table-column label="库存量" width="80">
                        <template #default="{ row }">{{ row.stockQuantity || 0 }}</template>
                    </el-table-column>
                    <el-table-column label="数量" width="100">
                        <template #default="{ row }"><el-input-number v-model="row.quantity" :min="1" size="small" /></template>
                    </el-table-column>
                    <el-table-column label="销售单价" width="120">
                        <template #default="{ row }"><el-input-number v-model="row.price" :min="0" :precision="2" size="small" /></template>
                    </el-table-column>
                    <el-table-column label="实销金额" width="110">
                        <template #default="{ row }">¥{{ ((row.quantity||0)*(row.price||0)).toFixed(2) }}</template>
                    </el-table-column>
                    <el-table-column label="操作" width="70">
                        <template #default="{ $index }">
                            <el-button link type="danger" :icon="Delete" @click="removeItem($index)" v-if="form.items.length > 1" />
                        </template>
                    </el-table-column>
                </el-table>
                <div class="total-bar">
                    成交金额: <span class="amount">¥{{ totalAmount.toFixed(2) }}</span>
                    &nbsp;&nbsp;本单欠款: <span class="amount">¥{{ totalAmount.toFixed(2) }}</span>
                </div>
            </div>
            <div class="form-actions">
                <el-button @click="router.back()">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="handleSubmit">{{ isEdit ? '保存修改' : '提交订单' }}</el-button>
            </div>
        </el-form>
    </el-card>
</template>

<style scoped>
.items-section { margin: 20px 0; padding: 20px; background: #f8f9fa; border-radius: 4px; }
.items-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.total-bar { text-align: right; margin-top: 15px; font-size: 16px; font-weight: 500; }
.amount { color: #f56c6c; }
.form-actions { margin-top: 30px; text-align: center; }
</style>
