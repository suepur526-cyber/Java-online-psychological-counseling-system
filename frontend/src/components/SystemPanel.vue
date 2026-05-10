<template>
  <div class="split-panels">
    <section>
      <div class="panel-head"><h3>关于我们</h3></div>
      <el-form v-if="info" :model="info" label-position="top">
        <el-form-item label="标题"><el-input v-model="info.title" /></el-form-item>
        <el-form-item label="副标题"><el-input v-model="info.subtitle" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="info.content" type="textarea" :rows="6" /></el-form-item>
        <el-button type="primary" @click="saveInfo">保存系统介绍</el-button>
      </el-form>
    </section>
    <section>
      <div class="panel-head"><h3>系统配置</h3></div>
      <el-table :data="configs">
        <el-table-column prop="configKey" label="配置项" min-width="180" />
        <el-table-column prop="configValue" label="值" width="140" />
        <el-table-column prop="remark" label="说明" />
      </el-table>
    </section>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { commonApi } from '../api/modules'

const info = ref(null)
const configs = ref([])

async function load() {
  const [infos, cfg] = await Promise.all([commonApi.systemInfos(), commonApi.configs()])
  info.value = infos.data[0]
  configs.value = cfg.data
}

async function saveInfo() {
  await commonApi.updateSystemInfo(info.value)
  ElMessage.success('系统介绍已保存')
  load()
}

onMounted(load)
</script>
