import request from './request'

// 获取用户列表
export const getUserList = (params: any) => {
    return request({
        url: '/users',
        method: 'get',
        params
    })
}

// 获取登录日志列表
export const getLoginLogList = (params: any) => {
    return request({
        url: '/login-logs',
        method: 'get',
        params
    })
}

// 更新用户状态
export const updateUserStatus = (id: number, status: number) => {
    return request({
        url: `/users/${id}/status`,
        method: 'put',
        params: { status }
    })
}

// 新增用户
export const addUser = (data: any) => {
    return request({
        url: '/users',
        method: 'post',
        data
    })
}

// 更新用户
export const updateUser = (id: number, data: any) => {
    return request({
        url: `/users/${id}`,
        method: 'put',
        data
    })
}

// 删除用户
export const deleteUser = (id: number) => {
    return request({
        url: `/users/${id}`,
        method: 'delete'
    })
}

// 批量删除用户
export const batchDeleteUsers = (ids: number[]) => {
    return request({
        url: '/users/batch-delete',
        method: 'post',
        data: ids
    })
}
