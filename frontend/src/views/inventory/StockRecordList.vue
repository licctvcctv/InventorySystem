<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getStockRecords } from '../../api/inventory'
import { Search } from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ page: 1, size: 10, productId: null })

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getStockRecords(queryParams)
        tableData.value = res.records
        total.value = res.total
    } finally {
        loading.value = false
    }
}

onMounted(fetchData)
</script>

<template>
    <el-card>
        <el-table :data="tableData" v-loading="loading" border stripe>
            <el-table-column prop="productId" label="商品ID" width="100" />
            <el-table-column prop="type" label="类型" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.type === 1 ? 'success' : 'warning'">
                        {{ row.type === 1 ? '入库' : '出库' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="120" />
            <el-table-column prop="remark" label="备注" />
            <el-table-column prop="createdAt" label="操作时间" width="180" />
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
    </el-card>
</template>
