<template>
  <div class="app-container">
    <div class="header-actions">
        <h2>登录日志</h2>
    </div>

    <el-card>
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="账号" width="150" />
        <el-table-column prop="fullName" label="姓名" width="150" />
        <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '成功' : '失败' }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="loginTime" label="登录时间" />
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getLoginLogList } from '@/api/system'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getLoginLogList({
      page: currentPage.value,
      size: pageSize.value
    })
    if (res && res.records) {
        tableData.value = res.records
        total.value = res.total
    }
  } catch (error) {
    console.error('Failed to load logs', error)
    ElMessage.error('加载日志列表失败')
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

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.header-actions {
    margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
