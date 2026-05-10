const fs = require('fs')
const path = require('path')
const {
  AlignmentType,
  BorderStyle,
  Document,
  HeadingLevel,
  LevelFormat,
  Packer,
  PageBreak,
  PageNumber,
  Paragraph,
  Table,
  TableCell,
  TableOfContents,
  TableRow,
  TextRun,
  WidthType,
  Footer,
  ShadingType,
} = require('docx')

const outPath = path.join(__dirname, '答辩准备与功能说明.docx')
const contentWidth = 9026
const border = { style: BorderStyle.SINGLE, size: 1, color: 'CCCCCC' }
const borders = { top: border, bottom: border, left: border, right: border }

function p(text, options = {}) {
  return new Paragraph({
    spacing: { after: options.after ?? 120, before: options.before ?? 0 },
    alignment: options.alignment,
    heading: options.heading,
    children: [new TextRun({ text, bold: options.bold, size: options.size, color: options.color })],
  })
}

function bullet(text) {
  return new Paragraph({
    numbering: { reference: 'bullet-list', level: 0 },
    spacing: { after: 80 },
    children: [new TextRun(text)],
  })
}

function numbered(text) {
  return new Paragraph({
    numbering: { reference: 'number-list', level: 0 },
    spacing: { after: 80 },
    children: [new TextRun(text)],
  })
}

function pageBreak() {
  return new Paragraph({ children: [new PageBreak()] })
}

function table(headers, rows, widths) {
  return new Table({
    width: { size: contentWidth, type: WidthType.DXA },
    columnWidths: widths,
    rows: [
      new TableRow({
        children: headers.map((h, i) => new TableCell({
          borders,
          width: { size: widths[i], type: WidthType.DXA },
          shading: { fill: 'DDEFEA', type: ShadingType.CLEAR },
          margins: { top: 100, bottom: 100, left: 120, right: 120 },
          children: [p(h, { bold: true, after: 0 })],
        })),
      }),
      ...rows.map((row) => new TableRow({
        children: row.map((cell, i) => new TableCell({
          borders,
          width: { size: widths[i], type: WidthType.DXA },
          margins: { top: 100, bottom: 100, left: 120, right: 120 },
          children: [p(String(cell), { after: 0 })],
        })),
      })),
    ],
  })
}

function qa(q, a) {
  return [
    p(`Q：${q}`, { bold: true, after: 60 }),
    p(`A：${a}`, { after: 180 }),
  ]
}

