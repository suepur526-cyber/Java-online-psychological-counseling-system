import { http } from './http'

export const authApi = {
  login: (data) => http.post('/auth/login', data),
  registerUser: (data) => http.post('/auth/register/user', data),
  registerTeacher: (data) => http.post('/auth/register/teacher', data),
}

export const commonApi = {
  announcements: (publicOnly = false) => http.get('/announcements', { params: { publicOnly } }),
  saveAnnouncement: (data) => data.id ? http.put(`/announcements/${data.id}`, data) : http.post('/announcements', data),
  deleteAnnouncement: (id) => http.delete(`/announcements/${id}`),
  teachers: (approvedOnly = false) => http.get('/teachers', { params: { approvedOnly } }),
  updateTeacherStatus: (id, status) => http.put(`/teachers/${id}/status`, null, { params: { status } }),
  services: () => http.get('/services'),
  users: () => http.get('/users'),
  updateUserStatus: (id, status) => http.put(`/users/${id}/status`, null, { params: { status } }),
  systemInfos: () => http.get('/system/infos'),
  updateSystemInfo: (data) => http.put(`/system/infos/${data.id}`, data),
  configs: () => http.get('/system/configs'),
}

export const testApi = {
  list: (activeOnly = false) => http.get('/tests', { params: { activeOnly } }),
  detail: (id) => http.get(`/tests/${id}`),
  save: (data) => data.id ? http.put(`/tests/${data.id}`, data) : http.post('/tests', data),
  saveQuestion: (testId, data) => data.id ? http.put(`/tests/questions/${data.id}`, data) : http.post(`/tests/${testId}/questions`, data),
  deleteQuestion: (id) => http.delete(`/tests/questions/${id}`),
  submit: (testId, data) => http.post(`/tests/${testId}/submit`, data),
  records: (userId) => http.get('/tests/records', { params: userId ? { userId } : {} }),
}

export const appointmentApi = {
  list: (role, ownerId) => http.get('/appointments', { params: { role, ownerId } }),
  submit: (data) => http.post('/appointments', data),
  review: (id, data) => http.put(`/appointments/${id}/review`, data),
}

export const consultationApi = {
  list: (role, ownerId) => http.get('/consultations', { params: { role, ownerId } }),
  submit: (data) => http.post('/consultations', data),
  reply: (id, data) => http.post(`/consultations/${id}/reply`, data),
  replies: (userId) => http.get('/consultations/replies', { params: userId ? { userId } : {} }),
}
