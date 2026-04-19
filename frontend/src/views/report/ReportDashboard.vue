<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getOrderReport, getStockReport, getFinanceReport } from '../../api/report'
import { Money, Wallet, Goods, TrendCharts } from '@element-plus/icons-vue'

import { computed } from 'vue'

const loading = ref(false)
const filterDate = ref([]) // [startDate, endDate]

// Role checks
const userRole = localStorage.getItem('role') || ''
const isAdmin = userRole === 'ROLE_ADMIN' || userRole === 'admin'

const canSeeFinance = computed(() => isAdmin || ['ROLE_FINANCE'].includes(userRole))
const canSeeOrders = computed(() => isAdmin || ['ROLE_SALES', 'ROLE_PROCUREMENT', 'ROLE_FINANCE'].includes(userRole))
const canSeeStock = computed(() => isAdmin || ['ROLE_WAREHOUSE', 'ROLE_FINANCE'].includes(userRole))

// Data references
const financeData = ref({ income: 0, expense: 0, profit: 0 })
const orderData = ref({ totalSales: 0, totalPurchases: 0, netIncome: 0 })
const stockData = ref([])

const fetchData = async () => {
    loading.value = true
    try {
        let startDate = ''
        let endDate = ''
        if (filterDate.value && filterDate.value.length === 2) {
            startDate = filterDate.value[0]
            endDate = filterDate.value[1]
        }

        const promises = []
        if (canSeeFinance.value) promises.push(getFinanceReport(startDate, endDate))
        else promises.push(Promise.resolve(null))

        if (canSeeOrders.value) promises.push(getOrderReport(startDate, endDate))
        else promises.push(Promise.resolve(null))

        if (canSeeStock.value) promises.push(getStockReport(startDate, endDate)) // Added params here just in case frontend api supports it
        else promises.push(Promise.resolve([]))

        const [financeRes, orderRes, stockRes] = await Promise.all(promises)

        financeData.value = financeRes || { income: 0, expense: 0, profit: 0 }
        orderData.value = orderRes || { totalSales: 0, totalPurchases: 0, netIncome: 0 }
        stockData.value = stockRes || []
        
    } catch (e) {
        console.error('Failed to fetch report data', e)
        // Suppress error if it's just a perm issue (though v-if prevents it usually)
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    fetchData()
})

const handleDateChange = () => {
    fetchData()
}
</script>

<template>
    <div class="report-dashboard">
        <!-- Header / Filter -->
        <el-card class="filter-card">
            <div class="filter-header">
                <h3>统计报表</h3>
                <el-date-picker
                    v-model="filterDate"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="YYYY-MM-DD"
                    @change="handleDateChange"
                    size="default"
                />
            </div>
        </el-card>

        <!-- Finance Stats -->
        <el-row :gutter="20" class="mt-20" v-if="canSeeFinance">
            <el-col :span="8">
                <el-card shadow="hover" class="stat-card income-bg">
                    <el-statistic :value="financeData.income" :precision="2" prefix="¥">
                        <template #title>
                            <div class="stat-title"><el-icon><Money /></el-icon> 总收入</div>
                        </template>
                    </el-statistic>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="stat-card expense-bg">
                    <el-statistic :value="financeData.expense" :precision="2" prefix="¥">
                        <template #title>
                             <div class="stat-title"><el-icon><Wallet /></el-icon> 总支出</div>
                        </template>
                    </el-statistic>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="stat-card profit-bg">
                    <el-statistic :value="financeData.profit" :precision="2" prefix="¥" :value-style="{ color: financeData.profit >= 0 ? '#67C23A' : '#F56C6C' }">
                        <template #title>
                             <div class="stat-title"><el-icon><TrendCharts /></el-icon> 净利润</div>
                        </template>
                    </el-statistic>
                </el-card>
            </el-col>
        </el-row>

        <!-- Order Stats -->
        <el-row :gutter="20" class="mt-20" v-if="canSeeOrders">
            <el-col :span="12">
                 <el-card shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span><el-icon><Goods /></el-icon> 订单销售概览</span>
                        </div>
                    </template>
                    <el-row :gutter="20">
                        <el-col :span="12">
                            <el-statistic title="销售总额" :value="orderData.totalSales" :precision="2" />
                        </el-col>
                         <el-col :span="12">
                            <el-statistic title="采购成本" :value="orderData.totalPurchases" :precision="2" />
                        </el-col>
                    </el-row>
                 </el-card>
            </el-col>
            
            <el-col :span="12">
                <!-- Placeholder for Charts or other stats -->
                <el-card shadow="hover">
                    <template #header>
                        <span>业务利润估算 (订单维度)</span>
                    </template>
                    <div class="profit-estimate">
                        <h2>¥ {{ Number(orderData.totalSales - orderData.totalPurchases).toFixed(2) }}</h2>
                        <span class="sub-text">销售 - 采购 (不含运营成本)</span>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- Stock Details -->
        <el-card shadow="always" class="mt-20" v-if="canSeeStock">
            <template #header>
               <div class="card-header">
                    <span>各仓库库存快照</span>
               </div>
            </template>
            <el-table :data="stockData" style="width: 100%" v-loading="loading" stripe>
                <el-table-column prop="warehouse_name" label="仓库名称" width="150" sortable />
                <el-table-column prop="product_name" label="商品名称" width="150" sortable />
                <el-table-column prop="stock" label="当前库存量">
                    <template #default="scope">
                        <el-tag :type="scope.row.stock > 10 ? 'success' : 'danger'">{{ scope.row.stock }}</el-tag>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<style scoped>
.mt-20 {
    margin-top: 20px;
}
.report-dashboard {
    padding: 10px;
}
.filter-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.stat-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
}
.income-bg {
    background: #f0f9eb;
}
.expense-bg {
    background: #fef0f0;
}
.profit-bg {
    background: #f4f6f8;
}
.profit-estimate {
    text-align: center;
    padding: 10px 0;
}
.profit-estimate h2 {
    margin: 0;
    color: #409EFF;
    font-size: 28px;
}
.sub-text {
    font-size: 12px;
    color: #909399;
}
</style>
