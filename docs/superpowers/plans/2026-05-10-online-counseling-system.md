# Online Counseling System Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a complete Java + Spring Boot + MyBatis + MySQL + Vue online psychological counseling system with seed data, tests, README, and GitHub delivery.

**Architecture:** Monorepo with `backend` REST API, `frontend` Vue SPA, and `database` MySQL scripts. Backend owns business rules and persistence; frontend provides role-based workflows for students, teachers, and administrators.

**Tech Stack:** Java 11, Spring Boot 2.7, MyBatis, MySQL 8, Vue 3, Vite, Element Plus, Axios, Vitest, Maven Wrapper, Docker Compose.

---

### Task 1: Project Foundation

**Files:**
- Create: `.gitignore`
- Create: `docker-compose.yml`
- Create: `database/schema.sql`
- Create: `database/seed.sql`
- Create: `backend/pom.xml`
- Create: `frontend/package.json`

- [ ] Add repository-level ignore rules for generated files, IDE metadata, dependency folders, logs, and local environment files.
- [ ] Add Docker Compose service `mysql` using MySQL 8 with database `counseling_system`.
- [ ] Add backend Maven project with Spring Boot, MyBatis, MySQL connector, validation, test dependencies, and Maven wrapper.
- [ ] Add frontend Vite Vue project with Element Plus, Axios, Vue Router, Pinia, and Vitest.

### Task 2: Backend Data and API

**Files:**
- Create backend package `com.example.counseling`
- Create entities, DTOs, mapper interfaces, XML mappers, services, controllers, and exception handling.

- [ ] Implement consistent response type.
- [ ] Implement auth endpoints for student registration, teacher registration, and role login.
- [ ] Implement announcement endpoints.
- [ ] Implement teacher, service, user, and admin management endpoints.
- [ ] Implement psychological test endpoints, scoring, and record persistence.
- [ ] Implement appointment submit, list, and review endpoints.
- [ ] Implement consultation submit, list, and reply endpoints.
- [ ] Implement system info/config endpoints.

### Task 3: Backend Tests

**Files:**
- Create: `backend/src/test/java/com/example/counseling/CounselingApplicationTests.java`

- [ ] Add integration tests for login, registration, announcements, tests, appointments, consultations, replies, teacher approval, and user status.
- [ ] Run backend test suite with Maven Wrapper.

### Task 4: Frontend Application

**Files:**
- Create Vue app under `frontend/src`

- [ ] Implement API client and auth store.
- [ ] Implement login/register screen.
- [ ] Implement shared dashboard layout with role menus.
- [ ] Implement student dashboard, announcements, tests, services, appointments, consultations, replies.
- [ ] Implement teacher dashboard, appointment review, consultation reply, test management, records.
- [ ] Implement admin dashboard, users, teachers, announcements, tests, appointments, consultations, system info.
- [ ] Implement polished CSS theme and responsive layout.

### Task 5: Frontend Tests and Browser QA

**Files:**
- Create: `frontend/src/**/*.test.js`

- [ ] Add unit tests for auth store, API response helpers, and score display helpers.
- [ ] Run frontend build and tests.
- [ ] Start backend and frontend, run browser smoke test across all roles.

### Task 6: Documentation and GitHub

**Files:**
- Create: `README.md`
- Create: `docs/testing-report.md`

- [ ] Document features, tech stack, project structure, database setup, startup commands, test accounts, API notes, and testing report.
- [ ] Commit all source code.
- [ ] Create GitHub repository with GitHub CLI and push code.
