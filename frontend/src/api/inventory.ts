import request from './request'

// 商品相关
export const getProducts = (params: any) => request({ url: '/products', method: 'get', params })
export const getAllProducts = () => request({ url: '/products/all', method: 'get' })
export const createProduct = (data: any) => request({ url: '/products', method: 'post', data })
export const updateProduct = (data: any) => request({ url: '/products', method: 'put', data })
export const deleteProduct = (id: number) => request({ url: `/products/${id}`, method: 'delete' })
export const batchDeleteProducts = (ids: number[]) => request({ url: '/products/batch-delete', method: 'post', data: ids })

// 库存操作
export const stockIn = (data: any) => request({ url: '/stock/in', method: 'post', data })
export const stockOut = (data: any) => request({ url: '/stock/out', method: 'post', data })
export const getStockRecords = (params: any) => request({ url: '/stock/records', method: 'get', params })
export const getStockRecord = (id: number) => request({ url: `/stock/${id}`, method: 'get' })
export const updateStockRecord = (data: any) => request({ url: '/stock', method: 'put', data })
export const deleteStockRecord = (id: number) => request({ url: `/stock/${id}`, method: 'delete' })
export const batchDeleteStockRecords = (ids: number[]) => request({ url: '/stock/batch-delete', method: 'post', data: ids })

// 库存台账
export const getInventoryLedger = (params: any) => request({ url: '/inventory-ledger', method: 'get', params })
export const createInventoryLedger = (data: any) => request({ url: '/inventory-ledger', method: 'post', data })
export const updateInventoryLedger = (data: any) => request({ url: '/inventory-ledger', method: 'put', data })
export const deleteInventoryLedger = (id: number) => request({ url: `/inventory-ledger/${id}`, method: 'delete' })
export const batchDeleteInventoryLedger = (ids: number[]) => request({ url: '/inventory-ledger/batch-delete', method: 'post', data: ids })

// Extra: Categories
export const getCategories = () => request({ url: '/extra/categories', method: 'get' })
export const addCategory = (data: any) => request({ url: '/extra/categories', method: 'post', data })
export const deleteCategory = (id: number) => request({ url: `/extra/categories/${id}`, method: 'delete' })
export const batchDeleteCategories = (ids: number[]) => request({ url: '/extra/categories/batch-delete', method: 'post', data: ids })

// Extra: Warehouses
export const getWarehouses = () => request({ url: '/extra/warehouses', method: 'get' })
export const addWarehouse = (data: any) => request({ url: '/extra/warehouses', method: 'post', data })
export const deleteWarehouse = (id: number) => request({ url: `/extra/warehouses/${id}`, method: 'delete' })
export const batchDeleteWarehouses = (ids: number[]) => request({ url: '/extra/warehouses/batch-delete', method: 'post', data: ids })
