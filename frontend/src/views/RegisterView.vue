<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const formRef = ref()
const form = reactive({
  username: '',
  password: '',
  email: '',
  phone: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loading = ref(false)

const handleRegister = async () => {
    if (!formRef.value) return
    await formRef.value.validate(async (valid: boolean) => {
        if (valid) {
            loading.value = true
            try {
                // POST /api/auth/register
                const res: any = await request.post('/auth/register', form)
                if (res.token) {
                    localStorage.setItem('token', res.token)
                    localStorage.setItem('role', res.role || '')
                    ElMessage.success('注册成功')
                    router.push('/dashboard')
                }
            } catch (e) {
                // handled
            } finally {
                loading.value = false
            }
        }
    })
}
</script>

<template>
  <div class="register-wrapper">
    <div class="register-left">
        <div class="register-left-content">
            <h1>进销存系统</h1>
            <p>开启高效管理之旅</p>
        </div>
    </div>
    <div class="register-right">
        <div class="form-container">
            <div class="welcome-text">
                <h2>创建账号</h2>
                <p class="sub-title">填写以下信息完成注册</p>
            </div>
            <el-form ref="formRef" :model="form" :rules="rules" size="large" label-width="80px" class="register-form">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="form.username" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入邮箱"/>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入电话"/>
                </el-form-item>
                
                <el-form-item class="btn-item">
                    <el-button type="primary" :loading="loading" class="w-100 register-btn" @click="handleRegister">注 册</el-button>
                </el-form-item>
                
                <div class="footer-links">
                    <span>已有账号？</span>
                    <router-link to="/login" class="link">立即登录</router-link>
                </div>
            </el-form>
        </div>
    </div>
  </div>
</template>

<style scoped>
.register-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;
  overflow: hidden;
}

.register-left {
  flex: 1.2;
  background: url('@/assets/login_bg.png') no-repeat center center; /* Use same bg or generate another */
  background-size: cover;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.register-left::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4); 
}

.register-left-content {
    position: relative;
    z-index: 2;
    text-align: center;
    color: #fff;
}

.register-left-content h1 {
    font-size: 3rem;
    font-weight: 700;
    margin-bottom: 20px;
    letter-spacing: 2px;
}

.register-left-content p {
    font-size: 1.5rem;
    font-weight: 300;
    opacity: 0.9;
}

.register-right {
  flex: 0.8;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.form-container {
    width: 100%;
    max-width: 480px;
}

.welcome-text {
    margin-bottom: 30px;
    text-align: center;
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

.register-btn {
    padding: 20px 0;
    font-size: 1.1rem;
    letter-spacing: 1px;
    font-weight: bold;
}

.footer-links {
  margin-top: 15px;
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

:deep(.el-form-item__label) {
    font-weight: 500;
}
.btn-item {
    margin-top: 30px;
    margin-bottom: 0px !important; 
}
/* override label width specific */
</style>
