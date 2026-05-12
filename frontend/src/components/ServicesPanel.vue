<template>
  <div>
    <div class="panel-head stacked-head">
      <div>
        <h3>{{ copy.title }}</h3>
        <p>{{ copy.description }}</p>
      </div>
    </div>
    <div class="service-grid">
      <article v-for="service in services" :key="service.id" class="service-card">
        <div class="avatar">{{ service.teacherName?.slice(0, 1) }}</div>
        <div>
          <h3>{{ service.teacherName }}</h3>
          <p>{{ service.introduction }}</p>
          <div class="info-lines">
            <span><strong>服务费用：</strong>{{ service.fee }}</span>
            <span><strong>服务时间：</strong>{{ service.workingTime }}</span>
            <span><strong>执教年限：</strong>{{ service.teachingYears }}</span>
            <span><strong>荣誉资质：</strong>{{ service.honor }}</span>
          </div>
          <div class="tags">
            <el-tag>{{ service.fee }}</el-tag>
            <el-tag type="success">{{ service.workingTime }}</el-tag>
            <el-tag type="warning">{{ service.teachingYears }}</el-tag>
          </div>
        </div>
        <div class="card-actions">
          <el-button v-if="mode !== 'teacherInfo'" type="primary" @click="$emit('appoint', service)">服务预约</el-button>
          <el-button v-if="mode !== 'teacherInfo'" @click="$emit('consult', service)">留言咨询</el-button>
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ services: Array, mode: String })
defineEmits(['appoint', 'consult'])

const textMap = {
  teacherInfo: {
    title: '心理老师信息',
    description: '这里展示心理老师的姓名、服务介绍、服务时间、费用、执教年限和荣誉资质。',
  },
  serviceBooking: {
    title: '服务预约',
    description: '选择合适的心理老师，点击“服务预约”填写预约时间和备注。',
  },
  userMessage: {
    title: '用户留言咨询',
    description: '选择心理老师后点击“留言咨询”，填写咨询内容即可提交在线咨询。',
  },
  services: {
    title: '心理服务',
    description: '查看心理老师服务信息，并进行预约或留言咨询。',
  },
}
const copy = computed(() => textMap[props.mode] || textMap.services)
</script>
