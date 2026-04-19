import request from './request'

// 订单报表
export const getOrderReport = (startDate?: string, endDate?: string) => {
    return request.get('/reports/orders', { params: { startDate, endDate } })
}

// 库存报表
export const getStockReport = () => {
    return request.get('/reports/stock')
}

// 财务报表
export const getFinanceReport = (startDate?: string, endDate?: string) => {
    return request.get('/reports/finance', { params: { startDate, endDate } })
}
