# macOS 部署说明

本文档用于在苹果电脑上部署“基于 Java 的在线心理咨询系统”。适用于 Apple Silicon 芯片和 Intel 芯片的 Mac。

## 1. 环境要求

建议使用以下版本：

| 工具 | 推荐版本 | 说明 |
| --- | --- | --- |
| macOS | 12 或以上 | 建议使用较新的系统版本 |
| JDK | 11 | 后端 Spring Boot 使用 Java 11 |
| Node.js | 20 或以上 | 前端 Vue/Vite 运行环境 |
| Docker Desktop | 最新稳定版 | 推荐用于启动 MySQL 8 |
| Git | 系统自带或 Homebrew 安装 | 用于拉取代码 |

## 2. 安装基础工具

### 2.1 安装 Homebrew

如果电脑还没有 Homebrew，可在终端执行：

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

安装完成后按终端提示配置环境变量。

### 2.2 安装 JDK 11

```bash
brew install openjdk@11
```

配置环境变量：

```bash
echo 'export PATH="/opt/homebrew/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 11)' >> ~/.zshrc
source ~/.zshrc
```

Intel 芯片 Mac 如果 Homebrew 路径是 `/usr/local`，可改为：

```bash
echo 'export PATH="/usr/local/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
```

检查版本：

```bash
java -version
```

### 2.3 安装 Node.js

```bash
brew install node
node -v
npm -v
```

### 2.4 安装 Docker Desktop

到 Docker 官网下载 Docker Desktop for Mac，安装后启动 Docker Desktop。

确认 Docker 正常：

```bash
docker -v
docker compose version
```

## 3. 获取项目代码

```bash
git clone https://github.com/suepur526-cyber/Java-online-psychological-counseling-system.git
cd Java-online-psychological-counseling-system
```

如果已经有项目压缩包，也可以解压后进入项目根目录。

## 4. 启动 MySQL 数据库

项目已提供 `docker-compose.yml`，推荐直接使用 Docker 启动 MySQL：

```bash
docker compose up -d mysql
```

数据库配置：

| 配置项 | 值 |
| --- | --- |
| 数据库 | `counseling_system` |
| 地址 | `127.0.0.1:13307` |
| 用户名 | `counseling` |
| 密码 | `counseling123` |
| root 密码 | `root123456` |

查看容器状态：

```bash
docker ps
```

如果需要重新初始化数据库：

```bash
docker compose down -v
docker compose up -d mysql
```

## 5. 启动后端服务

进入后端目录：

```bash
cd backend
chmod +x mvnw
./mvnw spring-boot:run
```

后端默认地址：

```text
http://localhost:8080/api
```

如果端口被占用，可以指定端口，例如：

```bash
SERVER_PORT=18100 ./mvnw spring-boot:run
```

## 6. 启动前端服务

另开一个终端，进入前端目录：

```bash
cd frontend
npm install
npm run dev
```

前端默认地址：

```text
http://localhost:5173
```

如果后端不是默认 `8080` 端口，例如后端使用 `18100`，启动前端时设置：

```bash
VITE_API_BASE=http://127.0.0.1:18100/api npm run dev -- --host 127.0.0.1 --port 15200
```

访问：

```text
http://127.0.0.1:15200
```

## 7. 测试账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 普通用户 | `2022001` | `123456` |
| 心理老师 | `T001` | `123456` |
| 待审核老师 | `T002` | `123456` |

## 8. 功能验收路线

### 8.1 普通用户验收

1. 使用 `2022001 / 123456` 登录。
2. 查看首页统计、最新公告和心理服务。
3. 进入公告信息，查看公告列表。
4. 进入心理服务，选择陈老师预约。
5. 填写预约时间和备注，提交预约。
6. 点击咨询，填写咨询内容并提交。
7. 进入在线测试，完成心理测试答题。
8. 进入我的预约，查看预约状态。
9. 进入咨询与回复，查看咨询记录和老师回复。

### 8.2 心理老师验收

