<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getCategories, addCategory, deleteCategory, batchDeleteCategories } from '../../api/inventory'
import { ElMessage, ElMessageBox } from 'element-plus'
import BatchToolbar from '../../components/BatchToolbar.vue'

const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const form = ref({ name: '', description: '' })
const selectedIds = ref<number[]>([])

const columns = [
    { label: 'ID', prop: 'id' }, { label: '分类名称', prop: 'name' },
    { label: '描述', prop: 'description' }, { label: '创建时间', prop: 'createdAt' }
]

const loadData = async () => { tableData.value = await getCategories() as any }

const handleAdd = () => { form.value = { name: '', description: '' }; dialogVisible.value = true }

const submitForm = async () => {
    if (!form.value.name) return ElMessage.warning('请输入名称')
    await addCategory(form.value); ElMessage.success('添加成功'); dialogVisible.value = false; loadData()
}

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确定删除该分类吗?', '提示', { type: 'warning' }).then(async () => {
        await deleteCategory(row.id); ElMessage.success('删除成功'); loadData()
    })
}

const handleSelectionChange = (rows: any[]) => { selectedIds.value = rows.map(r => r.id).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteCategories(ids) } catch { for (const id of ids) { await deleteCategory(id) } }
    ElMessage.success('批量删除成功'); loadData()
}

onMounted(loadData)
</script>

<template>
    <el-card>
        <div class="header">
            <h3>商品分类管理</h3>
            <div style="display:flex;gap:10px;align-items:center">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="商品分类" @batch-delete="handleBatchDelete" />
                <el-button type="primary" @click="handleAdd">新增分类</el-button>
            </div>
        </div>
        <el-table :data="tableData" border style="width:100%;margin-top:20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="分类名称" />
            <el-table-column prop="description" label="描述" />
            <el-table-column prop="createdAt" label="创建时间" />
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog v-model="dialogVisible" title="新增分类" width="30%">
            <el-form :model="form" label-width="80px">
                <el-form-item label="名称" required><el-input v-model="form.name" /></el-form-item>
                <el-form-item label="描述"><el-input v-model="form.description" type="textarea" /></el-form-item>
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
</style>
