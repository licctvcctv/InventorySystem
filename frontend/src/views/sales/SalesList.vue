<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getOrders, deleteOrder, batchDeleteOrders, type Order } from '../../api/order'
import { Plus, Search, Delete, View, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import BatchToolbar from '../../components/BatchToolbar.vue'

const router = useRouter()
const loading = ref(false)
const tableData = ref<Order[]>([])
const total = ref(0)
const selectedIds = ref<number[]>([])
const queryParams = reactive({ page: 1, size: 10, type: 'SALE', keyword: '' })

const columns = [
    { label: 'ID', prop: 'id' }, { label: '创建时间', prop: 'createdAt' },
    { label: '销售单号', prop: 'orderNo' }, { label: '实销金额', prop: 'totalAmount' },
    { label: '客户', prop: 'customerId' }, { label: '业务员', prop: 'salesman' },
    { label: '备注', prop: 'description' }
]

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getOrders(queryParams) as any
        tableData.value = res.records
        total.value = res.total
    } finally { loading.value = false }
}

const handleSearch = () => { queryParams.page = 1; fetchData() }
const handleAdd = () => router.push('/dashboard/sales/create')
const handleView = (row: Order) => router.push(`/dashboard/sales/detail?id=${row.id}`)
const handleEdit = (row: Order) => router.push(`/dashboard/sales/create?id=${row.id}`)

const handleDelete = (row: Order) => {
    ElMessageBox.confirm('确认删除该销售订单吗?', '提示', { type: 'warning' }).then(async () => {
        await deleteOrder(row.id!)
        ElMessage.success('删除成功')
        fetchData()
    })
}

const handleReceipt = (row: Order) => {
    router.push(`/dashboard/sales/receipt?orderId=${row.id}`)
}

const handleInvoice = (row: Order) => {
    router.push(`/dashboard/sales/invoice?orderId=${row.id}`)
}

const getPaymentText = (s: number) => {
    switch(s) { case 0: return '未收'; case 1: return '部分收'; case 2: return '已收'; default: return '未知' }
}

const handleSelectionChange = (rows: Order[]) => {
    selectedIds.value = rows.map(r => r.id!).filter(Boolean)
}

const handleBatchDelete = async (ids: number[]) => {
    try {
        await batchDeleteOrders(ids)
        ElMessage.success('批量删除成功')
        fetchData()
    } catch {
        for (const id of ids) { await deleteOrder(id) }
        ElMessage.success('批量删除成功')
        fetchData()
    }
}

onMounted(fetchData)
</script>

<template>
    <el-card>
        <div class="header-actions">
            <div class="left">
                <el-input v-model="queryParams.keyword" placeholder="销售单号/备注" style="width:200px" clearable @clear="handleSearch" />
                <el-button type="primary" :icon="Search" @click="handleSearch" style="margin-left:10px">搜索</el-button>
            </div>
            <div class="right" style="display:flex;gap:10px;align-items:center">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="销售历史单据" @batch-delete="handleBatchDelete" />
                <el-button type="primary" :icon="Plus" @click="handleAdd">新增销售订单</el-button>
            </div>
        </div>
        <el-table :data="tableData" v-loading="loading" style="margin-top:20px" border stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="createdAt" label="创建时间" width="170" />
            <el-table-column prop="orderNo" label="销售单号" width="200" />
            <el-table-column label="实销金额" width="120">
                <template #default="{ row }">¥{{ row.totalAmount || 0 }}</template>
            </el-table-column>
            <el-table-column prop="customerId" label="客户" width="80" />
            <el-table-column label="收款状态" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.paymentStatus === 2 ? 'success' : row.paymentStatus === 1 ? 'warning' : 'danger'">
                        {{ getPaymentText(row.paymentStatus || 0) }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="salesman" label="业务员" width="100" />
            <el-table-column prop="description" label="备注" />
            <el-table-column label="操作" width="340" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" :icon="View" @click="handleView(row)">详情</el-button>
                    <el-button link type="warning" :icon="Edit" @click="handleEdit(row)">修改</el-button>
                    <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                    <el-button link type="success" @click="handleReceipt(row)">收款</el-button>
                    <el-button link type="info" @click="handleInvoice(row)">开票</el-button>
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
