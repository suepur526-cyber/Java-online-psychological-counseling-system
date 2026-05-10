import axios from 'axios'
import { ElMessage } from 'element-plus'

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080/api',
  timeout: 10000,
})

http.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload && payload.success === false) {
      ElMessage.error(payload.message || '请求失败')
      return Promise.reject(new Error(payload.message || '请求失败'))
    }
    return payload
  },
  (error) => {
    const message = error.response?.data?.message || error.message || '网络异常'
    ElMessage.error(message)
    return Promise.reject(error)
  },
)

export const unwrap = (response) => response?.data ?? response
