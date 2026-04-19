<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

const username = ref('')
const role = ref('')
const currentTime = ref('')

// 获取问候语
const getGreeting = () => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
}

const greeting = ref(getGreeting())

const updateTime = () => {
    const now = new Date()
    currentTime.value = now.toLocaleString('zh-CN', {
        year: 'numeric',
        month: 'long', 
        day: 'numeric',
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit',
        weekday: 'long'
    })
}

// 角色名映射
const roleName = computed(() => {
    if (role.value === 'ROLE_ADMIN' || role.value === 'admin') {
        return '系统管理员'
    } else if (role.value === 'ROLE_USER' || role.value === 'user') {
        return '普通用户'
    }
    return role.value
})

import request from '../api/request'

// ... (existing code)

onMounted(() => {
    updateTime()
    setInterval(updateTime, 1000)

    const savedName = localStorage.getItem('fullName')
    if (savedName) {
        username.value = savedName
    }
    
    // 自动刷新逻辑
    const token = localStorage.getItem('token')
    if (token) {
        try {
                const base64Url = token.split('.')[1]
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
                const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
                }).join(''))
                const payload = JSON.parse(jsonPayload)
                const sub = payload.sub || 'User'
                
                if (!savedName) {
                    username.value = sub
                }

                request.get('/users', { params: { username: sub, page: 1, size: 1 } })
                .then((res: any) => {
                    if (res && res.records && res.records.length > 0) {
                        const user = res.records[0]
                        if (user.fullName) {
                            username.value = user.fullName
                            localStorage.setItem('fullName', user.fullName) 
                            localStorage.setItem('role', user.role)
                            // Update role ref if it was default
                            role.value = user.role
                        }
                    }
                })
        } catch (e) {
            username.value = 'User'
        }
    }
    role.value = localStorage.getItem('role') || '用户'
})
</script>

<template>
  <div class="home-container">
    <el-card class="welcome-card" shadow="hover">
      <div class="welcome-header">
        <div class="avatar-section">
            <el-avatar :size="80" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
        </div>
        <div class="info-section">
            <h1 class="welcome-title">{{ greeting }}，{{ username }}！</h1>
            <p class="welcome-subtitle">欢迎使用进销存管理系统 | 当前角色：<el-tag size="small" effect="dark">{{ roleName }}</el-tag></p>
            <p class="current-time">{{ currentTime }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.home-container {
    padding: 10px;
}

.welcome-card {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    border: none;
    margin-bottom: 20px;
}

.welcome-header {
    display: flex;
    align-items: center;
    gap: 30px;
    padding: 20px;
}

.info-section {
    flex: 1;
}

.welcome-title {
    font-size: 28px;
    color: #303133;
    margin-bottom: 10px;
    font-weight: 600;
}

.welcome-subtitle {
    font-size: 16px;
    color: #606266;
    margin-bottom: 5px;
}

.current-time {
    font-size: 14px;
    color: #909399;
    margin-top: 10px;
}
</style>
