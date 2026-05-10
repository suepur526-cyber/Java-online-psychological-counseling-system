<template>
  <div>
    <div class="panel-head"><h3>预约管理</h3></div>
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
import { ElMessageBox, ElMessage } from 'element-plus'
import { appointmentApi } from '../api/modules'
import { labelOf, statusLabels } from '../utils/labels'

defineProps({ role: String, items: Array })
const emit = defineEmits(['reviewed'])

async function review(row, status) {
  const { value } = await ElMessageBox.prompt('请输入审核回复', '预约审核', { inputValue: status === 'APPROVED' ? '预约通过，请准时参加。' : '当前时间不便，请重新预约。' })
  await appointmentApi.review(row.id, { status, reviewReply: value })
  ElMessage.success('审核完成')
  emit('reviewed')
}
</script>
