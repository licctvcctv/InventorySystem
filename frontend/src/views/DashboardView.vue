<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '../api/request'
import { useRouter, useRoute } from 'vue-router'
import { computed } from 'vue'
import { hasAnyRole, isAdminRole } from '../utils/access'

const router = useRouter()
const username = ref('')

const parseToken = (token: string) => {
    try {
        const base64Url = token.split('.')[1]
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
        }).join(''))
        return JSON.parse(jsonPayload)
    } catch (e) {
        return null
    }
}

const route = useRoute()
const activeMenu = computed(() => route.path)
const userRole = ref('')

const hasRole = (roles: string[]) => hasAnyRole(userRole.value, roles)

const isAdmin = computed(() => isAdminRole(userRole.value))

const canSeeFinance = computed(() => hasRole(['ROLE_FINANCE']))
const canSeeCustomer = computed(() => hasRole(['ROLE_SALES']))
const canSeeSupplier = computed(() => hasRole(['ROLE_PROCUREMENT']))
const canSeeStock = computed(() => hasRole(['ROLE_WAREHOUSE']))
const canManageProducts = computed(() => hasRole(['ROLE_WAREHOUSE']))
const canManageCategories = computed(() => hasRole(['ROLE_WAREHOUSE']))
const canSeePurchase = computed(() => hasRole(['ROLE_PROCUREMENT']))
const canSeeSales = computed(() => hasRole(['ROLE_SALES']))
const canSeeOrders = computed(() => hasRole(['ROLE_SALES', 'ROLE_PROCUREMENT']))
const canSeeReport = computed(() => hasRole(['ROLE_SALES', 'ROLE_PROCUREMENT', 'ROLE_WAREHOUSE', 'ROLE_FINANCE']))
const canSeeBase = computed(() =>
    canManageProducts.value ||
    canManageCategories.value ||
    canSeeStock.value ||
    canSeeCustomer.value ||
    canSeeSupplier.value ||
    canSeeFinance.value
)

onMounted(() => {
    const token = localStorage.getItem('token')
    const role = localStorage.getItem('role')

    if (role) userRole.value = role

    const savedName = localStorage.getItem('fullName')
    if (savedName) username.value = savedName

    if (token) {
        const payload = parseToken(token)
        if (payload && payload.sub) {
            const sub = payload.sub
            if (!savedName) username.value = sub

            request.get('/users', { params: { username: sub, page: 1, size: 1 } })
                .then((res: any) => {
                    if (res && res.records && res.records.length > 0) {
                        const user = res.records[0]
                        if (user.fullName) {
                            username.value = user.fullName
                            localStorage.setItem('fullName', user.fullName)
                            if (user.role) {
                                userRole.value = user.role
                                localStorage.setItem('role', user.role)
                            }
                        }
                    }
                })
                .catch(() => console.log('Failed to refresh user info'))
        }
    }
})

const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('role')
    localStorage.removeItem('fullName')
    router.push('/login')
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h2>进销存管理系统</h2>
      </div>
      <el-menu
        active-text-color="#409EFF"
        background-color="#304156"
        class="el-menu-vertical"
        text-color="#bfcbd9"
        :default-active="activeMenu"
        router
      >
        <el-menu-item index="/dashboard/home">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/dashboard/report" v-if="canSeeReport">
          <el-icon><DataAnalysis /></el-icon>
          <span>统计报表</span>
        </el-menu-item>

        <!-- 采购管理 -->
        <el-sub-menu index="purchase" v-if="canSeePurchase">
          <template #title>
            <el-icon><ShoppingCart /></el-icon>
            <span>采购管理</span>
          </template>
          <el-menu-item index="/dashboard/purchase/create">新增采购订单</el-menu-item>
          <el-menu-item index="/dashboard/purchase/list">历史单据</el-menu-item>
        </el-sub-menu>

        <!-- 销售管理 -->
        <el-sub-menu index="sales" v-if="canSeeSales">
          <template #title>
            <el-icon><Sell /></el-icon>
            <span>销售管理</span>
          </template>
          <el-menu-item index="/dashboard/sales/create">新增销售订单</el-menu-item>
          <el-menu-item index="/dashboard/sales/list">历史单据</el-menu-item>
        </el-sub-menu>

        <!-- 库存管理 -->
        <el-sub-menu index="inventory" v-if="canSeeStock">
          <template #title>
            <el-icon><Box /></el-icon>
            <span>库存管理</span>
          </template>
          <el-menu-item index="/dashboard/inventory/stock-in-list">入库单</el-menu-item>
          <el-menu-item index="/dashboard/inventory/stock-out-list">出库单</el-menu-item>
          <el-menu-item index="/dashboard/inventory/ledger">库存台账</el-menu-item>
          <el-menu-item index="/dashboard/inventory/products">商品库存</el-menu-item>
          <el-menu-item index="/dashboard/inventory/records">出入库明细</el-menu-item>
        </el-sub-menu>

        <!-- 旧订单管理 -->
        <el-sub-menu index="orders" v-if="canSeeOrders">
          <template #title>
            <el-icon><Goods /></el-icon>
            <span>订单管理</span>
          </template>
          <el-menu-item index="/dashboard/orders">
             <el-icon><List /></el-icon>订单列表
          </el-menu-item>
        </el-sub-menu>

        <!-- 系统管理 -->
        <el-sub-menu index="1" v-if="isAdmin">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/dashboard/users">
             <el-icon><User /></el-icon>用户管理
          </el-menu-item>
          <el-menu-item index="/dashboard/roles">
             <el-icon><Avatar /></el-icon>角色管理
          </el-menu-item>
          <el-menu-item index="/dashboard/permissions">
             <el-icon><Key /></el-icon>权限管理
          </el-menu-item>
          <el-menu-item index="/dashboard/logs">
             <el-icon><Document /></el-icon>登录日志
          </el-menu-item>
        </el-sub-menu>

        <!-- 基础信息管理 -->
        <el-sub-menu index="base" v-if="canSeeBase">
          <template #title>
            <el-icon><DataBoard /></el-icon>
            <span>基础信息管理</span>
          </template>
          <el-menu-item index="/dashboard/base/products" v-if="canManageProducts">商品信息</el-menu-item>
          <el-menu-item index="/dashboard/inventory/categories" v-if="canManageCategories">商品分类</el-menu-item>
          <el-menu-item index="/dashboard/inventory/warehouses" v-if="canSeeStock">仓库管理</el-menu-item>
          <el-menu-item index="/dashboard/base/customers" v-if="canSeeCustomer">客户管理</el-menu-item>
          <el-menu-item index="/dashboard/base/suppliers" v-if="canSeeSupplier">供应商管理</el-menu-item>
          <el-menu-item index="/dashboard/base/finance" v-if="canSeeFinance">财务信息</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left"></div>
        <div class="header-right">
            <span class="welcome-text">欢迎, {{ username }}</span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container { height: 100vh; }
.aside { background-color: #304156; color: #fff; display: flex; flex-direction: column; }
.logo { height: 60px; line-height: 60px; background-color: #2b2f3a; text-align: center; overflow: hidden; }
.logo h2 { margin: 0; color: #fff; font-size: 18px; font-weight: 600; }
.el-menu-vertical { border-right: none; }
.header { background-color: #fff; border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; height: 60px; }
.header-right { display: flex; align-items: center; gap: 15px; }
.main { background-color: #f0f2f5; padding: 20px; }
</style>
