import { defineConfig } from '@playwright/test'

export default defineConfig({
  testDir: './tests',
  timeout: 45000,
  workers: 1,
  webServer: {
    command: 'npm run dev -- --host 127.0.0.1 --port 15200 --strictPort',
    url: 'http://127.0.0.1:15200',
    reuseExistingServer: true,
    timeout: 30000,
    env: {
      VITE_API_BASE: 'http://127.0.0.1:18100/api',
    },
  },
  use: {
    baseURL: 'http://127.0.0.1:15200',
    trace: 'retain-on-failure',
    screenshot: 'only-on-failure',
  },
})
