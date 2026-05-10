# 基于 Java 的在线心理咨询系统

本项目是一个面向学生毕业设计的在线心理咨询系统，采用 Java、Spring Boot、MyBatis、MySQL、Vue、Element Plus 和 Axios 实现，包含普通用户、心理老师、管理员三类角色。

## 功能模块

- 普通用户：注册登录、公告查看、心理服务查看、在线心理测试、咨询预约、在线咨询、咨询回复查看。
- 心理老师：注册登录、预约审核、在线咨询管理、咨询回复、心理测试题库维护、测试记录查看。
- 管理员：用户管理、心理老师审核、公告管理、心理测试管理、预约管理、咨询管理、系统信息管理。

在线咨询按学生项目实现为“用户提交咨询内容，心理老师回复，系统保存记录”的留言式咨询。

## 技术栈

- 后端：Java 11、Spring Boot 2.7、MyBatis、MySQL 8
- 前端：Vue 3、Vite、Element Plus、Axios、Pinia、Vue Router
- 测试：Spring Boot Test、MockMvc、Vitest、Playwright
- 数据库：MySQL，提供 `database/schema.sql` 和 `database/seed.sql`

## 项目结构

```text
backend/     Spring Boot 后端接口
frontend/    Vue 前端页面
database/    MySQL 建表和测试数据脚本
docs/        设计文档、计划和测试报告
```

## 交付文档

- 需求与技术栈整理：`系统需求与技术栈整理.md`
- macOS 部署说明：`docs/macOS部署说明.md`
- 测试报告：`docs/testing-report.md`
- 答辩准备 Word 文档：`docs/答辩准备与功能说明.docx`

## 测试账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | admin | admin123 |
| 普通用户 | 2022001 | 123456 |
| 心理老师 | T001 | 123456 |
| 待审核老师 | T002 | 123456 |

## 数据库启动

方式一：使用 Docker Compose。

```bash
docker compose up -d
```

数据库信息：

```text
数据库：counseling_system
地址：127.0.0.1:13307
用户：counseling
密码：counseling123
root 密码：root123456
```

方式二：本地 MySQL。

1. 创建数据库或直接执行 `database/schema.sql`。
2. 执行 `database/seed.sql` 添加测试数据。
3. 如需修改连接信息，编辑 `backend/src/main/resources/application.yml`。当前默认端口为 `13307`，避免和本机已有 MySQL 端口冲突。

## 后端运行

```bash
cd backend
.\mvnw.cmd spring-boot:run
```

后端接口地址：

```text
http://localhost:8080/api
```

## 前端运行

```bash
cd frontend
npm install
npm run dev
```

前端访问地址：

```text
http://localhost:5173
```

## 测试命令

后端集成测试：

```bash
cd backend
.\mvnw.cmd test
```

前端单元测试：

```bash
cd frontend
npm run test
```

前端构建：

```bash
cd frontend
npm run build
```

端到端点选测试：

```bash
# 先启动 MySQL
docker compose up -d mysql

# 启动后端测试端口
cd backend
$env:SERVER_PORT="18100"
.\mvnw.cmd spring-boot:run

# 另开终端执行，Playwright 会自动启动 15200 前端
cd ../frontend
npm run test:e2e
```

## 已验证内容

- 后端 7 个集成测试全部通过。
- 前端 3 个单元测试全部通过。
- 前端生产构建成功。
- Playwright 端到端点选测试 9 条流程全部通过，覆盖普通用户、心理老师、管理员核心操作、边缘按钮、弹窗取消、编辑删除、拒绝审核、禁用启用、错误登录和响应式布局检查。
