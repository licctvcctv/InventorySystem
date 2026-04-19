<template>
  <div class="app-container">
    <div class="header-actions">
      <h2>权限管理</h2>
      <el-button type="primary" @click="handleAdd(0)">新增根权限</el-button>
    </div>

    <el-card>
      <el-table 
        :data="tableData" 
        border 
        style="width: 100%" 
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="name" label="名称" width="200" />
        <el-table-column prop="code" label="标识" />
        <el-table-column prop="type" label="类型" width="100">
            <template #default="scope">
                <el-tag :type="scope.row.type === 'MENU' ? '' : 'info'">{{ scope.row.type }}</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button link type="primary" size="small" @click="handleAdd(scope.row.id)">新增子项</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑权限' : '新增权限'" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="标识" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
           <el-radio-group v-model="form.type">
              <el-radio label="MENU">菜单</el-radio>
              <el-radio label="BUTTON">按钮</el-radio>
           </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getPermissionTree, addPermission, updatePermission, deletePermission } from '@/api/permission'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ id: undefined, name: '', code: '', type: 'MENU', parentId: 0 })

const rules = {
    name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
    code: [{ required: true, message: '请输入标识', trigger: 'blur' }]
}

const loadData = async () => {
    const res: any = await getPermissionTree()
    tableData.value = res
}

const handleAdd = (parentId: number) => {
    form.id = undefined
    form.name = ''
    form.code = ''
    form.type = 'MENU'
    form.parentId = parentId
    dialogVisible.value = true
}

const handleEdit = (row: any) => {
    Object.assign(form, row)
    dialogVisible.value = true
}

const handleDelete = (row: any) => {
    ElMessageBox.confirm('确认删除?', '警告').then(async () => {
        await deletePermission(row.id)
        ElMessage.success('删除成功')
        loadData()
    })
}

const submitForm = async () => {
    await formRef.value.validate(async (valid: boolean) => {
        if (valid) {
            if (form.id) {
                await updatePermission(form.id, form)
            } else {
                await addPermission(form)
            }
            ElMessage.success('操作成功')
            dialogVisible.value = false
            loadData()
        }
    })
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
