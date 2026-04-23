export const ADMIN_ROLE = 'ROLE_ADMIN'

export const normalizeRole = (role?: string | null) => role || ''

export const isAdminRole = (role?: string | null) => {
    const normalized = normalizeRole(role)
    return normalized === ADMIN_ROLE || normalized === 'admin'
}

export const hasAnyRole = (role: string | undefined | null, roles: string[]) => {
    const normalized = normalizeRole(role)
    return isAdminRole(normalized) || roles.includes(normalized)
}

const getRoleFromToken = () => {
    const token = localStorage.getItem('token')
    if (!token) return ''
    try {
        const payload = JSON.parse(window.atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')))
        const roles = payload.roles
        return Array.isArray(roles) ? roles[0] || '' : ''
    } catch {
        return ''
    }
}

export const getCurrentRole = () => localStorage.getItem('role') || getRoleFromToken()

export const canAccessRoles = (roles?: string[]) => {
    if (!roles || roles.length === 0) return true
    return hasAnyRole(getCurrentRole(), roles)
}
