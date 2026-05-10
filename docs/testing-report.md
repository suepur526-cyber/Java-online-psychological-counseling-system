# 测试报告

## 测试环境

- 操作系统：Windows
- Java：11.0.31
- Node.js：本机 Node 环境
- 数据库脚本：MySQL 8
- 后端测试数据库：H2 MySQL 兼容模式

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

## 说明

当前机器 Docker Desktop 未启动，无法连接 Docker 引擎，因此未完成本机 MySQL 容器全栈浏览器测试。项目已提供 `docker-compose.yml`、`database/schema.sql` 和 `database/seed.sql`，启动 Docker 或本机 MySQL 后即可运行完整系统。
