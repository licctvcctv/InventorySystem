<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import request from '../api/request'

const router = useRouter()

const formRef = ref()
const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loading = ref(false)

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true
      // 清除旧 Token，防止干扰
      localStorage.removeItem('token')
      try {
        const res: any = await request.post('/auth/login', form)
        // 假设后端返回结构 { token: '...' }
        // 注意：根据后端实现，UserContoller 是 /api/users/login
        // request.ts baseURL 是 /api/users
        // 所以这里 post '/login' -> /api/users/login
        
        if (res.token) {
            localStorage.setItem('token', res.token)
            // roles are comma separated if multiple, but here simple string
            localStorage.setItem('role', res.role || '') 
            localStorage.setItem('fullName', res.fullName || '')
            ElMessage.success('登录成功')
            router.push('/dashboard')
        }
      } catch (e) {
        // error handled in interceptor
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<template>
  <div class="login-wrapper">
    <div class="login-left">
        <div class="login-left-content">
            <h1>进销存系统</h1>
            <p>专业的库存管理解决方案</p>
        </div>
    </div>
    <div class="login-right">
        <div class="form-container">
            <div class="welcome-text">
                <h2>欢迎登录</h2>
                <p class="sub-title">请输入您的账号密码以继续</p>
            </div>
            <el-form ref="formRef" :model="form" :rules="rules" size="large" label-position="top">
                <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password @keyup.enter="handleLogin" :prefix-icon="Lock"/>
                </el-form-item>
                <el-form-item>
                <el-button type="primary" :loading="loading" class="w-100 login-btn" @click="handleLogin">登 录</el-button>
                </el-form-item>
                <div class="footer-links">
                <span>还没有账号？</span>
                <router-link to="/register" class="link">立即注册</router-link>
                </div>
            </el-form>
        </div>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}

.login-left {
  flex: 1.2;
  background: url('@/assets/login_bg.png') no-repeat center center;
  background-size: cover;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.login-left::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.3); /* Overlay for better text readability */
}

.login-left-content {
    position: relative;
    z-index: 2;
    text-align: center;
    color: #fff;
}

.login-left-content h1 {
    font-size: 3rem;
    font-weight: 700;
    margin-bottom: 20px;
    letter-spacing: 2px;
}

.login-left-content p {
    font-size: 1.5rem;
    font-weight: 300;
    opacity: 0.9;
}

.login-right {
  flex: 0.8;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.form-container {
    width: 100%;
    max-width: 400px;
}

.welcome-text {
    margin-bottom: 40px;
}

.welcome-text h2 {
    font-size: 2rem;
    color: #333;
    margin-bottom: 10px;
    font-weight: 600;
}

.sub-title {
    color: #909399;
    font-size: 1rem;
}

.w-100 {
  width: 100%;
}

.login-btn {
    padding: 20px 0;
    font-size: 1.1rem;
    letter-spacing: 1px;
    font-weight: bold;
}

.footer-links {
  margin-top: 20px;
  text-align: center;
  font-size: 0.95rem;
  color: #606266;
}

.link {
    color: #409EFF;
    text-decoration: none;
    margin-left: 5px;
    font-weight: 500;
}

.link:hover {
    text-decoration: underline;
}
</style>
