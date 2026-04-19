<template>
  <div class="app-container">
    <div class="header-actions">
      <div class="title-section">
        <h2>用户管理</h2>
      </div>
      <div>
        <BatchToolbar :selected-ids="selectedIds" :columns="columns" :table-data="tableData" entity-name="用户管理" @batch-delete="handleBatchDelete" />
        <el-button type="primary" @click="handleAdd" style="margin-left:10px">新增用户</el-button>
      </div>
    </div>

    <el-card>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="fullName" label="姓名" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ROLE_ADMIN' ? 'warning' : ''">
              {{ scope.row.role === 'ROLE_ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDeleteUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 用户表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="500px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="form.fullName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            :placeholder="isEdit ? '不修改请留空' : '请输入密码'" 
            show-password 
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="普通用户" value="ROLE_USER" />
            <el-option label="管理员" value="ROLE_ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { getUserList, addUser, updateUser, deleteUser, batchDeleteUsers } from '@/api/system'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import BatchToolbar from '@/components/BatchToolbar.vue'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0) // Start with 0

const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const selectedIds = ref<number[]>([])

const columns = [
    { label: '用户名', prop: 'username' }, { label: '姓名', prop: 'fullName' },
    { label: '邮箱', prop: 'email' }, { label: '电话', prop: 'phone' },
    { label: '角色', prop: 'role' }, { label: '状态', prop: 'status' }
]

const form = reactive({
  id: undefined as number | undefined,
  username: '',
  fullName: '',
  password: '',
  email: '',
  phone: '',
  role: 'ROLE_USER',
  status: 1
})

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  fullName: [
      { required: false, message: '请输入姓名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  email: [
      { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ]
})

const loadData = async () => {
  loading.value = true
  try {
    const res: any = await getUserList({
      page: currentPage.value,
      size: pageSize.value
    })
    if (res && res.records) {
        tableData.value = res.records
        total.value = res.total
    }
  } catch (error) {
    console.error('Failed to load users', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadData()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadData()
}

const handleAdd = () => {
    isEdit.value = false
    resetForm()
    // 新增时密码必填
    if (rules.password) {
        (rules.password as any)[0].required = true
    }
    dialogVisible.value = true
}

const handleEdit = (row: any) => {
    isEdit.value = true
    resetForm()
    // 编辑时密码非必填
     if (rules.password) {
        (rules.password as any)[0].required = false
    }
    
    // 赋值
    form.id = row.id
    form.username = row.username
    form.fullName = row.fullName
    form.email = row.email
    form.phone = row.phone
    form.role = row.role || 'ROLE_USER'
    form.status = row.status
    form.password = '' // 编辑时不回显密码
    
    dialogVisible.value = true
}

const resetForm = () => {
    form.id = undefined
    form.username = ''
    form.fullName = ''
    form.password = ''
    form.email = ''
    form.phone = ''
    form.role = 'ROLE_USER'
    form.status = 1
    if (formRef.value) {
        formRef.value.clearValidate()
    }
}

const submitForm = async () => {
    if (!formRef.value) return
    await formRef.value.validate(async (valid) => {
        if (valid) {
            submitLoading.value = true
            try {
                if (isEdit.value && form.id) {
                    await updateUser(form.id, form)
                    ElMessage.success('更新成功')
                } else {
                    await addUser(form)
                    ElMessage.success('新增成功')
                }
                dialogVisible.value = false
                loadData()
            } catch (error: any) {
                 // 错误已经在拦截器处理，或者是业务错误
                 // 如用户名已存在
            } finally {
                submitLoading.value = false
            }
        }
    })
}

onMounted(() => {
  loadData()
})

const handleSelectionChange = (rows: any[]) => { selectedIds.value = rows.map(r => r.id).filter(Boolean) }

const handleBatchDelete = async (ids: number[]) => {
    try { await batchDeleteUsers(ids) } catch { for (const id of ids) { await deleteUser(id) } }
    ElMessage.success('批量删除成功'); loadData()
}

const handleDeleteUser = (row: any) => {
    ElMessageBox.confirm('确定删除该用户吗?', '提示', { type: 'warning' }).then(async () => {
        await deleteUser(row.id); ElMessage.success('删除成功'); loadData()
    })
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.header-actions {
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.title-section h2 {
    margin: 0;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
