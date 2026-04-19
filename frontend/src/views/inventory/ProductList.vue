<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getProducts, createProduct, updateProduct, deleteProduct, batchDeleteProducts, stockIn, stockOut } from '../../api/inventory'
import { Plus, Search, Edit, Delete, Box } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import BatchToolbar from '../../components/BatchToolbar.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ page: 1, size: 10, keyword: '' })
const selectedIds = ref<number[]>([])

const columns = [
    { label: '商品名称', prop: 'name' }, { label: 'SKU', prop: 'sku' },
    { label: '单价', prop: 'price' }, { label: '库存', prop: 'stock' }
]

// 商品表单
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = reactive({ id: null, name: '', sku: '', price: 0, description: '' })

// 库存操作表单
const stockDialogVisible = ref(false)
const stockType = ref('IN') // IN or OUT
const stockForm = reactive({ productId: 0, quantity: 1, remark: '' })

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getProducts(queryParams)
        tableData.value = res.records
        total.value = res.total
    } finally {
        loading.value = false
    }
}

// ... CRUD Handlers ...
const handleAdd = () => {
    dialogTitle.value = '新增商品'
    Object.assign(form, { id: null, name: '', sku: '', price: 0, description: '' })
    dialogVisible.value = true
}

const handleEdit = (row: any) => {
    dialogTitle.value = '编辑商品'
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleSubmit = async () => {
    if(!form.name) return ElMessage.warning('请输入名称')
    if(form.id) await updateProduct(form)
    else await createProduct(form)
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchData()
}

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确定删除商品吗?', '提示', { type: 'warning' })
        .then(async () => {
            await deleteProduct(row.id)
            ElMessage.success('删除成功')
            fetchData()
        })
}

// 库存操作
const handleStock = (row: any, type: 'IN' | 'OUT') => {
    stockType.value = type
    stockForm.productId = row.id
    stockForm.quantity = 1
    stockForm.remark = ''
    stockDialogVisible.value = true
}

const submitStock = async () => {
    if (stockForm.quantity <= 0) return ElMessage.warning('数量必须大于0')
    try {
        if (stockType.value === 'IN') await stockIn(stockForm)
        else await stockOut(stockForm)
        ElMessage.success(stockType.value === 'IN' ? '入库成功' : '出库成功')
        stockDialogVisible.value = false
        fetchData()
    } catch(e) {
        // error handled
    }
}

onMounted(fetchData)

const handleSelectionChange = (rows: any[]) => { selectedIds.value = rows.map(r => r.id).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteProducts(ids) } catch { for (const id of ids) { await deleteProduct(id) } }
    ElMessage.success('批量删除成功'); fetchData()
}
</script>

<template>
    <el-card>
        <div class="header-actions">
            <div class="left">
                <el-input v-model="queryParams.keyword" placeholder="商品名称/SKU" style="width: 200px" clearable @clear="fetchData" />
                <el-button type="primary" :icon="Search" @click="fetchData" style="margin-left: 10px">搜索</el-button>
            </div>
            <div class="right">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="商品库存" @batch-delete="handleBatchDelete" />
                <el-button type="primary" :icon="Plus" @click="handleAdd" style="margin-left:10px">新增商品</el-button>
            </div>
        </div>

        <el-table :data="tableData" v-loading="loading" style="margin-top: 20px" border stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="name" label="商品名称" />
            <el-table-column prop="sku" label="SKU" width="120" />
            <el-table-column prop="price" label="单价" width="100">
                <template #default="{ row }">¥{{ row.price }}</template>
            </el-table-column>
            <el-table-column prop="stock" label="当前库存" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.stock < 10 ? 'danger' : 'success'">{{ row.stock }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="库存操作" width="200">
                <template #default="{ row }">
                    <el-button size="small" type="success" @click="handleStock(row, 'IN')">入库</el-button>
                    <el-button size="small" type="warning" @click="handleStock(row, 'OUT')">出库</el-button>
                </template>
            </el-table-column>
            <el-table-column label="管理" width="150" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
                    <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            v-if="total > 0"
            style="margin-top: 20px; display: flex; justify-content: flex-end"
            v-model:current-page="queryParams.page"
            v-model:page-size="queryParams.size"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="fetchData"
        />

        <!-- 商品表单 -->
        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
            <el-form :model="form" label-width="80px">
                <el-form-item label="商品名称">
                    <el-input v-model="form.name" />
                </el-form-item>
                <el-form-item label="SKU">
                    <el-input v-model="form.sku" />
                </el-form-item>
                <el-form-item label="单价">
                    <el-input-number v-model="form.price" :min="0" :precision="2" />
                </el-form-item>
                <el-form-item label="描述">
                    <el-input v-model="form.description" type="textarea" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </template>
        </el-dialog>

        <!-- 库存操作表单 -->
        <el-dialog v-model="stockDialogVisible" :title="stockType === 'IN' ? '商品入库' : '商品出库'" width="400px">
            <el-form :model="stockForm" label-width="80px">
                <el-form-item label="变动数量">
                    <el-input-number v-model="stockForm.quantity" :min="1" />
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="stockForm.remark" placeholder="如：采购单号/销售单号" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="stockDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitStock">确定</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.header-actions {
    display: flex;
    justify-content: space-between;
}
</style>
