<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Download, Upload, Printer } from '@element-plus/icons-vue'

const props = defineProps<{
    selectedIds: number[]
    columns: { label: string; prop: string }[]
    tableData: any[]
    entityName: string
}>()

const emit = defineEmits(['batch-delete', 'refresh'])
const importDialogVisible = ref(false)

const handleBatchDelete = () => {
    if (props.selectedIds.length === 0) return ElMessage.warning('请先选择要删除的数据')
    ElMessageBox.confirm(`确认批量删除选中的 ${props.selectedIds.length} 条数据吗?`, '批量删除', { type: 'warning' })
        .then(() => emit('batch-delete', props.selectedIds))
}

const handleExport = () => {
    if (props.tableData.length === 0) return ElMessage.warning('没有数据可导出')
    const selected = props.selectedIds.length > 0
        ? props.tableData.filter(r => props.selectedIds.includes(r.id))
        : props.tableData
    const headers = props.columns.map(c => c.label)
    const rows = selected.map(row => props.columns.map(c => row[c.prop] ?? ''))
    const bom = '\uFEFF'
    const csv = bom + [headers.join(','), ...rows.map(r => r.map((v: any) => `"${v}"`).join(','))].join('\n')
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = `${props.entityName}_${new Date().toISOString().slice(0, 10)}.csv`
    link.click()
    ElMessage.success('导出成功')
}

const handleImport = () => {
    importDialogVisible.value = true
}

const handleFileUpload = (e: Event) => {
    const file = (e.target as HTMLInputElement).files?.[0]
    if (!file) return
    ElMessage.info('导入功能：请确保CSV格式与导出格式一致')
    importDialogVisible.value = false
}

const handlePrint = () => {
    const selected = props.selectedIds.length > 0
        ? props.tableData.filter(r => props.selectedIds.includes(r.id))
        : props.tableData
    if (selected.length === 0) return ElMessage.warning('没有数据可打印')
    const headers = props.columns.map(c => c.label)
    const rows = selected.map(row =>
        `<tr>${props.columns.map(c => `<td style="border:1px solid #ddd;padding:6px">${row[c.prop] ?? ''}</td>`).join('')}</tr>`)
    const html = `<html><head><title>打印 - ${props.entityName}</title>
        <style>body{font-family:sans-serif}table{border-collapse:collapse;width:100%}th{background:#f5f5f5;border:1px solid #ddd;padding:8px}h2{text-align:center}</style></head>
        <body><h2>${props.entityName}</h2><table><tr>${headers.map(h => `<th>${h}</th>`).join('')}</tr>${rows.join('')}</table>
        <script>window.print();window.close();<\/script></body></html>`
    const win = window.open('', '_blank')
    if (win) { win.document.write(html); win.document.close() }
}
</script>

<template>
    <div class="batch-toolbar">
        <el-button size="small" type="danger" :icon="Delete" @click="handleBatchDelete"
            :disabled="selectedIds.length === 0">批量删除 ({{ selectedIds.length }})</el-button>
        <el-button size="small" type="success" :icon="Download" @click="handleExport">导出</el-button>
        <el-button size="small" type="warning" :icon="Upload" @click="handleImport">导入</el-button>
        <el-button size="small" :icon="Printer" @click="handlePrint">打印</el-button>
    </div>
    <el-dialog v-model="importDialogVisible" title="导入数据" width="400px">
        <p>请选择CSV文件导入（格式需与导出一致）</p>
        <input type="file" accept=".csv" @change="handleFileUpload" />
        <template #footer>
            <el-button @click="importDialogVisible = false">取消</el-button>
        </template>
    </el-dialog>
</template>

<style scoped>
.batch-toolbar { display: flex; gap: 8px; align-items: center; }
</style>
