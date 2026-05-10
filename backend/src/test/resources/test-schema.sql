
DROP TABLE IF EXISTS consultation_replies;
DROP TABLE IF EXISTS consultations;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS test_records;
DROP TABLE IF EXISTS test_questions;
DROP TABLE IF EXISTS psychological_tests;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS system_configs;
DROP TABLE IF EXISTS system_infos;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  student_no VARCHAR(30) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  gender VARCHAR(10),
  email VARCHAR(80),
  phone VARCHAR(30),
  avatar TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE teachers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  job_no VARCHAR(30) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  gender VARCHAR(10),
  email VARCHAR(80),
  phone VARCHAR(30),
  specialty VARCHAR(120),
  qualification TEXT,
  introduction TEXT,
  avatar TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE announcements (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(120) NOT NULL,
  introduction TEXT,
  picture TEXT,
  content TEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE services (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  teacher_id BIGINT NOT NULL,
  job_no VARCHAR(30) NOT NULL,
  teacher_name VARCHAR(50) NOT NULL,
  gender VARCHAR(10),
  photo TEXT,
  fee VARCHAR(50),
  working_time VARCHAR(80),
  teaching_years VARCHAR(50),
  introduction TEXT,
  honor TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_services_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE psychological_tests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  duration_minutes INT NOT NULL DEFAULT 20,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE test_questions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  test_id BIGINT NOT NULL,
  question_name VARCHAR(255) NOT NULL,
  options_json TEXT NOT NULL,
  score BIGINT NOT NULL DEFAULT 0,
  answer VARCHAR(100) NOT NULL,
  analysis TEXT,
  type VARCHAR(20) NOT NULL DEFAULT 'SINGLE',
  sequence_no BIGINT NOT NULL DEFAULT 100,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_questions_test FOREIGN KEY (test_id) REFERENCES psychological_tests(id) ON DELETE CASCADE
);

CREATE TABLE test_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  username VARCHAR(50) NOT NULL,
  test_id BIGINT NOT NULL,
  test_name VARCHAR(100) NOT NULL,
  total_score BIGINT NOT NULL DEFAULT 0,
  result_level VARCHAR(50) NOT NULL,
  answers_json TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_records_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_records_test FOREIGN KEY (test_id) REFERENCES psychological_tests(id)
);

CREATE TABLE appointments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  appointment_no VARCHAR(50) NOT NULL UNIQUE,
  teacher_id BIGINT NOT NULL,
  job_no VARCHAR(30) NOT NULL,
  teacher_name VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  student_no VARCHAR(30) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  fee VARCHAR(50),
  working_time VARCHAR(80),
  appointment_time DATETIME NOT NULL,
  note VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  review_reply TEXT,
  is_paid VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_appointments_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id),
  CONSTRAINT fk_appointments_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE consultations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  teacher_id BIGINT NOT NULL,
  job_no VARCHAR(30) NOT NULL,
  teacher_name VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  student_no VARCHAR(30) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  content TEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  consultation_date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_consultations_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id),
  CONSTRAINT fk_consultations_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE consultation_replies (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  consultation_id BIGINT NOT NULL,
  teacher_id BIGINT NOT NULL,
  job_no VARCHAR(30) NOT NULL,
  teacher_name VARCHAR(50) NOT NULL,
  user_id BIGINT NOT NULL,
  student_no VARCHAR(30) NOT NULL,
  student_name VARCHAR(50) NOT NULL,
  consultation_content TEXT NOT NULL,
  reply_content TEXT NOT NULL,
  reply_date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_replies_consultation FOREIGN KEY (consultation_id) REFERENCES consultations(id) ON DELETE CASCADE
);

CREATE TABLE system_infos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(120) NOT NULL,
  subtitle VARCHAR(120),
  content TEXT NOT NULL,
  picture1 TEXT,
  picture2 TEXT,
  picture3 TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE system_configs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  config_key VARCHAR(80) NOT NULL UNIQUE,
  config_value VARCHAR(255) NOT NULL,
  remark VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

