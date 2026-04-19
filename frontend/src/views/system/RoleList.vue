<template>
  <div class="app-container">
    <div class="header-actions">
      <h2>角色管理</h2>
      <el-button type="primary" @click="handleAdd">新增角色</el-button>
    </div>

    <el-card>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="角色名称" />
        <el-table-column prop="code" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态">
             <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button link type="primary" size="small" @click="handleAssign(scope.row)">分配权限</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色表单 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
         <el-form-item label="状态" prop="status">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限弹窗 -->
    <el-dialog v-model="permDialogVisible" title="分配权限" width="500px">
        <el-tree
            ref="treeRef"
            :data="permissionTree"
            :props="{ label: 'name', children: 'children' }"
            show-checkbox
            node-key="id"
            default-expand-all
        />
        <template #footer>
            <el-button @click="permDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitAssign">确定</el-button>
        </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { getRoleList, addRole, updateRole, deleteRole, getRolePermissions, assignPermissions } from '@/api/role'
import { getPermissionTree } from '@/api/permission'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = reactive({ id: undefined, name: '', code: '', description: '', status: 1 })

// Permission Assign
const permDialogVisible = ref(false)
const permissionTree = ref([])
const treeRef = ref()
const currentRoleId = ref<number>()

const rules = {
    name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入编码', trigger: 'blur' }]
}

const loadData = async () => {
    loading.value = true
    try {
        const res: any = await getRoleList({ page: 1, size: 100 })
        tableData.value = res.records
    } finally {
        loading.value = false
    }
}

const handleAdd = () => {
    isEdit.value = false
    form.id = undefined
    form.name = ''
    form.code = ''
    form.description = ''
    form.status = 1
    dialogVisible.value = true
}

const handleEdit = (row: any) => {
    isEdit.value = true
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确认删除该角色?', '警告', { type: 'warning' }).then(async () => {
        await deleteRole(row.id)
        ElMessage.success('删除成功')
        loadData()
    })
}

const submitForm = async () => {
    await formRef.value.validate(async (valid: boolean) => {
        if (valid) {
            if (isEdit.value) {
                await updateRole(form.id, form)
            } else {
                await addRole(form)
            }
            ElMessage.success('操作成功')
            dialogVisible.value = false
            loadData()
        }
    })
}

const handleAssign = async (row: any) => {
    currentRoleId.value = row.id
    // Load tree
    const treeRes: any = await getPermissionTree()
    permissionTree.value = treeRes
    
    // Load current permissions
    const currentPerms: any = await getRolePermissions(row.id)
    const currentIds = currentPerms.map((p: any) => p.id)
    
    permDialogVisible.value = true
    nextTick(() => {
        treeRef.value!.setCheckedKeys(currentIds, false)
    })
}

const submitAssign = async () => {
    const checkedKeys = treeRef.value!.getCheckedKeys()
    const halfCheckedKeys = treeRef.value!.getHalfCheckedKeys()
    const allKeys = [...checkedKeys, ...halfCheckedKeys]
    
    await assignPermissions(currentRoleId.value!, allKeys)
    ElMessage.success('权限分配成功')
    permDialogVisible.value = false
}

const resetForm = () => {
    if(formRef.value) formRef.value.resetFields()
}

onMounted(() => {
    loadData()
})
</script>

<style scoped>
.app-container { padding: 20px; }
.header-actions { display: flex; justify-content: space-between; margin-bottom: 20px; }
</style>
