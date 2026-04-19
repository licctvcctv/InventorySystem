<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { stockIn, getAllProducts, getWarehouses } from '../../api/inventory'
import { getSuppliers } from '../../api/base'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const router = useRouter()
const submitting = ref(false)
const products = ref<any[]>([])
const suppliers = ref<any[]>([])
const warehouses = ref<any[]>([])

const items = reactive<any[]>([
    { productId: null, productName: '', productAttr: '', unit: '', quantity: 1, unitPrice: 0, supplierId: null, warehouseId: null }
])

const addItem = () => items.push({ productId: null, productName: '', productAttr: '', unit: '', quantity: 1, unitPrice: 0, supplierId: null, warehouseId: null })
const removeItem = (i: number) => { if (items.length > 1) items.splice(i, 1) }

const onProductSelect = (item: any, productId: number) => {
    const p = products.value.find((x: any) => x.id === productId)
    if (p) {
        item.productName = p.name
        item.productAttr = p.productAttr || ''
        item.unit = p.unit || ''
        item.unitPrice = p.purchasePrice || p.price || 0
    }
}

const handleSubmit = async () => {
    submitting.value = true
    try {
        for (const item of items) {
            if (!item.productId) { ElMessage.warning('请选择商品'); submitting.value = false; return }
            await stockIn(item)
        }
        ElMessage.success('入库成功')
        router.push('/dashboard/inventory/stock-in-list')
    } finally { submitting.value = false }
}

onMounted(async () => {
    const [pRes, sRes, wRes] = await Promise.all([
        getAllProducts(), getSuppliers({ page: 1, size: 1000 }), getWarehouses()])
    products.value = pRes as any || []
    suppliers.value = (sRes as any)?.records || sRes || []
    warehouses.value = wRes as any || []
})
</script>

<template>
    <el-card>
        <template #header><span>新增入库单</span></template>
        <div class="items-header">
            <el-button type="primary" size="small" :icon="Plus" @click="addItem">添加商品</el-button>
        </div>
        <el-table :data="items" border style="width:100%;margin-top:10px">
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
            <el-table-column label="入库数量" width="110">
                <template #default="{ row }"><el-input-number v-model="row.quantity" :min="1" size="small" /></template>
            </el-table-column>
            <el-table-column label="成本单价" width="120">
                <template #default="{ row }"><el-input-number v-model="row.unitPrice" :min="0" :precision="2" size="small" /></template>
            </el-table-column>
            <el-table-column label="成本金额" width="110">
                <template #default="{ row }">¥{{ ((row.quantity||0)*(row.unitPrice||0)).toFixed(2) }}</template>
            </el-table-column>
            <el-table-column label="供应商" width="150">
                <template #default="{ row }">
                    <el-select v-model="row.supplierId" placeholder="供应商" filterable style="width:100%">
                        <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column label="仓库" width="140">
                <template #default="{ row }">
                    <el-select v-model="row.warehouseId" placeholder="仓库" filterable style="width:100%">
                        <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="70">
                <template #default="{ $index }">
                    <el-button link type="danger" :icon="Delete" @click="removeItem($index)" v-if="items.length > 1" />
                </template>
            </el-table-column>
        </el-table>
        <div class="form-actions">
            <el-button @click="router.back()">取消</el-button>
            <el-button type="primary" :loading="submitting" @click="handleSubmit">提交入库</el-button>
        </div>
    </el-card>
</template>

<style scoped>
.items-header { display: flex; justify-content: flex-end; }
.form-actions { margin-top: 30px; text-align: center; }
</style>
