<template>
  <div>
    <div class="panel-head"><h3>咨询与回复</h3></div>
    <el-table :data="items" stripe>
      <el-table-column prop="studentName" label="用户" width="110" />
      <el-table-column prop="teacherName" label="老师" width="110" />
      <el-table-column prop="content" label="咨询内容" min-width="260" />
      <el-table-column prop="status" label="状态" width="110">
        <template #default="{ row }"><el-tag>{{ labelOf(statusLabels, row.status) }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="consultationDate" label="日期" width="130" />
      <el-table-column v-if="role === 'TEACHER'" label="操作" width="120">
        <template #default="{ row }">
          <el-button link type="primary" @click="reply(row)">回复</el-button>
        </template>
      </el-table-column>
    </el-table>

    <h3 class="sub-title">咨询回复记录</h3>
    <el-table :data="replies" stripe>
      <el-table-column prop="studentName" label="用户" width="110" />
      <el-table-column prop="teacherName" label="老师" width="110" />
      <el-table-column prop="consultationContent" label="咨询内容" min-width="220" />
      <el-table-column prop="replyContent" label="回复内容" min-width="260" />
      <el-table-column prop="replyDate" label="回复日期" width="130" />
    </el-table>
  </div>
</template>

<script setup>
import { ElMessageBox, ElMessage } from 'element-plus'
import { consultationApi } from '../api/modules'
import { labelOf, statusLabels } from '../utils/labels'

defineProps({ role: String, items: Array, replies: Array })
const emit = defineEmits(['replied'])

async function reply(row) {
  const { value } = await ElMessageBox.prompt('请输入咨询回复内容', '咨询回复', { inputType: 'textarea', inputValue: '建议先记录近期情绪变化，并保持规律作息。' })
  await consultationApi.reply(row.id, { replyContent: value })
  ElMessage.success('回复成功')
  emit('replied')
}
</script>
