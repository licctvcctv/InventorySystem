import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { canAccessRoles } from '../utils/access'

const ADMIN_ONLY = ['ROLE_ADMIN']
const ORDER_ROLES = ['ROLE_SALES', 'ROLE_PROCUREMENT']
const SALES_ROLES = ['ROLE_SALES']
const PROCUREMENT_ROLES = ['ROLE_PROCUREMENT']
const WAREHOUSE_ROLES = ['ROLE_WAREHOUSE']
const FINANCE_ROLES = ['ROLE_FINANCE']
const REPORT_ROLES = ['ROLE_SALES', 'ROLE_PROCUREMENT', 'ROLE_WAREHOUSE', 'ROLE_FINANCE']

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            redirect: '/dashboard'
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/LoginView.vue')
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/RegisterView.vue')
        },
        {
            path: '/dashboard',
            name: 'dashboard',
            component: () => import('../views/DashboardView.vue'),
            redirect: '/dashboard/home',
            meta: { requiresAuth: true },
            children: [
                {
                    path: 'home',
                    name: 'DashboardHome',
                    component: () => import('../views/HomeView.vue')
                },
                // 系统管理
                {
                    path: 'users',
                    name: 'UserList',
                    component: () => import('../views/system/UserList.vue'),
                    meta: { roles: ADMIN_ONLY }
                },
                {
                    path: 'roles',
                    name: 'RoleList',
                    component: () => import('../views/system/RoleList.vue'),
                    meta: { roles: ADMIN_ONLY }
                },
                {
                    path: 'permissions',
                    name: 'PermissionList',
                    component: () => import('../views/system/PermissionList.vue'),
                    meta: { roles: ADMIN_ONLY }
                },
                {
                    path: 'logs',
                    name: 'LoginLogList',
                    component: () => import('../views/system/LoginLogList.vue'),
                    meta: { roles: ADMIN_ONLY }
                },
                // 旧订单管理（保留兼容）
                {
                    path: 'orders',
                    name: 'OrderList',
                    component: () => import('../views/order/OrderList.vue'),
                    meta: { roles: ORDER_ROLES }
                },
                {
                    path: 'orders/create',
                    name: 'OrderCreate',
                    component: () => import('../views/order/OrderCreate.vue'),
                    meta: { roles: ORDER_ROLES }
                },
                // 采购管理
                {
                    path: 'purchase/create',
                    name: 'PurchaseCreate',
                    component: () => import('../views/purchase/PurchaseCreate.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                {
                    path: 'purchase/list',
                    name: 'PurchaseList',
                    component: () => import('../views/purchase/PurchaseList.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                {
                    path: 'purchase/detail',
                    name: 'PurchaseDetail',
                    component: () => import('../views/purchase/PurchaseDetail.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                {
                    path: 'purchase/payment',
                    name: 'PurchasePayment',
                    component: () => import('../views/purchase/PaymentPage.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                {
                    path: 'purchase/invoice',
                    name: 'PurchaseInvoice',
                    component: () => import('../views/purchase/PurchaseInvoice.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                // 销售管理
                {
                    path: 'sales/create',
                    name: 'SalesCreate',
                    component: () => import('../views/sales/SalesCreate.vue'),
                    meta: { roles: SALES_ROLES }
                },
                {
                    path: 'sales/list',
                    name: 'SalesList',
                    component: () => import('../views/sales/SalesList.vue'),
                    meta: { roles: SALES_ROLES }
                },
                {
                    path: 'sales/detail',
                    name: 'SalesDetail',
                    component: () => import('../views/sales/SalesDetail.vue'),
                    meta: { roles: SALES_ROLES }
                },
                {
                    path: 'sales/receipt',
                    name: 'SalesReceipt',
                    component: () => import('../views/sales/ReceiptPage.vue'),
                    meta: { roles: SALES_ROLES }
                },
                {
                    path: 'sales/invoice',
                    name: 'SalesInvoice',
                    component: () => import('../views/sales/SalesInvoice.vue'),
                    meta: { roles: SALES_ROLES }
                },
                // 库存管理
                {
                    path: 'inventory/products',
                    name: 'ProductList',
                    component: () => import('../views/inventory/ProductList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/records',
                    name: 'StockRecordList',
                    component: () => import('../views/inventory/StockRecordList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-in-create',
                    name: 'StockInCreate',
                    component: () => import('../views/inventory/StockInCreate.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-in-list',
                    name: 'StockInList',
                    component: () => import('../views/inventory/StockInList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-in-detail',
                    name: 'StockInDetail',
                    component: () => import('../views/inventory/StockInDetail.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-out-create',
                    name: 'StockOutCreate',
                    component: () => import('../views/inventory/StockOutCreate.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-out-list',
                    name: 'StockOutList',
                    component: () => import('../views/inventory/StockOutList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/stock-out-detail',
                    name: 'StockOutDetail',
                    component: () => import('../views/inventory/StockOutDetail.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/ledger',
                    name: 'InventoryLedger',
                    component: () => import('../views/inventory/InventoryLedger.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                // 基础档案
                {
                    path: 'inventory/categories',
                    name: 'CategoryList',
                    component: () => import('../views/inventory/CategoryList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'inventory/warehouses',
                    name: 'WarehouseList',
                    component: () => import('../views/inventory/WarehouseList.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'base/customers',
                    name: 'CustomerList',
                    component: () => import('../views/base/CustomerList.vue'),
                    meta: { roles: SALES_ROLES }
                },
                {
                    path: 'base/suppliers',
                    name: 'SupplierList',
                    component: () => import('../views/base/SupplierList.vue'),
                    meta: { roles: PROCUREMENT_ROLES }
                },
                {
                    path: 'base/finance',
                    name: 'FinanceList',
                    component: () => import('../views/base/FinanceList.vue'),
                    meta: { roles: FINANCE_ROLES }
                },
                {
                    path: 'base/products',
                    name: 'ProductManage',
                    component: () => import('../views/base/ProductManage.vue'),
                    meta: { roles: WAREHOUSE_ROLES }
                },
                {
                    path: 'report',
                    name: 'ReportDashboard',
                    component: () => import('../views/report/ReportDashboard.vue'),
                    meta: { roles: REPORT_ROLES }
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else if (to.meta.roles && !canAccessRoles(to.meta.roles as string[])) {
        ElMessage.warning('当前账号没有访问该页面的权限')
        next('/dashboard/home')
    } else {
        next()
    }
})

export default router