const children = [
  new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { before: 1800, after: 260 },
    children: [new TextRun({ text: '基于 Java 的在线心理咨询系统', bold: true, size: 42 })],
  }),
  new Paragraph({
    alignment: AlignmentType.CENTER,
    spacing: { after: 900 },
    children: [new TextRun({ text: '答辩准备与功能说明文档', size: 30, color: '2F5F5A' })],
  }),
  p('适用场景：毕业设计答辩、项目演示、功能讲解、部署说明配套材料', { alignment: AlignmentType.CENTER, after: 120 }),
  p('技术栈：Java 11、Spring Boot 2.7、MyBatis、MySQL 8、Vue 3、Element Plus、Axios、Pinia、Vue Router', { alignment: AlignmentType.CENTER, after: 120 }),
  p('测试结论：后端 7 个集成测试通过，前端 3 个单元测试通过，前端构建成功，Playwright 9 条端到端点选测试通过。', { alignment: AlignmentType.CENTER, after: 120 }),
  pageBreak(),

  new TableOfContents('目录', { hyperlink: true, headingStyleRange: '1-3' }),
  pageBreak(),

  p('一、项目概述', { heading: HeadingLevel.HEADING_1 }),
  p('本项目是一个面向校园心理健康服务场景的在线心理咨询系统。系统采用 B/S 架构和前后端分离开发方式，围绕普通用户、心理老师、管理员三类角色，实现公告浏览、心理服务查看、在线预约、留言式咨询、咨询回复、心理测试、后台管理等功能。'),
  p('系统设计目标不是追求复杂业务，而是在学生项目范围内实现功能完整、流程闭环、代码结构清晰、测试可验证、答辩可讲清楚的应用系统。在线咨询部分按需求进行了合理简化，采用“用户提交咨询内容，老师回复，系统保存记录”的留言式咨询模式，避免实时通信带来的复杂度，同时保留咨询业务闭环。'),

  p('二、需求来源与简化原则', { heading: HeadingLevel.HEADING_1 }),
  bullet('保留初稿中的核心需求：注册登录、公告、心理测试、心理服务、预约、咨询、回复、后台管理。'),
  bullet('业务逻辑保持简单：不引入复杂排班算法、不做真实支付、不做实时 WebSocket 聊天。'),
  bullet('心理档案简化为用户信息、测试记录、预约记录、咨询记录和回复记录的组合。'),
  bullet('数据分析简化为测试记录列表和管理端查询展示，适合毕业设计实现和答辩。'),
  bullet('资质审核保留为管理员对心理老师账号状态的审核。'),

  p('三、技术架构', { heading: HeadingLevel.HEADING_1 }),
  table(
    ['层次', '技术', '作用'],
    [
      ['前端', 'Vue 3、Vite、Element Plus、Axios、Pinia、Vue Router', '实现页面展示、表单交互、状态管理、接口请求和路由控制'],
      ['后端', 'Java 11、Spring Boot 2.7、MyBatis', '实现 REST 接口、业务逻辑、数据访问和参数校验'],
      ['数据库', 'MySQL 8', '存储用户、老师、公告、测试、预约、咨询、回复、系统信息等数据'],
      ['测试', 'Spring Boot Test、MockMvc、Vitest、Playwright', '覆盖后端接口、前端工具函数、端到端浏览器点选流程'],
      ['部署', 'Docker Compose、Maven Wrapper、npm', '简化数据库启动、后端启动和前端运行'],
    ],
    [1500, 3500, 4026],
  ),
  p('架构说明：前端通过 Axios 调用后端 REST API，后端通过 MyBatis 访问 MySQL 数据库。系统没有引入复杂微服务架构，整体部署简单，适合学生项目演示和本地答辩环境运行。'),

  p('四、角色与功能解析', { heading: HeadingLevel.HEADING_1 }),
  p('4.1 普通用户功能', { heading: HeadingLevel.HEADING_2 }),
  bullet('注册登录：用户填写学号、姓名、密码、手机号后注册，注册成功后可登录系统。'),
  bullet('公告查看：用户可以查看平台发布的心理健康知识、活动通知和系统公告。'),
  bullet('心理服务查看：用户可以查看心理老师服务信息，包括老师姓名、费用、工作时间、执教年限、简介和荣誉。'),
  bullet('在线预约：用户选择老师，填写预约时间和备注，提交预约申请，等待老师审核。'),
  bullet('在线咨询：用户向老师提交咨询内容，系统保存咨询记录。'),
  bullet('咨询回复查看：老师回复后，用户可以在咨询与回复模块查看回复内容。'),
  bullet('在线心理测试：用户选择测试并答题，系统自动计算得分，生成测试结果并保存记录。'),

  p('4.2 心理老师功能', { heading: HeadingLevel.HEADING_2 }),
  bullet('老师注册登录：老师填写工号、姓名、密码、擅长领域后注册，等待管理员审核。'),
  bullet('预约审核：老师查看用户提交给自己的预约，选择通过或驳回，并填写审核回复。'),
  bullet('咨询回复：老师查看用户咨询内容并填写回复，系统保存回复记录。'),
  bullet('测试管理：老师可以新增或编辑心理测试。'),
  bullet('题库维护：老师可以查看测试题目，新增或编辑题目和答案解析。'),
  bullet('公告查看：老师可以查看平台发布的公告信息。'),

  p('4.3 管理员功能', { heading: HeadingLevel.HEADING_2 }),
  bullet('管理员登录：管理员使用后台账号登录，进入管理端。'),
  bullet('用户管理：管理员可以查看普通用户列表，禁用或启用用户账号。'),
  bullet('心理老师审核：管理员可以通过或驳回待审核老师。'),
  bullet('公告管理：管理员可以新增、编辑、删除公告。'),
  bullet('测试管理：管理员可以维护心理测试信息。'),
  bullet('预约管理：管理员可以查看系统预约记录。'),
  bullet('咨询管理：管理员可以查看咨询记录和咨询回复记录，用于平台监管。'),
  bullet('系统管理：管理员可以维护关于我们、平台简介和系统配置展示。'),

  p('五、核心业务流程', { heading: HeadingLevel.HEADING_1 }),
  numbered('普通用户注册或使用测试账号登录系统。'),
  numbered('用户查看公告和心理服务，选择合适老师提交预约。'),
  numbered('心理老师登录后查看预约申请，对预约进行通过或驳回。'),
  numbered('用户向老师提交留言式咨询内容。'),
  numbered('老师查看咨询内容并填写回复。'),
  numbered('用户查看咨询回复，完成一次咨询闭环。'),
  numbered('用户完成心理测试，系统自动计算得分并保存测试记录。'),
  numbered('管理员维护用户、老师、公告、测试、预约、咨询和系统信息。'),

  p('六、数据库设计说明', { heading: HeadingLevel.HEADING_1 }),
  table(
    ['数据表', '用途'],
    [
      ['admins', '存储管理员账号、密码、姓名和创建时间'],
      ['users', '存储普通用户学号、密码、姓名、联系方式、状态等信息'],
      ['teachers', '存储心理老师工号、密码、姓名、擅长领域、资质、审核状态等信息'],
      ['announcements', '存储公告标题、简介、内容、状态和发布时间'],
      ['services', '存储心理服务展示信息，包括老师、费用、工作时间、简介、荣誉'],
      ['psychological_tests', '存储心理测试基本信息'],
      ['test_questions', '存储测试题目、选项 JSON、分值、答案和解析'],
      ['test_records', '存储用户测试记录、得分、结果和答案 JSON'],
      ['appointments', '存储预约编号、老师、用户、预约时间、备注、状态和审核回复'],
      ['consultations', '存储用户咨询内容、老师、用户、状态和日期'],
      ['consultation_replies', '存储老师回复内容及关联咨询'],
      ['system_infos / system_configs', '存储系统介绍和基础配置'],
    ],
    [2800, 6226],
  ),
  p('数据库设计保持清晰直接，每个业务模块都有对应表结构。预约、咨询、测试记录等关键业务数据均可追溯，便于演示和答辩说明。'),

  p('七、测试与质量保证', { heading: HeadingLevel.HEADING_1 }),
  table(
    ['测试类型', '结果', '说明'],
    [
      ['后端集成测试', '7 passed', '覆盖登录、注册、公告、测试、预约、咨询、审核等后端接口'],
      ['前端单元测试', '3 passed', '覆盖登录状态管理、标签转换和结果样式工具函数'],
      ['前端构建', 'success', '验证 Vue 项目可以正常打包'],
      ['Playwright 点选测试', '9 passed', '覆盖三类角色主流程、边缘按钮、取消、删除、驳回、编辑、错误登录和布局检查'],
    ],
    [2300, 1800, 4926],
  ),
  p('点选测试说明：测试中不仅覆盖了主流程，还覆盖了示例账号按钮、错误登录提示、预约/咨询/测试弹窗取消、预约驳回、老师驳回、公告编辑与删除、题目编辑等容易遗漏的按钮分支。布局检查覆盖平板宽度和移动端登录页，验证没有明显横向遮挡、切割或不可见溢出。'),

  p('八、部署说明概览', { heading: HeadingLevel.HEADING_1 }),
  bullet('Mac 电脑推荐安装 JDK 11、Node.js、Docker Desktop 和 Git。'),
  bullet('使用 `docker compose up -d mysql` 启动 MySQL 8，数据库端口为 13307。'),
  bullet('后端进入 `backend` 目录执行 `./mvnw spring-boot:run`。'),
  bullet('前端进入 `frontend` 目录执行 `npm install` 和 `npm run dev`。'),
  bullet('详细部署步骤见仓库 `docs/macOS部署说明.md`。'),

  p('九、演示路线建议', { heading: HeadingLevel.HEADING_1 }),
  numbered('先展示登录页，说明系统分为普通用户、心理老师、管理员三类角色。'),
  numbered('普通用户登录后展示公告、心理服务、预约、咨询和心理测试。'),
  numbered('心理老师登录后展示预约审核和咨询回复，体现咨询业务闭环。'),
  numbered('管理员登录后展示用户管理、老师审核、公告管理、测试管理和系统管理。'),
  numbered('最后展示测试报告，说明系统经过自动化点选测试和构建验证。'),

  p('十、答辩 QA 问答准备', { heading: HeadingLevel.HEADING_1 }),
  ...qa('为什么选择 Spring Boot？', 'Spring Boot 配置简单、生态成熟、内置 Tomcat，适合快速搭建 Java Web 后端。对于学生项目来说，它能减少繁琐配置，让重点放在业务接口和数据处理上。'),
  ...qa('为什么选择 MyBatis，而不是 JPA？', 'MyBatis 更直观，SQL 可控性更强，适合毕业设计中展示数据库表和增删改查逻辑。项目业务以简单 CRUD 为主，使用 MyBatis 能让数据访问层更清晰。'),
  ...qa('为什么前端使用 Vue 和 Element Plus？', 'Vue 组件化开发效率高，学习成本相对较低；Element Plus 提供表格、表单、弹窗、标签等成熟组件，可以快速做出完整后台系统界面。'),
  ...qa('在线咨询为什么没有做实时聊天？', '需求中提到在线咨询，但考虑学生项目实现复杂度，本系统采用留言式咨询。用户提交咨询内容，老师回复，系统保存记录，能够满足咨询流程闭环，同时避免 WebSocket 实时通信带来的复杂部署和状态同步问题。'),
  ...qa('系统有哪些角色权限？', '系统分为普通用户、心理老师、管理员。普通用户主要使用测试、预约和咨询功能；心理老师处理预约和咨询回复，并维护测试题库；管理员负责用户、老师、公告、测试、预约、咨询和系统信息管理。'),
  ...qa('心理测试如何计分？', '每个测试题目的选项中配置分值，用户提交答案后，后端根据所选选项累计得分，并根据分数区间生成结果等级，最后保存测试记录。'),
  ...qa('预约流程是怎样的？', '用户选择老师并提交预约，预约初始状态为待审核。老师登录后可以查看自己的预约申请，选择通过或驳回，并填写审核回复。用户可以在我的预约中查看结果。'),
  ...qa('老师注册后为什么不能直接使用？', '系统保留了资质审核逻辑。老师注册后状态为待审核，管理员审核通过后，老师才能正常登录并处理预约和咨询。这样符合心理咨询服务需要审核资质的业务场景。'),
  ...qa('管理员能看到咨询内容是否涉及隐私？', '管理员端提供咨询记录查看用于平台监管。答辩时可以说明本项目是教学项目，实际生产环境还应增加更严格的隐私权限、脱敏展示和操作审计。'),
  ...qa('系统如何保证测试过？', '项目包含后端集成测试、前端单元测试、前端构建测试和 Playwright 浏览器端到端点选测试。端到端测试覆盖三类角色、主要流程、边缘按钮、负向登录、取消、删除、驳回、编辑和布局检查。'),
  ...qa('如果部署到 Mac，最容易出问题的地方是什么？', '常见问题是 JDK 版本不是 11、Docker Desktop 未启动、MySQL 端口冲突、前端 API 地址配置错误。部署文档中已经给出对应排查方法。'),
  ...qa('项目还有哪些可以扩展的方向？', '可以扩展实时聊天、咨询排班、咨询师评价、测试结果图表统计、JWT 登录认证、密码加密、隐私脱敏和部署到云服务器等功能。'),

  p('十一、项目总结', { heading: HeadingLevel.HEADING_1 }),
  p('本系统围绕在线心理咨询业务完成了从用户注册、心理测试、预约咨询、老师审核回复到管理员后台维护的完整闭环。项目技术栈符合 Java Web 毕业设计要求，业务复杂度控制合理，数据库结构清晰，页面交互完整，并通过自动化测试进行了验证。答辩时可以重点强调“需求完整、流程闭环、技术栈清晰、测试充分、部署可复现”。'),
]

