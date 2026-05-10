<template>
  <main class="auth-shell">
    <section class="auth-hero">
      <div class="brand-mark">PSY</div>
      <h1>在线心理咨询系统</h1>
      <p>心理测试、咨询预约、留言咨询和后台管理一体化平台</p>
      <div class="hero-grid">
        <div><strong>3</strong><span>类角色</span></div>
        <div><strong>12</strong><span>核心模块</span></div>
        <div><strong>MySQL</strong><span>数据存储</span></div>
      </div>
    </section>

    <section class="auth-panel">
      <el-tabs v-model="mode" stretch>
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" label-position="top">
            <el-form-item label="角色" data-testid="login-role">
              <el-segmented v-model="loginForm.role" :options="roleOptions" />
            </el-form-item>
            <el-form-item label="账号">
              <el-input data-testid="login-account" v-model="loginForm.account" placeholder="admin / 2022001 / T001" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input data-testid="login-password" v-model="loginForm.password" type="password" show-password placeholder="默认 123456，管理员 admin123" />
            </el-form-item>
            <el-button data-testid="login-submit" type="primary" size="large" class="full" :loading="loading" @click="submitLogin">进入系统</el-button>
          </el-form>
          <div class="demo-accounts">
            <span>测试账号：</span>
            <button @click="fill('ADMIN', 'admin', 'admin123')">管理员</button>
            <button @click="fill('USER', '2022001', '123456')">用户</button>
            <button @click="fill('TEACHER', 'T001', '123456')">老师</button>
          </div>
        </el-tab-pane>

        <el-tab-pane label="用户注册" name="user">
          <el-form :model="userForm" label-position="top">
            <el-form-item label="学号"><el-input v-model="userForm.studentNo" /></el-form-item>
            <el-form-item label="姓名"><el-input v-model="userForm.name" /></el-form-item>
            <el-form-item label="密码"><el-input v-model="userForm.password" type="password" show-password /></el-form-item>
            <el-form-item label="手机号"><el-input v-model="userForm.phone" /></el-form-item>
            <el-button type="primary" class="full" @click="registerUser">注册普通用户</el-button>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="老师注册" name="teacher">
          <el-form :model="teacherForm" label-position="top">
            <el-form-item label="工号"><el-input v-model="teacherForm.jobNo" /></el-form-item>
            <el-form-item label="姓名"><el-input v-model="teacherForm.name" /></el-form-item>
            <el-form-item label="密码"><el-input v-model="teacherForm.password" type="password" show-password /></el-form-item>
            <el-form-item label="擅长领域"><el-input v-model="teacherForm.specialty" /></el-form-item>
            <el-button type="primary" class="full" @click="registerTeacher">提交审核</el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </section>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import { authApi } from '../api/modules'

const router = useRouter()
const auth = useAuthStore()
const mode = ref('login')
const loading = ref(false)
const roleOptions = [
  { label: '管理员', value: 'ADMIN' },
  { label: '普通用户', value: 'USER' },
  { label: '心理老师', value: 'TEACHER' },
]

const loginForm = reactive({ role: 'ADMIN', account: 'admin', password: 'admin123' })
const userForm = reactive({ studentNo: '', password: '123456', name: '', gender: '女', email: '', phone: '' })
const teacherForm = reactive({ jobNo: '', password: '123456', name: '', gender: '女', email: '', phone: '', specialty: '', qualification: '', introduction: '' })

function fill(role, account, password) {
  loginForm.role = role
  loginForm.account = account
  loginForm.password = password
}

async function submitLogin() {
  loading.value = true
  try {
    await auth.login(loginForm)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}

async function registerUser() {
  await authApi.registerUser(userForm)
  ElMessage.success('注册成功，请登录')
  mode.value = 'login'
}

async function registerTeacher() {
  await authApi.registerTeacher(teacherForm)
  ElMessage.success('老师注册成功，请等待管理员审核')
  mode.value = 'login'
}
</script>
