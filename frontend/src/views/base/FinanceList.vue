<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getFinance, addFinance, deleteFinance, batchDeleteFinance, type FinanceRecord } from '../../api/base'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import BatchToolbar from '../../components/BatchToolbar.vue'

const tableData = ref<FinanceRecord[]>([])
const total = ref(0)
const currentPage = ref(1)
const dialogVisible = ref(false)
const selectedIds = ref<number[]>([])
const form = ref<FinanceRecord>({ type: 1, amount: 0, category: '' })

const columns = [
    { label: 'ID', prop: 'id' }, { label: '日期', prop: 'createdAt' },
    { label: '类型', prop: 'type' }, { label: '金额', prop: 'amount' },
    { label: '来源单据单号', prop: 'sourceOrderNo' }, { label: '收入来源/支出去向', prop: 'targetName' },
    { label: '备注', prop: 'remark' }
]

const loadData = async () => {
    const res = await getFinance({ page: currentPage.value, size: 10 }) as any
    tableData.value = res.records
    total.value = res.total
}

const handleAdd = () => {
    form.value = { type: 1, amount: 0, category: '', targetName: '', remark: '', sourceOrderNo: '', incomeSource: '', expenseTarget: '' }
    dialogVisible.value = true
}

const submitForm = async () => {
    if (form.value.amount <= 0) return ElMessage.warning('金额必须大于0')
    await addFinance(form.value)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    loadData()
}

const handleDelete = (row: FinanceRecord) => {
    ElMessageBox.confirm('确定删除?', '提示', { type: 'warning' }).then(async () => {
        if (row.id) {
            await deleteFinance(row.id)
            ElMessage.success('删除成功')
            loadData()
        }
    })
}

const handleSelectionChange = (rows: FinanceRecord[]) => {
    selectedIds.value = rows.map(r => r.id!).filter(Boolean)
}

const handleBatchDelete = async (ids: number[]) => {
    try {
        await batchDeleteFinance(ids)
    } catch {
        for (const id of ids) { await deleteFinance(id) }
    }
    ElMessage.success('批量删除成功')
    loadData()
}

onMounted(loadData)
</script>

<template>
    <el-card>
        <div class="header">
            <h3>财务信息</h3>
            <div style="display:flex;gap:10px;align-items:center">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="财务信息" @batch-delete="handleBatchDelete" />
                <el-button type="primary" @click="handleAdd">新增流水</el-button>
            </div>
        </div>

        <el-table :data="tableData" border style="width: 100%; margin-top: 20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="60" />
            <el-table-column prop="createdAt" label="日期" width="170" />
            <el-table-column prop="type" label="类型" width="80">
                <template #default="scope">
                    <el-tag :type="scope.row.type === 1 ? 'success' : 'danger'">
                        {{ scope.row.type === 1 ? '收入' : '支出' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="120">
                <template #default="{ row }">¥{{ row.amount || 0 }}</template>
            </el-table-column>
            <el-table-column prop="sourceOrderNo" label="来源单据单号" width="180" />
            <el-table-column prop="category" label="科目" width="120" />
            <el-table-column label="金额收入来源/支出去向" width="180">
                <template #default="{ row }">{{ row.incomeSource || row.expenseTarget || row.targetName || '' }}</template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" width="80" fixed="right">
                <template #default="{ row }">
                    <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination background layout="prev, pager, next" :total="total"
                v-model:current-page="currentPage" @current-change="loadData" />
        </div>

        <!-- 新增流水 Dialog -->
        <el-dialog v-model="dialogVisible" title="新增财务流水" width="500px">
            <el-form :model="form" label-width="120px">
                <el-form-item label="类型" required>
                    <el-radio-group v-model="form.type">
                        <el-radio :value="1">收入</el-radio>
                        <el-radio :value="2">支出</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="金额" required>
                    <el-input-number v-model="form.amount" :precision="2" :step="0.1" />
                </el-form-item>
                <el-form-item label="来源单据单号">
                    <el-input v-model="form.sourceOrderNo" placeholder="关联的采购或销售订单单号" />
                </el-form-item>
                <el-form-item label="科目">
                    <el-input v-model="form.category" placeholder="如：销售收入、采购支出" />
                </el-form-item>
                <el-form-item :label="form.type === 1 ? '金额收入来源' : '金额支出去向'">
                    <el-input v-model="form.targetName" :placeholder="form.type === 1 ? '收入来源' : '支出去向'" />
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" type="textarea" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitForm">确定</el-button>
            </template>
        </el-dialog>
    </el-card>
</template>

<style scoped>
.header { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }
.pagination { margin-top: 20px; display: flex; justify-content: center; }
</style>
