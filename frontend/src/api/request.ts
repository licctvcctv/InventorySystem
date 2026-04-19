import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
    baseURL: '/api', // Vite 代理前缀
    timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token')
        const url = config.url || ''
        // 排除登录和注册接口 (使用更精确的匹配，防止误伤 login-logs)
        if (token && !url.includes('/auth/login') && !url.includes('/auth/register')) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        if (error.response) {
            if (error.response.status === 401) {
                localStorage.removeItem('token')
                router.push('/login')
                ElMessage.error('认证失效，请重新登录')
            } else if (error.response.status === 403) {
                ElMessage.error('Access Denied: 您没有权限执行此操作')
            } else {
                ElMessage.error(error.response.data?.message || '请求失败')
            }
        } else {
            ElMessage.error('网络错误')
        }
        return Promise.reject(error)
    }
)

export default request
