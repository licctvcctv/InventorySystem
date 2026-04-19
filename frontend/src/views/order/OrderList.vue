<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getOrders, getOrder, deleteOrder, type Order } from '../../api/order'
import { Plus, Search, Delete, View } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const tableData = ref<Order[]>([])
const total = ref(0)
const queryParams = reactive({
    page: 1,
    size: 10,
    type: '',
    keyword: ''
})

const detailVisible = ref(false)
const currentOrder = ref<Order | null>(null)

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getOrders(queryParams)
        // 修正：request.ts 已解包，直接读取 records
        tableData.value = res.records
        total.value = res.total
    } finally {
        loading.value = false
    }
}

const handleSearch = () => {
    queryParams.page = 1
    fetchData()
}

const handleAdd = () => {
    router.push('/dashboard/orders/create')
}

const handleDelete = (row: Order) => {
    ElMessageBox.confirm('确认删除该订单吗?', '提示', {
        type: 'warning'
    }).then(async () => {
        await deleteOrder(row.id!)
        ElMessage.success('删除成功')
        fetchData()
    })
}

const handleView = async (row: Order) => {
    try {
        const res = await getOrder(row.id!)
        // 修正：request.ts 已解包，直接赋值 res
        currentOrder.value = res as unknown as Order
        detailVisible.value = true
    } catch (e) {
        ElMessage.error('获取详情失败')
    }
}

const getTypeText = (type: string) => {
    return type === 'PURCHASE' ? '采购订单' : '销售订单'
}

const getStatusText = (status: number) => {
    switch(status) {
        case 0: return '待处理'
        case 1: return '已完成'
        case 2: return '已取消'
        default: return '未知'
    }
}

onMounted(() => {
    fetchData()
})
</script>

<template>
    <el-card>
        <div class="header-actions">
            <div class="left">
                <el-select v-model="queryParams.type" placeholder="订单类型" clearable style="width: 150px" @change="handleSearch">
                    <el-option label="采购订单" value="PURCHASE" />
                    <el-option label="销售订单" value="SALE" />
                </el-select>
                <el-input v-model="queryParams.keyword" placeholder="订单号/备注" style="width: 200px; margin-left: 10px" clearable @clear="handleSearch" />
                <el-button type="primary" :icon="Search" @click="handleSearch" style="margin-left: 10px">搜索</el-button>
            </div>
            <div class="right">
                <el-button type="primary" :icon="Plus" @click="handleAdd">新增订单</el-button>
            </div>
        </div>

        <el-table :data="tableData" v-loading="loading" style="margin-top: 20px" border stripe>
            <el-table-column prop="orderNo" label="订单编号" width="180" />
            <el-table-column prop="type" label="类型" width="120">
                <template #default="{ row }">
                    <el-tag :type="row.type === 'PURCHASE' ? 'warning' : 'success'">{{ getTypeText(row.type) }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="totalAmount" label="总金额" sortable>
                <template #default="{ row }">
                    ¥ {{ row.totalAmount }}
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                    {{ getStatusText(row.status) }}
                </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180" />
            <el-table-column label="操作" width="180" fixed="right">
                <template #default="{ row }">
                    <el-button link type="primary" :icon="View" @click="handleView(row)">详情</el-button>
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

        <!-- 详情弹窗 -->
        <el-dialog v-model="detailVisible" title="订单详情" width="60%">
            <div v-if="currentOrder">
                <el-descriptions title="基础信息" :column="2" border>
                    <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
                    <el-descriptions-item label="类型">{{ getTypeText(currentOrder.type) }}</el-descriptions-item>
                    <el-descriptions-item label="总金额">¥ {{ currentOrder.totalAmount }}</el-descriptions-item>
                    <el-descriptions-item label="状态">{{ getStatusText(currentOrder.status!) }}</el-descriptions-item>
                    <el-descriptions-item label="备注" :span="2">{{ currentOrder.description }}</el-descriptions-item>
                </el-descriptions>

                <div style="margin-top: 20px">
                    <h4>商品明细</h4>
                    <el-table :data="currentOrder.items" border style="width: 100%">
                        <el-table-column prop="productName" label="商品名称" />
                        <el-table-column prop="quantity" label="数量" width="100" />
                        <el-table-column prop="price" label="单价" width="120" />
                        <el-table-column prop="amount" label="小计" width="120">
                             <template #default="{ row }">
                                ¥ {{ row.amount }}
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>
