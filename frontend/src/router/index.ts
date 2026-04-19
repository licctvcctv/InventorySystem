import { createRouter, createWebHistory } from 'vue-router'

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
                    component: () => import('../views/system/UserList.vue')
                },
                {
                    path: 'roles',
                    name: 'RoleList',
                    component: () => import('../views/system/RoleList.vue')
                },
                {
                    path: 'permissions',
                    name: 'PermissionList',
                    component: () => import('../views/system/PermissionList.vue')
                },
                {
                    path: 'logs',
                    name: 'LoginLogList',
                    component: () => import('../views/system/LoginLogList.vue')
                },
                // 旧订单管理（保留兼容）
                {
                    path: 'orders',
                    name: 'OrderList',
                    component: () => import('../views/order/OrderList.vue')
                },
                {
                    path: 'orders/create',
                    name: 'OrderCreate',
                    component: () => import('../views/order/OrderCreate.vue')
                },
                // 采购管理
                {
                    path: 'purchase/create',
                    name: 'PurchaseCreate',
                    component: () => import('../views/purchase/PurchaseCreate.vue')
                },
                {
                    path: 'purchase/list',
                    name: 'PurchaseList',
                    component: () => import('../views/purchase/PurchaseList.vue')
                },
                {
                    path: 'purchase/detail',
                    name: 'PurchaseDetail',
                    component: () => import('../views/purchase/PurchaseDetail.vue')
                },
                {
                    path: 'purchase/payment',
                    name: 'PurchasePayment',
                    component: () => import('../views/purchase/PaymentPage.vue')
                },
                {
                    path: 'purchase/invoice',
                    name: 'PurchaseInvoice',
                    component: () => import('../views/purchase/PurchaseInvoice.vue')
                },
                // 销售管理
                {
                    path: 'sales/create',
                    name: 'SalesCreate',
                    component: () => import('../views/sales/SalesCreate.vue')
                },
                {
                    path: 'sales/list',
                    name: 'SalesList',
                    component: () => import('../views/sales/SalesList.vue')
                },
                {
                    path: 'sales/detail',
                    name: 'SalesDetail',
                    component: () => import('../views/sales/SalesDetail.vue')
                },
                {
                    path: 'sales/receipt',
                    name: 'SalesReceipt',
                    component: () => import('../views/sales/ReceiptPage.vue')
                },
                {
                    path: 'sales/invoice',
                    name: 'SalesInvoice',
                    component: () => import('../views/sales/SalesInvoice.vue')
                },
                // 库存管理
                {
                    path: 'inventory/products',
                    name: 'ProductList',
                    component: () => import('../views/inventory/ProductList.vue')
                },
                {
                    path: 'inventory/records',
                    name: 'StockRecordList',
                    component: () => import('../views/inventory/StockRecordList.vue')
                },
                {
                    path: 'inventory/stock-in-create',
                    name: 'StockInCreate',
                    component: () => import('../views/inventory/StockInCreate.vue')
                },
                {
                    path: 'inventory/stock-in-list',
                    name: 'StockInList',
                    component: () => import('../views/inventory/StockInList.vue')
                },
                {
                    path: 'inventory/stock-in-detail',
                    name: 'StockInDetail',
                    component: () => import('../views/inventory/StockInDetail.vue')
                },
                {
                    path: 'inventory/stock-out-create',
                    name: 'StockOutCreate',
                    component: () => import('../views/inventory/StockOutCreate.vue')
                },
                {
                    path: 'inventory/stock-out-list',
                    name: 'StockOutList',
                    component: () => import('../views/inventory/StockOutList.vue')
                },
                {
                    path: 'inventory/stock-out-detail',
                    name: 'StockOutDetail',
                    component: () => import('../views/inventory/StockOutDetail.vue')
                },
                {
                    path: 'inventory/ledger',
                    name: 'InventoryLedger',
                    component: () => import('../views/inventory/InventoryLedger.vue')
                },
                // 基础档案
                {
                    path: 'inventory/categories',
                    name: 'CategoryList',
                    component: () => import('../views/inventory/CategoryList.vue')
                },
                {
                    path: 'inventory/warehouses',
                    name: 'WarehouseList',
                    component: () => import('../views/inventory/WarehouseList.vue')
                },
                {
                    path: 'base/customers',
                    name: 'CustomerList',
                    component: () => import('../views/base/CustomerList.vue')
                },
                {
                    path: 'base/suppliers',
                    name: 'SupplierList',
                    component: () => import('../views/base/SupplierList.vue')
                },
                {
                    path: 'base/finance',
                    name: 'FinanceList',
                    component: () => import('../views/base/FinanceList.vue')
                },
                {
                    path: 'base/products',
                    name: 'ProductManage',
                    component: () => import('../views/base/ProductManage.vue')
                },
                {
                    path: 'report',
                    name: 'ReportDashboard',
                    component: () => import('../views/report/ReportDashboard.vue')
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/login')
    } else {
        next()
    }
})

export default router
