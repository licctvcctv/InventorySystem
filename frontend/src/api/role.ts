import request from './request'

// 获取角色分页列表
export const getRoleList = (params: any) => {
    return request({
        url: '/roles',
        method: 'get',
        params
    })
}

// 获取所有角色（不分页，用于下拉）
export const getAllRoles = () => {
    return request({
        url: '/roles/all',
        method: 'get'
    })
}

// 新增角色
export const addRole = (data: any) => {
    return request({
        url: '/roles',
        method: 'post',
        data
    })
}

// 更新角色
export const updateRole = (id: number, data: any) => {
    return request({
        url: `/roles/${id}`,
        method: 'put',
        data
    })
}

// 删除角色
export const deleteRole = (id: number) => {
    return request({
        url: `/roles/${id}`,
        method: 'delete'
    })
}

// 获取角色权限
export const getRolePermissions = (id: number) => {
    return request({
        url: `/roles/${id}/permissions`,
        method: 'get'
    })
}

// 分配权限
export const assignPermissions = (id: number, permissionIds: number[]) => {
    return request({
        url: `/roles/${id}/permissions`,
        method: 'post',
        data: { permissionIds }
    })
}
