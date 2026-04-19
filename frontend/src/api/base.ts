import request from './request'

export interface Customer {
    id?: number
    name: string
    contactPerson?: string
    phone?: string
    email?: string
    address?: string
    createdAt?: string
}

export interface Supplier {
    id?: number
    name: string
    contactPerson?: string
    phone?: string
    email?: string
    address?: string
    createdAt?: string
}

export interface FinanceRecord {
    id?: number
    type: number // 1-IN, 2-OUT
    amount: number
    category: string
    targetName?: string
    remark?: string
    createdAt?: string
    relatedOrderId?: number
    sourceOrderNo?: string
    incomeSource?: string
    expenseTarget?: string
}

// Customers
export const getCustomers = (params: any) => request({ url: '/base/customers', method: 'get', params })
export const getAllCustomers = () => request({ url: '/base/customers', method: 'get', params: { page: 1, size: 10000 } })
export const addCustomer = (data: Customer) => request({ url: '/base/customers', method: 'post', data })
export const updateCustomer = (data: Customer) => request({ url: '/base/customers', method: 'put', data })
export const deleteCustomer = (id: number) => request({ url: `/base/customers/${id}`, method: 'delete' })
export const batchDeleteCustomers = (ids: number[]) => request({ url: '/base/customers/batch-delete', method: 'post', data: ids })

// Suppliers
export const getSuppliers = (params: any) => request({ url: '/base/suppliers', method: 'get', params })
export const getAllSuppliers = () => request({ url: '/base/suppliers', method: 'get', params: { page: 1, size: 10000 } })
export const addSupplier = (data: Supplier) => request({ url: '/base/suppliers', method: 'post', data })
export const updateSupplier = (data: Supplier) => request({ url: '/base/suppliers', method: 'put', data })
export const deleteSupplier = (id: number) => request({ url: `/base/suppliers/${id}`, method: 'delete' })
export const batchDeleteSuppliers = (ids: number[]) => request({ url: '/base/suppliers/batch-delete', method: 'post', data: ids })

// Finance
export const getFinance = (params: any) => request({ url: '/base/finance', method: 'get', params })
export const addFinance = (data: FinanceRecord) => request({ url: '/base/finance', method: 'post', data })
export const updateFinance = (data: FinanceRecord) => request({ url: '/base/finance', method: 'put', data })
export const deleteFinance = (id: number) => request({ url: `/base/finance/${id}`, method: 'delete' })
export const batchDeleteFinance = (ids: number[]) => request({ url: '/base/finance/batch-delete', method: 'post', data: ids })
