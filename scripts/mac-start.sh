#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

log() { printf '\n[%s] %s\n' "启动" "$1"; }
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

configure_java_env() {
  local java_home_path=""
  if command -v brew >/dev/null 2>&1 && brew list --formula openjdk@11 >/dev/null 2>&1; then
    java_home_path="$(brew --prefix openjdk@11)/libexec/openjdk.jdk/Contents/Home"
  elif [[ -x /usr/libexec/java_home ]]; then
    java_home_path="$({ /usr/libexec/java_home -v 11; } 2>/dev/null || true)"
  fi

  if [[ -n "$java_home_path" ]]; then
    export PATH="$java_home_path/bin:$PATH"
    export JAVA_HOME="$java_home_path"
  fi
}

ensure_docker() {
  ensure_cmd docker || die "未检测到 Docker。"
  if ! docker info >/dev/null 2>&1; then
    open -a Docker >/dev/null 2>&1 || true
    for _ in {1..30}; do
      if docker info >/dev/null 2>&1; then
        return
      fi
      sleep 2
    done
    die "Docker 没有启动，请先打开 Docker Desktop。"
  fi
}

detect_backend_port() {
  if lsof -iTCP:8080 -sTCP:LISTEN >/dev/null 2>&1; then
    echo 18100
  else
    echo 8080
  fi
}

start_mysql() {
  docker compose up -d mysql
}

start_backend() {
  local port="$1"
  log "启动后端，端口 $port。"
  if [[ "$port" == "18100" ]]; then
    (cd backend && SERVER_PORT=18100 ./mvnw spring-boot:run > ../backend-run-mac.log 2>&1 &)
  else
    (cd backend && ./mvnw spring-boot:run > ../backend-run-mac.log 2>&1 &)
  fi
}

start_frontend() {
  local api_base="$1"
  log "启动前端。"
  (cd frontend && VITE_API_BASE="$api_base" npm run dev -- --host 127.0.0.1 --port 15200 > ../frontend-run-mac.log 2>&1 &)
}

wait_for_port() {
  local port="$1"
  local name="$2"
  for _ in {1..30}; do
    if lsof -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1; then
      return
    fi
    sleep 2
  done
  warn "$name 可能还在启动中，请稍后再看日志。"
}

main() {
  [[ "$(uname -s)" == "Darwin" ]] || die "这个脚本只能在 macOS 上运行。"
  load_brew_env
  configure_java_env
  ensure_docker
  start_mysql

  backend_port="$(detect_backend_port)"
  api_base="http://127.0.0.1:${backend_port}/api"

  start_backend "$backend_port"
  wait_for_port "$backend_port" "后端"

  if [[ ! -d frontend/node_modules ]]; then
    log "首次启动前端，先安装依赖。"
    (cd frontend && npm install)
  fi
  start_frontend "$api_base"
  wait_for_port 15200 "前端"

  cat <<EOF

系统已启动。

前端地址： http://127.0.0.1:15200
后端地址： $api_base

日志文件：
  backend-run-mac.log
  frontend-run-mac.log

测试账号：
  管理员：admin / admin123
  普通用户：2022001 / 123456
  心理老师：T001 / 123456
  待审核老师：T002 / 123456
EOF
}

main "$@"
