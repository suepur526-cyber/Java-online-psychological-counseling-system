<template>
  <div>
    <div class="panel-head stacked-head">
      <div>
        <h3>{{ copy.title }}</h3>
        <p>{{ copy.description }}</p>
      </div>
    </div>
    <el-table :data="items" stripe>
      <el-table-column prop="appointmentNo" label="预约编号" width="170" />
      <el-table-column prop="studentName" label="用户" width="110" />
      <el-table-column prop="teacherName" label="老师" width="110" />
      <el-table-column prop="appointmentTime" label="预约时间" width="180" />
      <el-table-column prop="note" label="备注" min-width="180" />
      <el-table-column prop="status" label="状态" width="110">
        <template #default="{ row }"><el-tag>{{ labelOf(statusLabels, row.status) }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="reviewReply" label="审核回复" min-width="160" />
      <el-table-column v-if="role === 'TEACHER'" label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="success" @click="review(row, 'APPROVED')">通过</el-button>
          <el-button link type="danger" @click="review(row, 'REJECTED')">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { appointmentApi } from '../api/modules'
import { labelOf, statusLabels } from '../utils/labels'

const props = defineProps({ role: String, items: Array, mode: String })
const emit = defineEmits(['reviewed'])

const textMap = {
  appointments: {
    title: '我的预约',
    description: '查看自己提交的服务预约、审核状态和老师回复。',
  },
  appointmentReview: {
    title: '服务预约审核',
    description: '心理老师在这里通过或驳回用户预约；管理员可查看平台所有预约审核记录。',
  },
  appointmentQuery: {
    title: '服务预约查询',
    description: '查询预约编号、预约时间、状态、备注和审核回复。',
  },
}
const copy = computed(() => textMap[props.mode] || textMap.appointments)

async function review(row, status) {
  const { value } = await ElMessageBox.prompt('请输入审核回复', '服务预约审核', { inputValue: status === 'APPROVED' ? '预约通过，请准时参加。' : '当前时间不便，请重新预约。' })
  await appointmentApi.review(row.id, { status, reviewReply: value })
  ElMessage.success('审核完成')
  emit('reviewed')
}
</script>