const doc = new Document({
  styles: {
    default: {
      document: {
        run: { font: 'Arial', size: 24 },
      },
    },
    paragraphStyles: [
      {
        id: 'Heading1',
        name: 'Heading 1',
        basedOn: 'Normal',
        next: 'Normal',
        quickFormat: true,
        run: { font: 'Arial', size: 32, bold: true, color: '173E3A' },
        paragraph: { spacing: { before: 260, after: 180 }, outlineLevel: 0 },
      },
      {
        id: 'Heading2',
        name: 'Heading 2',
        basedOn: 'Normal',
        next: 'Normal',
        quickFormat: true,
        run: { font: 'Arial', size: 28, bold: true, color: '2F5F5A' },
        paragraph: { spacing: { before: 220, after: 140 }, outlineLevel: 1 },
      },
    ],
  },
  numbering: {
    config: [
      {
        reference: 'bullet-list',
        levels: [{ level: 0, format: LevelFormat.BULLET, text: '•', alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 520, hanging: 260 } } } }],
      },
      {
        reference: 'number-list',
        levels: [{ level: 0, format: LevelFormat.DECIMAL, text: '%1.', alignment: AlignmentType.LEFT, style: { paragraph: { indent: { left: 520, hanging: 260 } } } }],
      },
    ],
  },
  sections: [{
    properties: {
      page: {
        size: { width: 11906, height: 16838 },
        margin: { top: 1440, right: 1440, bottom: 1440, left: 1440 },
      },
    },
    footers: {
      default: new Footer({
        children: [
          new Paragraph({
            alignment: AlignmentType.CENTER,
            children: [new TextRun('第 '), new TextRun({ children: [PageNumber.CURRENT] }), new TextRun(' 页')],
          }),
        ],
      }),
    },
    children,
  }],
})

Packer.toBuffer(doc).then((buffer) => {
  fs.writeFileSync(outPath, buffer)
  console.log(outPath)
})
