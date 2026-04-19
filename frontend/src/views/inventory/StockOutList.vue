<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getStockRecords, deleteStockRecord, batchDeleteStockRecords } from '../../api/inventory'
import { Plus, Search, Delete, View, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import BatchToolbar from '../../components/BatchToolbar.vue'

const router = useRouter()
const loading = ref(false)
const tableData = ref<any[]>([])
const total = ref(0)
const selectedIds = ref<number[]>([])
const queryParams = reactive({ page: 1, size: 10, type: 2, keyword: '' })

const columns = [
    { label: 'ID', prop: 'id' }, { label: '创建时间', prop: 'createdAt' },
    { label: '出库单号', prop: 'recordNo' }, { label: '商品名称', prop: 'productName' },
    { label: '供应商', prop: 'supplierId' }, { label: '仓库', prop: 'warehouseId' },
    { label: '库存量', prop: 'stockQuantity' }
]

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getStockRecords(queryParams) as any
        tableData.value = res.records; total.value = res.total
    } finally { loading.value = false }
}

const handleSearch = () => { queryParams.page = 1; fetchData() }
const handleAdd = () => router.push('/dashboard/inventory/stock-out-create')
const handleView = (row: any) => router.push(`/dashboard/inventory/stock-out-detail?id=${row.id}`)
const handleEdit = (row: any) => router.push(`/dashboard/inventory/stock-out-create?id=${row.id}`)

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确认删除?', '提示', { type: 'warning' }).then(async () => {
        await deleteStockRecord(row.id); ElMessage.success('删除成功'); fetchData()
    })
}

const handleSelectionChange = (rows: any[]) => { selectedIds.value = rows.map(r => r.id).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteStockRecords(ids) } catch { for (const id of ids) { await deleteStockRecord(id) } }
    ElMessage.success('批量删除成功'); fetchData()
}

onMounted(fetchData)
</script>

<template>
    <el-card>
        <div class="header-actions">
            <div class="left">
                <el-input v-model="queryParams.keyword" placeholder="出库单号/商品" style="width:200px" clearable @clear="handleSearch" />
                <el-button type="primary" :icon="Search" @click="handleSearch" style="margin-left:10px">搜索</el-button>
            </div>
            <div class="right" style="display:flex;gap:10px;align-items:center">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="出库单" @batch-delete="handleBatchDelete" />
                <el-button type="primary" :icon="Plus" @click="handleAdd">新增出库单</el-button>
            </div>
        </div>
        <el-table :data="tableData" v-loading="loading" style="margin-top:20px" border stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="createdAt" label="创建时间" width="170" />
            <el-table-column prop="recordNo" label="出库单号" width="200" />
            <el-table-column prop="productName" label="商品名称" />
            <el-table-column prop="supplierId" label="供应商" width="80" />
            <el-table-column prop="warehouseId" label="仓库" width="80" />
            <el-table-column prop="stockQuantity" label="库存量" width="80" />
            <el-table-column label="操作" width="220" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" :icon="View" @click="handleView(row)">详情</el-button>
                    <el-button link type="warning" :icon="Edit" @click="handleEdit(row)">修改</el-button>
                    <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination v-if="total > 0" style="margin-top:20px;display:flex;justify-content:flex-end"
            v-model:current-page="queryParams.page" v-model:page-size="queryParams.size"
            :total="total" layout="total, prev, pager, next" @current-change="fetchData" />
    </el-card>
</template>

<style scoped>
.header-actions { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }
</style>
