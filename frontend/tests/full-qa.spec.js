import { expect, test } from '@playwright/test'

const accounts = {
  admin: { role: '管理员', account: 'admin', password: 'admin123' },
  user: { role: '普通用户', account: '2022001', password: '123456' },
  teacher: { role: '心理老师', account: 'T001', password: '123456' },
}

async function login(page, { role, account, password }) {
  await page.goto('/login')
  await page.getByTestId('login-role').getByText(role, { exact: true }).click()
  await page.getByPlaceholder('admin / 2022001 / T001').fill(account)
  await page.getByPlaceholder('默认 123456，管理员 admin123').fill(password)
  await page.getByTestId('login-submit').click()
  await expect(page).toHaveURL(/\/dashboard$/)
  await expect(page.getByTestId('app-sidebar')).toContainText(role)
}

async function logout(page) {
  await page.getByRole('button', { name: '退出' }).click()
  await expect(page).toHaveURL(/\/login$/)
}

async function openMenu(page, label) {
  await page.getByTestId('app-sidebar').getByText(label, { exact: true }).click()
  await expect(page.locator('.topbar h2')).toHaveText(label)
}

async function expectNoVisualOverflow(page) {
  const overflow = await page.evaluate(() => {
    const width = document.documentElement.clientWidth
    const offenders = []
    for (const el of document.querySelectorAll('body *')) {
      const rect = el.getBoundingClientRect()
      const style = window.getComputedStyle(el)
      const isInsideScrollable = Boolean(el.closest('.el-scrollbar, .el-table, .el-table__body-wrapper, .el-table__header-wrapper'))
      if (rect.width < 1 || rect.height < 1 || style.position === 'fixed' || isInsideScrollable) continue
      if (rect.right > width + 2 || rect.left < -2) {
        offenders.push({
          tag: el.tagName.toLowerCase(),
          text: (el.textContent || '').trim().slice(0, 60),
          left: Math.round(rect.left),
          right: Math.round(rect.right),
          width: Math.round(rect.width),
        })
      }
    }
    return offenders.slice(0, 10)
  })
  expect(overflow).toEqual([])
}

async function expectDialogInsideViewport(page, title) {
  await expect(page.getByRole('dialog', { name: title })).toBeVisible()
  const messageBox = page.locator('.el-message-box').filter({ hasText: title }).last()
  const standardDialog = page.locator('.el-dialog').filter({ hasText: title }).last()
  const dialogPanel = await messageBox.count() ? messageBox : standardDialog
  await expect(dialogPanel).toBeVisible()
  const box = await dialogPanel.boundingBox()
  const viewport = page.viewportSize()
  expect(box).toBeTruthy()
  expect(box.x).toBeGreaterThanOrEqual(0)
  expect(box.y).toBeGreaterThanOrEqual(0)
  expect(box.x + box.width).toBeLessThanOrEqual(viewport.width)
  expect(box.y + box.height).toBeLessThanOrEqual(viewport.height)
}

