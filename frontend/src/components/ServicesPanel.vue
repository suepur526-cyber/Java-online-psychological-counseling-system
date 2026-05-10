<template>
  <div class="service-grid">
    <article v-for="service in services" :key="service.id" class="service-card">
      <div class="avatar">{{ service.teacherName?.slice(0, 1) }}</div>
      <div>
        <h3>{{ service.teacherName }}</h3>
        <p>{{ service.introduction }}</p>
        <div class="tags">
          <el-tag>{{ service.fee }}</el-tag>
          <el-tag type="success">{{ service.workingTime }}</el-tag>
          <el-tag type="warning">{{ service.teachingYears }}</el-tag>
        </div>
        <small>{{ service.honor }}</small>
      </div>
      <div class="card-actions">
        <el-button type="primary" @click="$emit('appoint', service)">预约</el-button>
        <el-button @click="$emit('consult', service)">咨询</el-button>
      </div>
    </article>
  </div>
</template>

<script setup>
defineProps({ services: Array })
defineEmits(['appoint', 'consult'])
</script>
