<template>
  <div>
    <div class="panel-head stacked-head">
      <div>
        <h3>{{ role === 'USER' ? '在线心理测试' : '心理测试管理' }}</h3>
        <p>{{ role === 'USER' ? '选择心理测试并开始答题，系统会自动计算得分并保存测试记录。' : '维护心理测试、题库题目，并查看用户测试记录。' }}</p>
      </div>
      <el-button v-if="role !== 'USER'" type="primary" @click="openTest()">新增测试</el-button>
    </div>
    <el-table :data="tests" stripe>
      <el-table-column prop="name" label="测试名称" min-width="180" />
      <el-table-column prop="durationMinutes" label="时长" width="90" />
      <el-table-column prop="description" label="说明" min-width="240" />
      <el-table-column prop="status" label="状态" width="110" />
      <el-table-column label="操作" width="230">
        <template #default="{ row }">
          <el-button v-if="role === 'USER'" link type="primary" @click="start(row)">开始答题</el-button>
          <el-button v-else link type="primary" @click="openTest(row)">编辑</el-button>
          <el-button link @click="loadDetail(row)">题目</el-button>
        </template>
      </el-table-column>
    </el-table>

    <h3 class="sub-title">测试记录</h3>
    <el-table :data="records" stripe>
      <el-table-column prop="username" label="用户" width="120" />
      <el-table-column prop="testName" label="测试" min-width="180" />
      <el-table-column prop="totalScore" label="得分" width="90" />
      <el-table-column prop="resultLevel" label="结果" width="140">
        <template #default="{ row }"><el-tag :type="resultTone(row.resultLevel)">{{ row.resultLevel }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createdAt" label="提交时间" width="180" />
    </el-table>

    <el-dialog v-model="answerVisible" title="心理测试答题" width="680px">
      <div v-if="detail">
        <h3>{{ detail.test.name }}</h3>
        <div v-for="question in detail.questions" :key="question.id" class="question-box">
          <strong>{{ question.questionName }}</strong>
          <el-radio-group v-model="answers[question.id]">
            <el-radio v-for="option in parseOptions(question.optionsJson)" :key="option.value" :value="option.value">{{ option.label }}</el-radio>
          </el-radio-group>
        </div>
      </div>
      <template #footer>
        <el-button @click="answerVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交测试</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="testVisible" title="测试维护" width="560px">
      <el-form :model="testForm" label-position="top">
        <el-form-item label="测试名称"><el-input v-model="testForm.name" /></el-form-item>
        <el-form-item label="测试时长"><el-input-number v-model="testForm.durationMinutes" :min="5" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="testForm.description" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="testForm.status"><el-option label="启用" value="ACTIVE" /><el-option label="停用" value="DISABLED" /></el-select></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="testVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTest">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="questionVisible" title="题库维护" width="760px">
      <div class="panel-head">
        <h3>{{ questionDetail?.test?.name }}</h3>
        <el-button v-if="role !== 'USER'" type="primary" @click="openQuestion()">新增题目</el-button>
      </div>
      <el-table :data="questionDetail?.questions || []">
        <el-table-column prop="questionName" label="题目" min-width="220" />
        <el-table-column prop="optionsJson" label="选项JSON" min-width="260" show-overflow-tooltip />
        <el-table-column label="操作" width="120" v-if="role !== 'USER'">
          <template #default="{ row }"><el-button link type="primary" @click="openQuestion(row)">编辑</el-button></template>
        </el-table-column>
      </el-table>
      <el-form v-if="questionEditing" :model="questionForm" label-position="top" class="question-editor">
        <el-form-item label="题目"><el-input v-model="questionForm.questionName" /></el-form-item>
        <el-form-item label="选项JSON"><el-input v-model="questionForm.optionsJson" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="答案解析"><el-input v-model="questionForm.analysis" /></el-form-item>
        <el-button type="primary" @click="saveQuestion">保存题目</el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { testApi } from '../api/modules'
import { resultTone } from '../utils/labels'

const props = defineProps({ role: String, user: Object })
const tests = ref([])
const records = ref([])
const answerVisible = ref(false)
const testVisible = ref(false)
const questionVisible = ref(false)
const questionEditing = ref(false)
const detail = ref(null)
const questionDetail = ref(null)
const answers = reactive({})
const testForm = reactive({ id: null, name: '', durationMinutes: 20, status: 'ACTIVE', description: '' })
const questionForm = reactive({ id: null, testId: null, questionName: '', optionsJson: '[{"label":"很少","value":"A","score":1},{"label":"有时","value":"B","score":2},{"label":"经常","value":"C","score":3}]', score: 3, answer: 'A', analysis: '', type: 'SINGLE', sequenceNo: 100 })

async function load() {
  const [list, rec] = await Promise.all([
    testApi.list(props.role === 'USER'),
    testApi.records(props.role === 'USER' ? props.user.id : undefined),
  ])
  tests.value = list.data
  records.value = rec.data
}

async function start(row) {
  const response = await testApi.detail(row.id)
  detail.value = response.data
  Object.keys(answers).forEach((key) => delete answers[key])
  answerVisible.value = true
}

async function submit() {
  const response = await testApi.submit(detail.value.test.id, { userId: props.user.id, answers })
  ElMessage.success(`测试完成：${response.data.resultLevel}`)
  answerVisible.value = false
  load()
}

function parseOptions(json) {
  try { return JSON.parse(json) } catch { return [] }
}

function openTest(row) {
  Object.assign(testForm, row || { id: null, name: '', durationMinutes: 20, status: 'ACTIVE', description: '' })
  testVisible.value = true
}

async function saveTest() {
  await testApi.save(testForm)
  ElMessage.success('测试已保存')
  testVisible.value = false
  load()
}

async function loadDetail(row) {
  const response = await testApi.detail(row.id)
  questionDetail.value = response.data
  questionVisible.value = true
  questionEditing.value = false
}

function openQuestion(row) {
  Object.assign(questionForm, row || { id: null, testId: questionDetail.value.test.id, questionName: '', optionsJson: '[{"label":"很少","value":"A","score":1},{"label":"有时","value":"B","score":2},{"label":"经常","value":"C","score":3}]', score: 3, answer: 'A', analysis: '', type: 'SINGLE', sequenceNo: 100 })
  questionEditing.value = true
}

async function saveQuestion() {
  await testApi.saveQuestion(questionDetail.value.test.id, questionForm)
  ElMessage.success('题目已保存')
  await loadDetail(questionDetail.value.test)
}

onMounted(load)
</script>
