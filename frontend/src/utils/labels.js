export const roleLabels = {
  ADMIN: '管理员',
  USER: '普通用户',
  TEACHER: '心理老师',
}

export const statusLabels = {
  ACTIVE: '正常',
  DISABLED: '禁用',
  PENDING: '待审核',
  APPROVED: '已通过',
  REJECTED: '已驳回',
  REPLIED: '已回复',
  PUBLISHED: '已发布',
  DRAFT: '草稿',
  UNPAID: '未支付',
}

export function labelOf(map, key) {
  return map[key] || key || '-'
}

export function resultTone(level) {
  if (level?.includes('良好')) return 'success'
  if (level?.includes('轻度')) return 'warning'
  return 'danger'
}
