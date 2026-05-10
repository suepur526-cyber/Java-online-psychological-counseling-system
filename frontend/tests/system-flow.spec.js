import { expect, test } from '@playwright/test'

async function login(page, role, account, password) {
  await page.goto('/login')
  await page.getByTestId('login-role').getByText(role, { exact: true }).click()
  await page.getByPlaceholder('admin / 2022001 / T001').fill(account)
  await page.getByPlaceholder('默认 123456，管理员 admin123').fill(password)
  await page.getByTestId('login-submit').click()
  await expect(page).toHaveURL(/\/dashboard$/)
  await expect(page.getByTestId('app-sidebar')).toContainText(role === '普通用户' ? '心理服务' : role === '心理老师' ? '预约审核' : '公告管理')
}

async function openMenu(page, label) {
  await page.getByTestId('app-sidebar').getByText(label, { exact: true }).click()
}

test('student can view services, submit appointment, consultation and test', async ({ page }) => {
  await login(page, '普通用户', '2022001', '123456')
  await openMenu(page, '心理服务')
  await expect(page.getByText('陈老师')).toBeVisible()
  const chenService = page.locator('.service-card').filter({ hasText: '陈老师' })
  await chenService.getByRole('button', { name: '预约' }).click()
  await page.getByLabel('预约时间').fill('2026-06-01 15:00:00')
  await page.getByLabel('预约备注').fill('端到端测试预约')
  await page.getByRole('button', { name: '提交' }).click()
  await expect(page.getByText('预约提交成功')).toBeVisible()

  await chenService.getByRole('button', { name: '咨询' }).click()
  await page.getByLabel('咨询内容').fill('端到端测试咨询内容')
  await page.getByRole('button', { name: '提交' }).click()
  await expect(page.getByText('咨询提交成功')).toBeVisible()

  await openMenu(page, '在线测试')
  await page.getByRole('row', { name: /大学生心理压力自评量表/ }).getByRole('button', { name: '开始答题' }).click()
  const answerDialog = page.getByRole('dialog', { name: '心理测试答题' })
  await answerDialog.getByText('很少', { exact: true }).click()
  await answerDialog.getByText('能', { exact: true }).click()
  await answerDialog.getByText('愿意', { exact: true }).click()
  await page.getByRole('button', { name: '提交测试' }).click()
  await expect(page.getByText('测试完成')).toBeVisible()
})

test('teacher can review appointment and reply consultation', async ({ page }) => {
  await login(page, '心理老师', 'T001', '123456')
  await openMenu(page, '预约审核')
  await expect(page.getByText('端到端测试预约')).toBeVisible()
  await page.getByText('通过').last().click()
  await page.getByRole('button', { name: 'OK' }).click()
  await expect(page.getByText('审核完成')).toBeVisible()

  await openMenu(page, '咨询回复')
  await expect(page.getByText('端到端测试咨询内容')).toBeVisible()
  await page.getByRole('row', { name: /端到端测试咨询内容/ }).getByRole('button', { name: '回复' }).click()
  await page.getByRole('button', { name: 'OK' }).click()
  await expect(page.getByText('回复成功')).toBeVisible()
})

test('admin can manage announcement and approve teacher/user statuses', async ({ page }) => {
  await login(page, '管理员', 'admin', 'admin123')
  await openMenu(page, '公告管理')
  await page.getByRole('button', { name: '新增公告' }).click()
  await page.getByLabel('标题').fill('端到端测试公告')
  await page.getByLabel('简介').fill('端到端测试简介')
  await page.getByLabel('内容').fill('端到端测试内容')
  await page.getByRole('button', { name: '保存' }).click()
  await expect(page.getByText('公告已保存')).toBeVisible()

  await openMenu(page, '用户与老师')
  await expect(page.getByText('普通用户管理')).toBeVisible()
  await expect(page.getByText('心理老师审核')).toBeVisible()
})
