<template>
  <div>
    <div class="panel-head">
      <h3>公告信息</h3>
      <el-button v-if="role === 'ADMIN'" type="primary" @click="open()">新增公告</el-button>
    </div>
    <el-table :data="items" stripe>
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column prop="introduction" label="简介" min-width="220" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }"><el-tag>{{ labelOf(statusLabels, row.status) }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createdAt" label="发布时间" width="180" />
      <el-table-column v-if="role === 'ADMIN'" label="操作" width="170">
        <template #default="{ row }">
          <el-button link type="primary" @click="open(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="visible" title="公告维护" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.introduction" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="6" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status"><el-option label="已发布" value="PUBLISHED" /><el-option label="草稿" value="DRAFT" /></el-select></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { commonApi } from '../api/modules'
import { labelOf, statusLabels } from '../utils/labels'

defineProps({ role: String, items: Array })
const emit = defineEmits(['refresh'])
const visible = ref(false)
const form = reactive({ id: null, title: '', introduction: '', picture: '', content: '', status: 'PUBLISHED' })

function open(row) {
  Object.assign(form, row || { id: null, title: '', introduction: '', picture: '', content: '', status: 'PUBLISHED' })
  visible.value = true
}

async function save() {
  await commonApi.saveAnnouncement(form)
  ElMessage.success('公告已保存')
  visible.value = false
  emit('refresh')
}

async function remove(id) {
  await commonApi.deleteAnnouncement(id)
  ElMessage.success('公告已删除')
  emit('refresh')
}
</script>
