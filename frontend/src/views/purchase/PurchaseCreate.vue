<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { createOrder, updateOrder, getOrder, type Order, type OrderItem } from '../../api/order'
import { getAllProducts } from '../../api/inventory'
import { getSuppliers } from '../../api/base'
import { getWarehouses } from '../../api/inventory'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const submitting = ref(false)
const isEdit = ref(false)
const products = ref<any[]>([])
const suppliers = ref<any[]>([])
const warehouses = ref<any[]>([])

const form = reactive<Order>({
    type: 'PURCHASE',
    description: '',
    supplierId: undefined,
    warehouseId: undefined,
    invoiceType: '',
    salesman: '',
    items: [{ productName: '', productAttr: '', unit: '', quantity: 1, price: 0, costPrice: 0 }]
})

// 采购单：进货金额 = 成本单价 × 数量（price 始终跟随 costPrice）
const totalAmount = computed(() =>
    form.items.reduce((s, i) => s + (i.quantity || 0) * (i.costPrice || 0), 0))
const totalCost = computed(() =>
    form.items.reduce((s, i) => s + (i.quantity || 0) * (i.costPrice || 0), 0))

const addItem = () => {
    form.items.push({ productName: '', productAttr: '', unit: '', quantity: 1, price: 0, costPrice: 0 })
}
const removeItem = (idx: number) => { if (form.items.length > 1) form.items.splice(idx, 1) }

/** 获取商品的可选属性列表 */
const getAttrOptions = (item: OrderItem): string[] => {
    const p = products.value.find((x: any) => x.id === item.productId)
    if (p && p.productAttr) {
        return p.productAttr.split(',').map((s: string) => s.trim()).filter((s: string) => s)
    }
    return []
}

const onProductSelect = (item: OrderItem, productId: number) => {
    const p = products.value.find((x: any) => x.id === productId)
    if (p) {
        item.productName = p.name
        const attrs = p.productAttr ? p.productAttr.split(',').map((s: string) => s.trim()).filter((s: string) => s) : []
        item.productAttr = attrs.length > 0 ? attrs[0] : ''
        item.unit = p.unit || ''
        item.price = p.purchasePrice || p.price || 0
        item.costPrice = p.purchasePrice || p.price || 0
        item.productId = p.id
    }
}

const handleSubmit = async () => {
    if (!form.supplierId) return ElMessage.warning('请选择供应商')
    if (form.items.some(i => !i.productName)) return ElMessage.warning('请填写商品名称')
    submitting.value = true
    try {
        // 采购单：price 同步为 costPrice，保证后端计算一致
        form.items.forEach(i => { i.price = i.costPrice })
        const data = { ...form, totalCost: totalCost.value, dealAmount: totalAmount.value }
        if (isEdit.value) await updateOrder(data as Order)
        else await createOrder(data as Order)
        ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
        router.push('/dashboard/purchase/list')
    } finally { submitting.value = false }
}

onMounted(async () => {
    const [pRes, sRes, wRes] = await Promise.all([
        getAllProducts(), getSuppliers({ page: 1, size: 1000 }), getWarehouses()])
    products.value = pRes as any || []
    suppliers.value = (sRes as any)?.records || sRes || []
    warehouses.value = wRes as any || []

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
        <template #header><span>{{ isEdit ? '编辑' : '新增' }}采购订单</span></template>
        <el-form label-width="100px" style="max-width: 1200px">
            <el-row :gutter="20">
                <el-col :span="8">
                    <el-form-item label="供应商">
                        <el-select v-model="form.supplierId" placeholder="选择供应商" filterable style="width:100%">
                            <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="仓库">
                        <el-select v-model="form.warehouseId" placeholder="选择仓库" filterable style="width:100%">
                            <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
                        </el-select>
                    </el-form-item>
                </el-col>
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
            </el-row>
            <el-row :gutter="20">
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
                <el-col :span="8">
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
                    <el-table-column label="商品" width="200">
                        <template #default="{ row }">
                            <el-select v-model="row.productId" placeholder="选择商品" filterable
                                @change="(v: number) => onProductSelect(row, v)" style="width:100%">
                                <el-option v-for="p in products" :key="p.id" :label="p.name" :value="p.id" />
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column label="属性（配置）" width="160">
                        <template #default="{ row }">
                            <el-select v-if="getAttrOptions(row).length > 0" v-model="row.productAttr"
                                placeholder="选择配置" style="width:100%">
                                <el-option v-for="attr in getAttrOptions(row)" :key="attr" :label="attr" :value="attr" />
                            </el-select>
                            <el-input v-else v-model="row.productAttr" placeholder="无可选属性" />
                        </template>
                    </el-table-column>
                    <el-table-column label="采购单位" width="100">
                        <template #default="{ row }"><el-input v-model="row.unit" /></template>
                    </el-table-column>
                    <el-table-column label="数量" width="110">
                        <template #default="{ row }"><el-input-number v-model="row.quantity" :min="1" size="small" /></template>
                    </el-table-column>
                    <el-table-column label="成本单价" width="130">
                        <template #default="{ row }"><el-input-number v-model="row.costPrice" :min="0" :precision="2" size="small" /></template>
                    </el-table-column>
                    <el-table-column label="进货金额" width="120">
                        <template #default="{ row }">¥{{ ((row.quantity || 0) * (row.costPrice || 0)).toFixed(2) }}</template>
                    </el-table-column>
                    <el-table-column label="操作" width="80">
                        <template #default="{ $index }">
                            <el-button link type="danger" :icon="Delete" @click="removeItem($index)" v-if="form.items.length > 1" />
                        </template>
                    </el-table-column>
                </el-table>
                <div class="total-bar">
                    进货总额: <span class="amount">¥{{ totalAmount.toFixed(2) }}</span>
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
