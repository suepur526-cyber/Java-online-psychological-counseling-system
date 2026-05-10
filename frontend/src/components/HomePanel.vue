<template>
  <div class="home-grid">
    <div class="welcome-card">
      <span class="eyebrow">{{ roleLabels[role] }}</span>
      <h3>{{ title }}</h3>
      <p>{{ desc }}</p>
    </div>
    <div class="quick-list">
      <h3>最新公告</h3>
      <div v-for="item in announcements.slice(0, 3)" :key="item.id" class="mini-row">
        <strong>{{ item.title }}</strong>
        <span>{{ item.introduction }}</span>
      </div>
    </div>
    <div class="quick-list">
      <h3>心理服务</h3>
      <div v-for="item in services.slice(0, 3)" :key="item.id" class="mini-row">
        <strong>{{ item.teacherName }}</strong>
        <span>{{ item.introduction }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { roleLabels } from '../utils/labels'

const props = defineProps({
  role: String,
  announcements: Array,
  services: Array,
})

const title = computed(() => props.role === 'USER' ? '欢迎使用心理健康服务' : props.role === 'TEACHER' ? '今日咨询工作台' : '平台综合管理中心')
const desc = computed(() => props.role === 'USER' ? '你可以完成心理测试、预约老师并提交咨询内容。' : props.role === 'TEACHER' ? '请及时处理预约申请和用户咨询回复。' : '统一查看和维护用户、老师、公告、测试与咨询业务。')
</script>
