# 测试报告

## 测试环境

- 操作系统：Windows
- Java：11.0.31
- Node.js：本机 Node 环境
- 数据库脚本：MySQL 8
- 后端测试数据库：H2 MySQL 兼容模式
- 端到端测试数据库：Docker MySQL 8，端口 `13307`

## 后端测试

执行命令：

```bash
cd backend
.\mvnw.cmd test
```

测试结果：

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

覆盖功能：

- 管理员、普通用户、心理老师登录
- 待审核老师登录拦截
- 普通用户注册
- 心理老师注册
- 公告查询和新增
- 心理测试提交、自动计分、测试记录保存
- 用户预约提交
- 心理老师预约审核
- 用户咨询提交
- 心理老师咨询回复
- 管理员审核老师
- 管理员禁用用户

## 前端测试

执行命令：

```bash
cd frontend
npm run test
```

测试结果：

```text
Test Files  2 passed
Tests  3 passed
```

覆盖内容：

- 状态与结果标签转换工具
- 登录状态管理
- 退出登录状态清理

## 前端构建测试

执行命令：

```bash
cd frontend
npm run build
```

测试结果：

```text
✓ built
```

## 端到端点选测试

执行环境：

```text
MySQL: Docker 容器 counseling_mysql，127.0.0.1:13307
后端: http://127.0.0.1:18100/api
前端: http://127.0.0.1:15200
```

执行命令：

```bash
cd frontend
npm run test:e2e
```

测试结果：

```text
3 passed
```

覆盖流程：

- 普通用户登录，查看心理服务，提交预约，提交留言咨询，完成在线心理测试。
- 心理老师登录，审核普通用户预约，回复普通用户咨询。
- 管理员登录，新增公告，查看普通用户管理和心理老师审核页面。

## 说明

端到端测试前已使用 `database/schema.sql` 和 `database/seed.sql` 重置 MySQL 测试数据，保证点选流程从固定测试数据开始执行。
