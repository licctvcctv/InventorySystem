import request from './request'

export interface OrderItem {
    id?: number
    productId?: number
    productName: string
    productAttr?: string
    unit?: string
    quantity: number
    price: number
    costPrice?: number
    amount?: number
    costAmount?: number
    stockQuantity?: number
}

export interface Order {
    id?: number
    orderNo?: string
    type: string
    totalAmount?: number
    status?: number
    description: string
    createdBy?: string
    createdAt?: string
    customerId?: number
    supplierId?: number
    warehouseId?: number
    invoiceType?: string
    salesman?: string
    dealAmount?: number
    totalCost?: number
    orderDebt?: number
    discount?: number
    paymentStatus?: number
    customerAddress?: string
    customerPaymentInfo?: string
    items: OrderItem[]
    supplierName?: string
    customerName?: string
    warehouseName?: string
}

export interface PaymentRecord {
    id?: number
    paymentDate?: string
    paymentNo?: string
    sourceOrderNo?: string
    sourceOrderId?: number
    settlementMethod?: string
    amountDue?: number
    amountPaid?: number
    paymentPerson?: string
    payee?: string
    isFullyPaid?: boolean
    remark?: string
    type?: string // PAYMENT or RECEIPT
    payer?: string
    createdAt?: string
}

export const getOrders = (params: any) => request({ url: '/orders', method: 'get', params })
export const getOrder = (id: number) => request({ url: `/orders/${id}`, method: 'get' })
export const createOrder = (data: Order) => request({ url: '/orders', method: 'post', data })
export const updateOrder = (data: Order) => request({ url: '/orders', method: 'put', data })
export const deleteOrder = (id: number) => request({ url: `/orders/${id}`, method: 'delete' })
export const batchDeleteOrders = (ids: number[]) => request({ url: '/orders/batch-delete', method: 'post', data: ids })
export const updatePaymentStatus = (id: number, status: number) =>
    request({ url: `/orders/${id}/payment`, method: 'put', params: { status } })
export const updateInvoiceType = (id: number, invoiceType: string) =>
    request({ url: `/orders/${id}/invoice`, method: 'put', params: { invoiceType } })
export const getCustomerDebt = (params?: any) =>
    request({ url: '/orders/customer-debt', method: 'get', params })

// Payment records
export const getPayments = (params: any) => request({ url: '/orders/payments', method: 'get', params })
export const createPayment = (data: PaymentRecord) => request({ url: '/orders/payments', method: 'post', data })

// Receipt records
export const getReceipts = (params: any) => request({ url: '/orders/receipts', method: 'get', params })
export const createReceipt = (data: PaymentRecord) => request({ url: '/orders/receipts', method: 'post', data })
