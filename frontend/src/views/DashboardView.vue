<template>
  <el-container class="app-shell">
    <el-aside width="252px" class="sidebar">
      <div class="logo">
        <div class="logo-icon">心</div>
        <div>
          <strong>心理咨询系统</strong>
          <span>{{ roleLabels[auth.role] }}</span>
        </div>
      </div>
      <el-menu :default-active="active" @select="active = $event">
        <el-menu-item v-for="item in menus" :key="item.key" :index="item.key">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="topbar">
        <div>
          <h2>{{ currentMenu?.label }}</h2>
          <p>{{ pageSubtitle }}</p>
        </div>
        <div class="user-chip">
          <span>{{ auth.user?.name }}</span>
          <el-tag>{{ roleLabels[auth.role] }}</el-tag>
          <el-button @click="logout">退出</el-button>
        </div>
      </el-header>

      <el-main class="main">
        <section class="stats-grid">
          <div class="stat-card">
            <span>公告</span><strong>{{ announcements.length }}</strong>
          </div>
          <div class="stat-card">
            <span>心理老师</span><strong>{{ teachers.length }}</strong>
          </div>
          <div class="stat-card">
            <span>预约</span><strong>{{ appointments.length }}</strong>
          </div>
          <div class="stat-card">
            <span>咨询</span><strong>{{ consultations.length }}</strong>
          </div>
        </section>

        <section class="content-surface">
          <AnnouncementsPanel v-if="active === 'announcements'" :role="auth.role" :items="announcements" @refresh="loadAll" />
          <TestsPanel v-else-if="active === 'tests'" :role="auth.role" :user="auth.user" @refresh="loadAll" />
          <ServicesPanel v-else-if="active === 'services'" :services="services" @appoint="openAppointment" @consult="openConsultation" />
          <AppointmentsPanel v-else-if="active === 'appointments'" :role="auth.role" :items="appointments" @reviewed="loadAll" />
          <ConsultationsPanel v-else-if="active === 'consultations'" :role="auth.role" :items="consultations" :replies="replies" @replied="loadAll" />
          <UsersPanel v-else-if="active === 'users'" :users="users" :teachers="teachers" @refresh="loadAll" />
          <SystemPanel v-else-if="active === 'system'" />
          <HomePanel v-else :role="auth.role" :announcements="announcements" :services="services" />
        </section>
      </el-main>
    </el-container>
  </el-container>

  <el-dialog v-model="appointmentVisible" title="提交预约" width="520px">
    <el-form :model="appointmentForm" label-position="top">
      <el-form-item label="预约老师"><el-input v-model="appointmentForm.teacherName" disabled /></el-form-item>
      <el-form-item label="预约时间"><el-date-picker v-model="appointmentForm.appointmentTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="full" /></el-form-item>
      <el-form-item label="预约备注"><el-input v-model="appointmentForm.note" type="textarea" :rows="4" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="appointmentVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAppointment">提交</el-button>
    </template>
  </el-dialog>

  <el-dialog v-model="consultVisible" title="提交咨询" width="520px">
    <el-form :model="consultForm" label-position="top">
      <el-form-item label="咨询老师"><el-input v-model="consultForm.teacherName" disabled /></el-form-item>
      <el-form-item label="咨询内容"><el-input v-model="consultForm.content" type="textarea" :rows="6" /></el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="consultVisible = false">取消</el-button>
      <el-button type="primary" @click="submitConsultation">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Bell, Calendar, ChatLineRound, DataAnalysis, House, Setting, User, UserFilled } from '@element-plus/icons-vue'
import { useAuthStore } from '../stores/auth'
import { appointmentApi, commonApi, consultationApi } from '../api/modules'
import { roleLabels } from '../utils/labels'
import HomePanel from '../components/HomePanel.vue'
import AnnouncementsPanel from '../components/AnnouncementsPanel.vue'
import TestsPanel from '../components/TestsPanel.vue'
import ServicesPanel from '../components/ServicesPanel.vue'
import AppointmentsPanel from '../components/AppointmentsPanel.vue'
import ConsultationsPanel from '../components/ConsultationsPanel.vue'
import UsersPanel from '../components/UsersPanel.vue'
import SystemPanel from '../components/SystemPanel.vue'