1. 使用 `T001 / 123456` 登录。
2. 进入预约审核，审核用户预约。
3. 进入咨询回复，回复用户咨询。
4. 进入测试管理，新增或编辑测试。
5. 进入题库维护，新增或编辑题目。
6. 进入公告查看，查看平台公告。

### 8.3 管理员验收

1. 使用 `admin / admin123` 登录。
2. 进入用户与老师，禁用/启用普通用户，审核老师。
3. 进入公告管理，新增、编辑、删除公告。
4. 进入测试管理，编辑心理测试。
5. 进入预约管理，查看预约记录。
6. 进入咨询管理，查看咨询和回复记录。
7. 进入系统管理，维护系统介绍。

## 9. 自动化测试命令

后端测试：

```bash
cd backend
./mvnw test
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
# 确保 MySQL 已启动
docker compose up -d mysql

# 启动后端测试端口
cd backend
SERVER_PORT=18100 ./mvnw spring-boot:run

# 另开终端
cd frontend
npm run test:e2e
```

当前项目已验证：

| 测试类型 | 结果 |
| --- | --- |
| 后端集成测试 | 7 passed |
| 前端单元测试 | 3 passed |
| 前端构建 | success |
| Playwright 点选测试 | 9 passed |

## 10. 常见问题

### 10.1 Docker 无法启动

先确认 Docker Desktop 已打开，再执行：

```bash
docker ps
```

如果提示没有权限或 daemon 未运行，重启 Docker Desktop。

### 10.2 MySQL 端口冲突

项目使用宿主机 `13307` 映射容器 `3306`，一般不会和本机 MySQL 冲突。

如果仍然冲突，修改 `docker-compose.yml`：

```yaml
ports:
  - "新的端口:3306"
```

同时修改 `backend/src/main/resources/application.yml` 中的数据库端口。

### 10.3 后端启动失败

检查：

1. JDK 是否为 11。
2. MySQL 容器是否启动。
3. `application.yml` 中数据库地址、用户名、密码是否正确。
4. 端口 `8080` 是否被占用。

### 10.4 前端请求失败

检查前端 API 地址：

```bash
VITE_API_BASE=http://127.0.0.1:8080/api npm run dev
```

如果后端端口是 `18100`，则改为：

```bash
VITE_API_BASE=http://127.0.0.1:18100/api npm run dev
```

### 10.5 页面乱码

数据库和 SQL 脚本均使用 `utf8mb4`。如果手动导入 SQL，建议使用：

```bash
mysql --default-character-set=utf8mb4 -u counseling -p counseling_system < database/schema.sql
mysql --default-character-set=utf8mb4 -u counseling -p counseling_system < database/seed.sql
```

## 11. 部署建议

答辩或演示时建议使用以下方式：

1. 提前启动 Docker Desktop。
2. 提前执行 `docker compose up -d mysql`。
3. 启动后端并确认接口可访问。
4. 启动前端并打开登录页面。
5. 使用测试账号按“功能验收路线”演示。

如果现场网络不稳定，建议提前运行 `npm install` 和 Maven 依赖下载，避免临时下载依赖失败。

## 12. 一键部署脚本

如果你不想手动执行前面的步骤，可以直接在项目根目录运行：

```bash
bash scripts/mac-setup.sh
```

如果你更习惯双击文件，也可以在 Finder 里双击：

```text
scripts/mac-setup.command
```

这个脚本会尽量自动完成：

- 检查 Homebrew
- 安装 JDK 11、Node.js、Git
- 提示并处理 Command Line Tools 问题
- 自动打开或安装 Docker Desktop
- 启动 MySQL 容器
- 准备后端和前端运行环境

如果环境已经准备好了，只想直接启动系统，可以再运行：

```bash
bash scripts/mac-start.sh
```

或者双击：

```text
scripts/mac-start.command
```

脚本会输出前端地址、后端地址和测试账号。  
如果遇到 macOS 系统层面的确认窗口，按照窗口提示点一下就行，脚本会继续带你往下走。