test.describe.serial('full functional QA', () => {
  test('login and registration pages render and submit correctly', async ({ page }) => {
    await page.goto('/login')
    await expect(page.getByRole('heading', { name: '在线心理咨询系统' })).toBeVisible()
    await expect(page.getByText('心理测试、咨询预约、留言咨询和后台管理一体化平台')).toBeVisible()
    await expectNoVisualOverflow(page)

    const suffix = Date.now().toString().slice(-6)
    await page.getByRole('tab', { name: '用户注册' }).click()
    const userPanel = page.getByRole('tabpanel', { name: '用户注册' })
    await userPanel.getByLabel('学号').fill(`QA${suffix}`)
    await userPanel.getByLabel('姓名').fill('验收用户')
    await userPanel.getByLabel('密码').fill('123456')
    await userPanel.getByLabel('手机号').fill('13812345678')
    await page.getByRole('button', { name: '注册普通用户' }).click()
    await expect(page.getByText('注册成功，请登录')).toBeVisible()

    await page.getByRole('tab', { name: '老师注册' }).click()
    const teacherPanel = page.getByRole('tabpanel', { name: '老师注册' })
    await teacherPanel.getByLabel('工号').fill(`QT${suffix}`)
    await teacherPanel.getByLabel('姓名').fill('验收老师')
    await teacherPanel.getByLabel('密码').fill('123456')
    await teacherPanel.getByLabel('擅长领域').fill('压力调节')
    await page.getByRole('button', { name: '提交审核' }).click()
    await expect(page.getByText('老师注册成功，请等待管理员审核')).toBeVisible()
  })

  test('student menus, lists, dialogs and consultation flow are usable', async ({ page }) => {
    await login(page, accounts.user)
    await expect(page.getByText('欢迎使用心理健康服务')).toBeVisible()
    await expect(page.getByText('春季心理健康周活动通知')).toBeVisible()

    await openMenu(page, '公告信息')
    await expect(page.getByRole('columnheader', { name: '标题' })).toBeVisible()
    await expect(page.getByText('考前压力调节小贴士')).toBeVisible()
    await expectNoVisualOverflow(page)

    await openMenu(page, '心理服务')
    const chenService = page.locator('.service-card').filter({ hasText: '陈老师' })
    await expect(chenService).toContainText('情绪压力')
    await chenService.getByRole('button', { name: '预约' }).click()
    await expectDialogInsideViewport(page, '提交预约')
    await page.getByLabel('预约时间').fill('2026-06-02 10:00:00')
    await page.getByLabel('预约备注').fill('全功能验收预约')
    await page.getByRole('button', { name: '提交' }).click()
    await expect(page.getByText('预约提交成功')).toBeVisible()

    await chenService.getByRole('button', { name: '咨询' }).click()
    await expectDialogInsideViewport(page, '提交咨询')
    await page.getByLabel('咨询内容').fill('全功能验收咨询内容')
    await page.getByRole('button', { name: '提交' }).click()
    await expect(page.getByText('咨询提交成功')).toBeVisible()

    await openMenu(page, '在线测试')
    const pressureTestRow = page.getByRole('row', { name: /大学生心理压力自评量表.*开始答题.*题目/ }).first()
    await expect(pressureTestRow).toBeVisible()
    await pressureTestRow.getByRole('button', { name: '开始答题' }).click()
    await expectDialogInsideViewport(page, '心理测试答题')
    const dialog = page.getByRole('dialog', { name: '心理测试答题' })
    await dialog.getByText('很少', { exact: true }).click()
    await dialog.getByText('能', { exact: true }).click()
    await dialog.getByText('愿意', { exact: true }).click()
    await dialog.getByRole('button', { name: '提交测试' }).click()
    await expect(page.getByText('测试完成')).toBeVisible()

    await openMenu(page, '我的预约')
    await expect(page.getByText('全功能验收预约')).toBeVisible()
    await openMenu(page, '咨询与回复')
    await expect(page.getByText('全功能验收咨询内容')).toBeVisible()
    await logout(page)
  })

  test('teacher menus, review dialogs, replies and test maintenance are usable', async ({ page }) => {
    await login(page, accounts.teacher)
    await expect(page.getByText('今日咨询工作台')).toBeVisible()

    await openMenu(page, '预约审核')
    await expect(page.getByText('全功能验收预约')).toBeVisible()
    await page.getByRole('row', { name: /全功能验收预约/ }).getByRole('button', { name: '通过' }).click()
    await expectDialogInsideViewport(page, '预约审核')
    await page.getByRole('button', { name: 'OK' }).click()
    await expect(page.getByText('审核完成')).toBeVisible()

    await openMenu(page, '咨询回复')
    await expect(page.getByText('全功能验收咨询内容')).toBeVisible()
    await page.getByRole('row', { name: /全功能验收咨询内容/ }).getByRole('button', { name: '回复' }).click()
    await expectDialogInsideViewport(page, '咨询回复')
    await page.getByRole('button', { name: 'OK' }).click()
    await expect(page.getByText('回复成功')).toBeVisible()

    await openMenu(page, '测试管理')
    await page.getByRole('button', { name: '新增测试' }).click()
    await expectDialogInsideViewport(page, '测试维护')
    await page.getByLabel('测试名称').fill('全功能验收测试')
    await page.getByLabel('说明').fill('用于验收测试维护弹窗')
    await page.getByRole('button', { name: '保存' }).click()
    await expect(page.getByText('测试已保存')).toBeVisible()
    await expect(page.getByText('全功能验收测试')).toBeVisible()

    await page.getByRole('row', { name: /全功能验收测试/ }).getByRole('button', { name: '题目' }).click()
    await expectDialogInsideViewport(page, '题库维护')
    await page.getByRole('button', { name: '新增题目' }).click()
    await page.getByLabel('题目').fill('我能保持稳定作息。')
    await page.getByLabel('答案解析').fill('保持作息有助于情绪稳定。')
    await page.getByRole('button', { name: '保存题目' }).click()
    await expect(page.getByText('题目已保存')).toBeVisible()
    await page.getByRole('button', { name: 'Close this dialog' }).click()

    await openMenu(page, '公告查看')
    await expect(page.getByText('春季心理健康周活动通知')).toBeVisible()
    await logout(page)
  })

  test('admin menus, moderation, system settings and management dialogs are usable', async ({ page }) => {
    await login(page, accounts.admin)
    await expect(page.getByText('平台综合管理中心')).toBeVisible()

    await openMenu(page, '用户与老师')
    await expect(page.getByText('普通用户管理')).toBeVisible()
    await expect(page.getByText('心理老师审核')).toBeVisible()
    await page.getByRole('row', { name: /2022002/ }).getByRole('button', { name: '禁用' }).click()
    await expect(page.locator('.el-message', { hasText: '用户状态已更新' }).last()).toBeVisible()
    await page.getByRole('row', { name: /2022002/ }).getByRole('button', { name: '启用' }).click()
    await expect(page.locator('.el-message', { hasText: '用户状态已更新' }).last()).toBeVisible()
    await page.getByRole('row', { name: /T002/ }).getByRole('button', { name: '通过' }).click()
    await expect(page.locator('.el-message', { hasText: '老师审核状态已更新' }).last()).toBeVisible()

    await openMenu(page, '公告管理')
    await page.getByRole('button', { name: '新增公告' }).click()
    await expectDialogInsideViewport(page, '公告维护')
    await page.getByLabel('标题').fill('全功能验收公告')
    await page.getByLabel('简介').fill('全功能验收简介')
    await page.getByLabel('内容').fill('全功能验收公告内容')
    await page.getByRole('button', { name: '保存' }).click()
    await expect(page.getByText('公告已保存')).toBeVisible()
    await expect(page.getByText('全功能验收公告')).toBeVisible()

    await openMenu(page, '测试管理')
    const pressureTestRow = page.getByRole('row', { name: /大学生心理压力自评量表.*编辑.*题目/ }).first()
    await expect(pressureTestRow).toBeVisible()
    await pressureTestRow.getByRole('button', { name: '编辑' }).click()
    await expectDialogInsideViewport(page, '测试维护')
    await page.getByLabel('说明').fill('管理员验收编辑测试说明')
    await page.getByRole('button', { name: '保存' }).click()
    await expect(page.getByText('测试已保存')).toBeVisible()

    await openMenu(page, '预约管理')
    await expect(page.getByText('全功能验收预约')).toBeVisible()
    await openMenu(page, '咨询管理')
    await expect(page.getByRole('row', { name: /全功能验收咨询内容.*已回复/ }).first()).toBeVisible()

    await openMenu(page, '系统管理')
    await expect(page.getByText('关于我们')).toBeVisible()
    await expect(page.getByText('系统配置')).toBeVisible()
    await page.getByLabel('副标题').fill('让校园心理服务更便捷')
    await page.getByRole('button', { name: '保存系统介绍' }).click()
    await expect(page.getByText('系统介绍已保存')).toBeVisible()
    await expectNoVisualOverflow(page)
    await logout(page)
  })

  test('secondary buttons and negative branches are clickable and safe', async ({ page }) => {
    await page.goto('/login')
    await page.getByRole('button', { name: '用户' }).click()
    await expect(page.getByPlaceholder('admin / 2022001 / T001')).toHaveValue('2022001')
    await page.getByRole('button', { name: '老师' }).click()
    await expect(page.getByPlaceholder('admin / 2022001 / T001')).toHaveValue('T001')
    await page.getByRole('button', { name: '管理员' }).click()
    await expect(page.getByPlaceholder('admin / 2022001 / T001')).toHaveValue('admin')
    await page.getByPlaceholder('默认 123456，管理员 admin123').fill('wrong-password')
    await page.getByTestId('login-submit').click()
    await expect(page.getByText('管理员账号或密码错误')).toBeVisible()

    await login(page, accounts.user)
    await openMenu(page, '心理服务')
    const chenService = page.locator('.service-card').filter({ hasText: '陈老师' })
    await chenService.getByRole('button', { name: '预约' }).click()
    await expectDialogInsideViewport(page, '提交预约')
    await page.getByRole('button', { name: '取消' }).click()
    await expect(page.getByRole('dialog', { name: '提交预约' })).toBeHidden()

    await chenService.getByRole('button', { name: '咨询' }).click()
    await expectDialogInsideViewport(page, '提交咨询')
    await page.getByRole('button', { name: '取消' }).click()
    await expect(page.getByRole('dialog', { name: '提交咨询' })).toBeHidden()

    await chenService.getByRole('button', { name: '预约' }).click()
    await page.getByLabel('预约时间').fill('2026-06-03 09:00:00')
    await page.getByLabel('预约备注').fill('边缘按钮驳回预约')
    await page.getByRole('button', { name: '提交' }).click()
    await expect(page.getByText('预约提交成功')).toBeVisible()

    await openMenu(page, '在线测试')
    const pressureTestRow = page.getByRole('row', { name: /大学生心理压力自评量表.*开始答题.*题目/ }).first()
    await pressureTestRow.getByRole('button', { name: '开始答题' }).click()
    await expectDialogInsideViewport(page, '心理测试答题')
    await page.getByRole('button', { name: '取消' }).click()
    await expect(page.getByRole('dialog', { name: '心理测试答题' })).toBeHidden()
    await logout(page)

    await login(page, accounts.teacher)
    await openMenu(page, '预约审核')
    await page.getByRole('row', { name: /边缘按钮驳回预约/ }).getByRole('button', { name: '驳回' }).click()
    await expectDialogInsideViewport(page, '预约审核')
    await page.getByRole('button', { name: 'OK' }).click()
    await expect(page.getByText('审核完成')).toBeVisible()

    await openMenu(page, '测试管理')
    await page.getByRole('button', { name: '新增测试' }).click()
    await expectDialogInsideViewport(page, '测试维护')
    await page.getByRole('button', { name: '取消' }).click()
    await expect(page.getByRole('dialog', { name: '测试维护' })).toBeHidden()
    await page.getByRole('row', { name: /大学生心理压力自评量表/ }).getByRole('button', { name: '题目' }).click()
    await expectDialogInsideViewport(page, '题库维护')
    await page.getByRole('row', { name: /最近一周我经常感到学习或生活压力较大/ }).getByRole('button', { name: '编辑' }).click()
    await page.getByLabel('答案解析').fill('边缘按钮编辑题目解析')
    await page.getByRole('button', { name: '保存题目' }).click()
    await expect(page.getByText('题目已保存')).toBeVisible()
    await page.getByRole('button', { name: 'Close this dialog' }).click()
    await logout(page)

    await login(page, accounts.admin)
    await openMenu(page, '用户与老师')
    await page.getByRole('row', { name: /T002/ }).getByRole('button', { name: '驳回' }).click()
    await expect(page.locator('.el-message', { hasText: '老师审核状态已更新' }).last()).toBeVisible()

    await openMenu(page, '公告管理')
    await page.getByRole('row', { name: /考前压力调节小贴士/ }).getByRole('button', { name: '编辑' }).click()
    await expectDialogInsideViewport(page, '公告维护')
    await page.getByLabel('简介').fill('边缘按钮编辑公告简介')
    await page.getByRole('button', { name: '保存' }).click()
    await expect(page.getByText('公告已保存')).toBeVisible()
    await page.getByRole('row', { name: /全功能验收公告/ }).getByRole('button', { name: '删除' }).click()
    await expect(page.getByText('公告已删除')).toBeVisible()
    await expect(page.getByText('全功能验收公告')).toHaveCount(0)
    await logout(page)
  })

  test('dashboard layouts avoid horizontal clipping on tablet and mobile widths', async ({ page }) => {
    await page.setViewportSize({ width: 1024, height: 768 })
    await login(page, accounts.admin)
    await openMenu(page, '公告管理')
    await expectNoVisualOverflow(page)
    await logout(page)

    await page.setViewportSize({ width: 390, height: 844 })
    await page.goto('/login')
    await expect(page.getByRole('heading', { name: '在线心理咨询系统' })).toBeVisible()
    await expectNoVisualOverflow(page)
  })
})
