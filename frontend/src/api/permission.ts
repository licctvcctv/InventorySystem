import request from './request'

// 获取权限树
export const getPermissionTree = () => {
    return request({
        url: '/permissions/tree',
        method: 'get'
    })
}

// 获取所有权限（平铺）
export const getAllPermissions = () => {
    return request({
        url: '/permissions',
        method: 'get'
    })
}

// 新增权限
export const addPermission = (data: any) => {
    return request({
        url: '/permissions',
        method: 'post',
        data
    })
}

// 更新权限
export const updatePermission = (id: number, data: any) => {
    return request({
        url: `/permissions/${id}`,
        method: 'put',
        data
    })
}

// 删除权限
export const deletePermission = (id: number) => {
    return request({
        url: `/permissions/${id}`,
        method: 'delete'
    })
}
