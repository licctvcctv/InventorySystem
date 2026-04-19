<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getInventoryLedger, createInventoryLedger, updateInventoryLedger, deleteInventoryLedger, batchDeleteInventoryLedger } from '../../api/inventory'
import { getSuppliers } from '../../api/base'
import { getWarehouses } from '../../api/inventory'
import { Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import BatchToolbar from '../../components/BatchToolbar.vue'

const loading = ref(false)
const tableData = ref<any[]>([])
const total = ref(0)
const queryParams = reactive({ page: 1, size: 10, keyword: '' })
const suppliers = ref<any[]>([])
const warehouses = ref<any[]>([])
const selectedIds = ref<number[]>([])

const columns = [
    { label: 'ID', prop: 'id' }, { label: '创建时间', prop: 'createdAt' },
    { label: '商品编码', prop: 'productCode' }, { label: '商品名称', prop: 'productName' },
    { label: '类型', prop: 'productType' }, { label: '属性', prop: 'productAttr' },
    { label: '单位', prop: 'unit' }, { label: '最终库存量', prop: 'finalStock' },
    { label: '销售单价', prop: 'salePrice' }, { label: '销售金额', prop: 'saleAmount' }
]
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive<any>({
    id: null, productId: null, productCode: '', productName: '', productType: '',
    productAttr: '', unit: '', finalStock: 0, salePrice: 0, saleAmount: 0,
    supplierId: null, warehouseId: null
})

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getInventoryLedger(queryParams) as any
        tableData.value = res.records
        total.value = res.total
    } finally { loading.value = false }
}

const handleAdd = () => {
    dialogTitle.value = '新增库存'
    Object.assign(form, { id: null, productId: null, productCode: '', productName: '', productType: '',
        productAttr: '', unit: '', finalStock: 0, salePrice: 0, saleAmount: 0, supplierId: null, warehouseId: null })
    dialogVisible.value = true
}

const handleEdit = (row: any) => {
    dialogTitle.value = '编辑库存'
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if (!form.productName) return ElMessage.warning('请输入商品名称')
    form.saleAmount = (form.finalStock || 0) * (form.salePrice || 0)
    if (form.id) await updateInventoryLedger(form)
    else await createInventoryLedger(form)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchData()
}

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' }).then(async () => {
        await deleteInventoryLedger(row.id)
        ElMessage.success('删除成功')
        fetchData()
    })
}

const handleSelectionChange = (rows: any[]) => { selectedIds.value = rows.map(r => r.id).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteInventoryLedger(ids) } catch { for (const id of ids) { await deleteInventoryLedger(id) } }
    ElMessage.success('批量删除成功'); fetchData()
}

onMounted(async () => {
    fetchData()
    const [sRes, wRes] = await Promise.all([
        getSuppliers({ page: 1, size: 1000 }), getWarehouses()])
    suppliers.value = (sRes as any)?.records || sRes || []
    warehouses.value = wRes as any || []
})
</script>

<template>
    <el-card>
        <div class="header-actions">
            <div class="left">
                <el-input v-model="queryParams.keyword" placeholder="商品名称/编码" style="width:200px" clearable @clear="fetchData" />
                <el-button type="primary" :icon="Search" @click="fetchData" style="margin-left:10px">搜索</el-button>
            </div>
            <div class="right">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="商品库存" @batch-delete="handleBatchDelete" />
                <el-button type="primary" :icon="Plus" @click="handleAdd" style="margin-left:10px">新增库存</el-button>
            </div>
        </div>
        <el-table :data="tableData" v-loading="loading" style="margin-top:20px" border stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="createdAt" label="创建时间" width="170" />
            <el-table-column prop="productCode" label="商品编码" width="120" />
            <el-table-column prop="productName" label="商品名称" />
            <el-table-column prop="productType" label="商品类型" width="100" />
            <el-table-column prop="productAttr" label="属性（配置）" width="120" />
            <el-table-column prop="unit" label="库存单位" width="80" />
            <el-table-column prop="finalStock" label="最终库存量" width="100">
                <template #default="{ row }">
                    <el-tag :type="(row.finalStock || 0) < 10 ? 'danger' : 'success'">{{ row.finalStock }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="salePrice" label="销售单价" width="100">
                <template #default="{ row }">¥{{ row.salePrice || 0 }}</template>
            </el-table-column>
            <el-table-column prop="saleAmount" label="销售金额" width="100">
                <template #default="{ row }">¥{{ row.saleAmount || 0 }}</template>
            </el-table-column>
            <el-table-column prop="supplierId" label="供应商" width="80" />
            <el-table-column prop="warehouseId" label="仓库" width="80" />
            <el-table-column label="操作" width="150" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">修改</el-button>
                    <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination v-if="total > 0" style="margin-top:20px;display:flex;justify-content:flex-end"
            v-model:current-page="queryParams.page" v-model:page-size="queryParams.size"
            :total="total" layout="total, prev, pager, next" @current-change="fetchData" />

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
            <el-form :model="form" label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="商品编码"><el-input v-model="form.productCode" /></el-form-item></el-col>
                    <el-col :span="12"><el-form-item label="商品名称"><el-input v-model="form.productName" /></el-form-item></el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="商品类型"><el-input v-model="form.productType" /></el-form-item></el-col>
                    <el-col :span="12"><el-form-item label="属性（配置）"><el-input v-model="form.productAttr" /></el-form-item></el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="库存单位"><el-input v-model="form.unit" /></el-form-item></el-col>
                    <el-col :span="12"><el-form-item label="最终库存量"><el-input-number v-model="form.finalStock" :min="0" /></el-form-item></el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12"><el-form-item label="销售单价"><el-input-number v-model="form.salePrice" :min="0" :precision="2" /></el-form-item></el-col>
                    <el-col :span="12">
                        <el-form-item label="供应商">
                            <el-select v-model="form.supplierId" placeholder="选择供应商" filterable style="width:100%">
                                <el-option v-for="s in suppliers" :key="s.id" :label="s.name" :value="s.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="仓库">
                            <el-select v-model="form.warehouseId" placeholder="选择仓库" filterable style="width:100%">
                                <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.header-actions { display: flex; justify-content: space-between; }
</style>
