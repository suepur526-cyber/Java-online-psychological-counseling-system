#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

log() { printf '\n[%s] %s\n' "部署" "$1"; }
warn() { printf '\n[%s] %s\n' "提醒" "$1"; }
die() { printf '\n[%s] %s\n' "失败" "$1" >&2; exit 1; }

ensure_cmd() {
  command -v "$1" >/dev/null 2>&1
}

load_brew_env() {
  if [[ -x /opt/homebrew/bin/brew ]]; then
    eval "$(/opt/homebrew/bin/brew shellenv)"
  elif [[ -x /usr/local/bin/brew ]]; then
    eval "$(/usr/local/bin/brew shellenv)"
  fi
}

install_brew() {
  if ensure_cmd brew; then
    return
  fi

  log "未检测到 Homebrew，开始安装。"
  /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

  load_brew_env
  if ! ensure_cmd brew; then
    die "Homebrew 安装后仍未找到 brew。请重新运行安装器。"
  fi

  if [[ -x /opt/homebrew/bin/brew ]]; then
    grep -q 'opt/homebrew/bin/brew shellenv' "$HOME/.zprofile" 2>/dev/null || \
      echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> "$HOME/.zprofile"
  elif [[ -x /usr/local/bin/brew ]]; then
    grep -q 'usr/local/bin/brew shellenv' "$HOME/.zprofile" 2>/dev/null || \
      echo 'eval "$(/usr/local/bin/brew shellenv)"' >> "$HOME/.zprofile"
  fi
}

ensure_xcode_tools() {
  if xcode-select -p >/dev/null 2>&1; then
    return
  fi

  warn "检测到 Xcode Command Line Tools 不完整。请在弹出的系统窗口里点击 Install，完成后重新运行脚本。"
  xcode-select --install || true
  exit 1
}

ensure_brew_packages() {
  log "检查并安装基础工具。"
  brew update
  brew install openjdk@11 node git
  if ! brew list --cask docker >/dev/null 2>&1; then
    brew install --cask docker
  fi
}

configure_java() {
  local java_home_path=""
  if [[ -d /opt/homebrew/opt/openjdk@11 ]]; then
    java_home_path="$(brew --prefix openjdk@11)/libexec/openjdk.jdk/Contents/Home"
  elif [[ -d /usr/local/opt/openjdk@11 ]]; then
    java_home_path="$(brew --prefix openjdk@11)/libexec/openjdk.jdk/Contents/Home"
  fi

  if [[ -n "$java_home_path" ]]; then
    grep -q 'openjdk@11/bin' "$HOME/.zshrc" 2>/dev/null || \
      echo "export PATH=\"$java_home_path/bin:\$PATH\"" >> "$HOME/.zshrc"
    grep -q 'JAVA_HOME=.*openjdk@11' "$HOME/.zshrc" 2>/dev/null || \
      echo "export JAVA_HOME=\"$java_home_path\"" >> "$HOME/.zshrc"
    export PATH="$java_home_path/bin:$PATH"
    export JAVA_HOME="$java_home_path"
  fi
}

ensure_docker() {
  if ! ensure_cmd docker; then
    warn "未检测到 Docker 命令，尝试安装 Docker Desktop。"
    if ! brew list --cask docker >/dev/null 2>&1; then
      brew install --cask docker
    fi
  fi

  if ! docker info >/dev/null 2>&1; then
    warn "Docker Desktop 还没有完全启动，正在尝试打开应用。"
    open -a Docker >/dev/null 2>&1 || true
    for _ in {1..30}; do
      if docker info >/dev/null 2>&1; then
        return
      fi
      sleep 2
    done
    die "Docker Desktop 仍未就绪。请先打开 Docker Desktop，确认它完成启动后重新运行脚本。"
  fi
}

start_mysql() {
  log "启动 MySQL 容器。"
  docker compose up -d mysql
}

prepare_backend() {
  log "准备后端运行环境。"
  chmod +x backend/mvnw
}

prepare_frontend() {
  log "准备前端运行环境。"
  if [[ ! -d frontend/node_modules ]]; then
    (cd frontend && npm install)
  fi
}

print_summary() {
  cat <<'EOF'

部署准备完成。

下一步可以执行：
  bash scripts/mac-start.sh

常用访问地址：
  前端：http://127.0.0.1:15200
  后端：http://127.0.0.1:18100/api

测试账号：
  管理员：admin / admin123
  普通用户：2022001 / 123456
  心理老师：T001 / 123456
  待审核老师：T002 / 123456
EOF
}

main() {
  [[ "$(uname -s)" == "Darwin" ]] || die "这个脚本只能在 macOS 上运行。"
  ensure_xcode_tools
  install_brew
  ensure_brew_packages
  configure_java
  ensure_docker
  start_mysql
  prepare_backend
  prepare_frontend
  print_summary
}

main "$@"