const router = useRouter()
const auth = useAuthStore()
const active = ref('home')
const announcements = ref([])
const teachers = ref([])
const services = ref([])
const users = ref([])
const appointments = ref([])
const consultations = ref([])
const replies = ref([])
const appointmentVisible = ref(false)
const consultVisible = ref(false)
const appointmentForm = reactive({ teacherId: null, teacherName: '', appointmentTime: '', note: '' })
const consultForm = reactive({ teacherId: null, teacherName: '', content: '' })

const menuSets = {
  USER: [
    ['home', '首页', House], ['announcements', '公告信息', Bell], ['services', '心理服务', UserFilled],
    ['tests', '在线测试', DataAnalysis], ['appointments', '我的预约', Calendar], ['consultations', '咨询与回复', ChatLineRound],
  ],
  TEACHER: [
    ['home', '工作台', House], ['appointments', '预约审核', Calendar], ['consultations', '咨询回复', ChatLineRound],
    ['tests', '测试管理', DataAnalysis], ['announcements', '公告查看', Bell],
  ],
  ADMIN: [
    ['home', '管理首页', House], ['users', '用户与老师', User], ['announcements', '公告管理', Bell],
    ['tests', '测试管理', DataAnalysis], ['appointments', '预约管理', Calendar], ['consultations', '咨询管理', ChatLineRound], ['system', '系统管理', Setting],
  ],
}

const menus = computed(() => menuSets[auth.role].map(([key, label, icon]) => ({ key, label, icon })))
const currentMenu = computed(() => menus.value.find((item) => item.key === active.value))
const pageSubtitle = computed(() => auth.role === 'USER' ? '完成测试、预约与咨询流程' : auth.role === 'TEACHER' ? '处理预约、咨询回复与测试维护' : '统一管理平台业务数据')

async function loadAll() {
  const [ann, tea, svc, userRes, appt, cons, reply] = await Promise.all([
    commonApi.announcements(auth.role !== 'ADMIN'),
    commonApi.teachers(auth.role === 'USER'),
    commonApi.services(),
    auth.role === 'ADMIN' ? commonApi.users() : Promise.resolve({ data: [] }),
    appointmentApi.list(auth.role, auth.user?.id),
    consultationApi.list(auth.role, auth.user?.id),
    consultationApi.replies(auth.role === 'USER' ? auth.user?.id : undefined),
  ])
  announcements.value = ann.data
  teachers.value = tea.data
  services.value = svc.data
  users.value = userRes.data
  appointments.value = appt.data
  consultations.value = cons.data
  replies.value = reply.data
}

function openAppointment(service) {
  appointmentForm.teacherId = service.teacherId
  appointmentForm.teacherName = service.teacherName
  appointmentForm.appointmentTime = ''
  appointmentForm.note = ''
  appointmentVisible.value = true
}

function openConsultation(service) {
  consultForm.teacherId = service.teacherId
  consultForm.teacherName = service.teacherName
  consultForm.content = ''
  consultVisible.value = true
}

async function submitAppointment() {
  await appointmentApi.submit({ userId: auth.user.id, teacherId: appointmentForm.teacherId, appointmentTime: appointmentForm.appointmentTime, note: appointmentForm.note })
  ElMessage.success('预约提交成功')
  appointmentVisible.value = false
  loadAll()
}

async function submitConsultation() {
  await consultationApi.submit({ userId: auth.user.id, teacherId: consultForm.teacherId, content: consultForm.content })
  ElMessage.success('咨询提交成功')
  consultVisible.value = false
  loadAll()
}

function logout() {
  auth.logout()
  router.push('/login')
}

onMounted(loadAll)
</script>
