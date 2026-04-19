<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getSuppliers, addSupplier, updateSupplier, deleteSupplier, batchDeleteSuppliers, type Supplier } from '../../api/base'
import { ElMessage, ElMessageBox } from 'element-plus'
import BatchToolbar from '../../components/BatchToolbar.vue'

const tableData = ref<Supplier[]>([])
const total = ref(0)
const currentPage = ref(1)
const keyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref<Supplier>({ name: '' })
const selectedIds = ref<number[]>([])

const columns = [
    { label: 'ID', prop: 'id' }, { label: '供应商名称', prop: 'name' },
    { label: '联系人', prop: 'contactPerson' }, { label: '电话', prop: 'phone' },
    { label: '邮箱', prop: 'email' }, { label: '地址', prop: 'address' }
]

const loadData = async () => {
    const res = await getSuppliers({ page: currentPage.value, size: 10, keyword: keyword.value }) as any
    tableData.value = res.records; total.value = res.total
}

const handleAdd = () => { isEdit.value = false; form.value = { name: '' }; dialogVisible.value = true }
const handleEdit = (row: Supplier) => { isEdit.value = true; form.value = { ...row }; dialogVisible.value = true }

const submitForm = async () => {
    if (!form.value.name) return ElMessage.warning('请输入名称')
    if (isEdit.value) { await updateSupplier(form.value); ElMessage.success('更新成功') }
    else { await addSupplier(form.value); ElMessage.success('添加成功') }
    dialogVisible.value = false; loadData()
}

const handleDelete = (row: Supplier) => {
    ElMessageBox.confirm('确定删除该供应商吗?', '提示', { type: 'warning' }).then(async () => {
        if (row.id) { await deleteSupplier(row.id); ElMessage.success('删除成功'); loadData() }
    })
}

const handleSelectionChange = (rows: Supplier[]) => { selectedIds.value = rows.map(r => r.id!).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteSuppliers(ids) } catch { for (const id of ids) { await deleteSupplier(id) } }
    ElMessage.success('批量删除成功'); loadData()
}

onMounted(loadData)
</script>

<template>
    <el-card>
        <div class="header">
            <div class="left">
                <el-input v-model="keyword" placeholder="搜索供应商名称" style="width:200px;margin-right:10px" @keyup.enter="loadData" />
                <el-button type="primary" @click="loadData">搜索</el-button>
            </div>
            <div style="display:flex;gap:10px;align-items:center">
                <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="供应商管理" @batch-delete="handleBatchDelete" />
                <el-button type="primary" @click="handleAdd">新增供应商</el-button>
            </div>
        </div>
        <el-table :data="tableData" border style="width:100%;margin-top:20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="45" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="供应商名称" />
            <el-table-column prop="contactPerson" label="联系人" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="address" label="地址" />
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination background layout="prev, pager, next" :total="total" v-model:current-page="currentPage" @current-change="loadData" />
        </div>
        <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑供应商' : '新增供应商'" width="40%">
            <el-form :model="form" label-width="100px">
                <el-form-item label="供应商名称" required><el-input v-model="form.name" /></el-form-item>
                <el-form-item label="联系人"><el-input v-model="form.contactPerson" /></el-form-item>
                <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
                <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
                <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
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
