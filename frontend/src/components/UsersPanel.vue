<template>
  <div class="split-panels">
    <section v-if="mode !== 'teacherAudit'">
      <h3>普通用户管理</h3>
      <p class="panel-desc">管理员在这里查看普通用户信息，并启用或禁用用户账号。</p>
      <el-table :data="users" stripe>
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }"><el-tag>{{ labelOf(statusLabels, row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button link type="warning" @click="setUser(row, row.status === 'ACTIVE' ? 'DISABLED' : 'ACTIVE')">{{ row.status === 'ACTIVE' ? '禁用' : '启用' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <section v-if="mode !== 'users'">
      <h3>心理老师信息</h3>
      <p class="panel-desc">这里展示心理老师账号信息，管理员可对待审核老师执行通过或驳回操作。</p>
      <el-table :data="teachers" stripe>
        <el-table-column prop="jobNo" label="工号" width="100" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="specialty" label="擅长领域" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }"><el-tag>{{ labelOf(statusLabels, row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="success" @click="setTeacher(row, 'APPROVED')">通过</el-button>
            <el-button link type="danger" @click="setTeacher(row, 'REJECTED')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { commonApi } from '../api/modules'
import { labelOf, statusLabels } from '../utils/labels'

defineProps({ users: Array, teachers: Array, mode: String })
const emit = defineEmits(['refresh'])

async function setUser(row, status) {
  await commonApi.updateUserStatus(row.id, status)
  ElMessage.success('用户状态已更新')
  emit('refresh')
}

async function setTeacher(row, status) {
  await commonApi.updateTeacherStatus(row.id, status)
  ElMessage.success('老师审核状态已更新')
  emit('refresh')
}
</script>
