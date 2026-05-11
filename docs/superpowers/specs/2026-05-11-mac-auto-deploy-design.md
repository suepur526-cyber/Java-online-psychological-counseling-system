# Mac 自动部署脚本设计

## Goal

为这个在线心理咨询系统提供一个面向 macOS 的“一键部署”脚本，尽量自动完成环境检查、依赖安装、数据库启动、后端启动和前端启动，让第一次接触这套系统的同学只需要少量人工确认就能跑起来。

## Confirmed Scope

这个脚本面向 Apple Silicon 和 Intel Mac，优先支持当前仓库已经采用的部署方式：

- MySQL 通过 `docker-compose.yml` 启动。
- 后端通过 Maven Wrapper 启动。
- 前端通过 Vite 启动。
- 文档和提示全部用中文，适合学生第一次部署。

脚本必须保留当前项目的简单架构，不引入新的部署平台，不把系统改成云部署，也不改业务逻辑。

## Hard Limits

macOS 有三类操作无法真正做到完全无交互：

- 输入系统管理员密码。
- 首次安装 Xcode Command Line Tools 时的系统弹窗。
- 首次打开 Docker Desktop 时的系统授权提示。

脚本会尽量自动化这些流程，但必须在这些点明确提示用户下一步要做什么，而不是假装全自动成功。

## Architecture

计划新增一个 `scripts/` 目录，里面放两个核心脚本：

- `scripts/mac-setup.sh`：一键安装和初始化脚本，负责环境检查、Homebrew 安装、JDK 11、Node.js、Docker Desktop 提示、数据库启动、代码依赖准备。
- `scripts/mac-start.sh`：一键启动脚本，负责在环境已经就绪时启动数据库、后端和前端，并打印最终访问地址。

脚本采用顺序化流程，不引入额外编排工具。这样用户只要复制一条命令即可开始，出错时也容易看懂卡在哪一步。

## Flow

1. 检查当前目录是不是项目根目录。
2. 检查 macOS 架构，区分 `arm64` 和 `x86_64`。
3. 检查 `brew` 是否存在，缺失时自动安装 Homebrew，并输出需要执行的 shellenv 语句。
4. 检查 `xcode-select`，缺失时提醒用户安装 Command Line Tools。
5. 安装或确认 `git`、`openjdk@11`、`node`、`docker` 等基础依赖。
6. 检查 Docker Desktop 是否可用，不可用时提示用户先启动 Docker Desktop。
7. 使用 `docker compose up -d mysql` 启动 MySQL。
8. 启动后端时优先使用 `./mvnw spring-boot:run`，默认端口 `8080`；如冲突则切到 `18100`。
9. 启动前端时优先使用 `npm install` 和 `npm run dev`；如后端端口变更，则自动注入 `VITE_API_BASE`。
10. 在终端输出测试账号、访问地址、常见故障排查提示。

## Error Handling

脚本需要把失败点写清楚，避免只给一个空白退出码。

- 如果 Homebrew 安装失败，停止并提示重试。
- 如果 Docker 未启动，提示打开 Docker Desktop 后重新运行脚本。
- 如果 MySQL 容器启动失败，打印 `docker compose logs mysql` 的查看方式。
- 如果后端端口占用，脚本自动换到备用端口并告诉用户最终地址。
- 如果前端依赖安装失败，脚本说明是网络问题还是 Node 环境问题。

## Files

- Create: `scripts/mac-setup.sh`
- Create: `scripts/mac-start.sh`
- Modify: `README.md`
- Modify: `docs/macOS部署说明.md`

## Testing

脚本完成后要做四层验证：

1. `bash -n scripts/mac-setup.sh`
2. `bash -n scripts/mac-start.sh`
3. 在 Mac 上手动执行脚本，确认提示语清楚、步骤顺序正确。
4. 启动后访问前端页面，确认首页、登录、角色菜单能正常加载。

## Acceptance Criteria

- 新用户可以照着脚本提示把环境跑起来。
- 脚本能在缺少 Homebrew、JDK、Node、Docker 的情况下给出明确处理方式。
- 脚本能启动数据库、后端和前端，并打印最终访问地址。
- README 和 macOS 部署文档都能指向这个脚本。
