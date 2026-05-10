
INSERT INTO admins (username, password, name) VALUES
('admin', 'admin123', '系统管理员');

INSERT INTO users (student_no, password, name, gender, email, phone, avatar, status) VALUES
('2022001', '123456', '李同学', '女', 'student@example.com', '13800000001', '', 'ACTIVE'),
('2022002', '123456', '王同学', '男', 'wang@example.com', '13800000002', '', 'ACTIVE');

INSERT INTO teachers (job_no, password, name, gender, email, phone, specialty, qualification, introduction, avatar, status) VALUES
('T001', '123456', '陈老师', '女', 'chen@example.com', '13900000001', '情绪压力、学业焦虑', '国家二级心理咨询师', '擅长大学生压力调节与情绪疏导，咨询风格温和清晰。', '', 'APPROVED'),
('T002', '123456', '周老师', '男', 'zhou@example.com', '13900000002', '人际关系、职业规划', '心理健康教育教师', '长期从事校园心理健康教育，关注学生成长与自我探索。', '', 'PENDING');

INSERT INTO announcements (title, introduction, picture, content, status) VALUES
('春季心理健康周活动通知', '学校心理中心将开展心理健康主题活动。', '', '本周三下午将在学生活动中心开展心理健康讲座、团体辅导和压力放松体验活动，欢迎同学参加。', 'PUBLISHED'),
('考前压力调节小贴士', '掌握呼吸、计划和休息方法，缓解考前焦虑。', '', '建议同学们制定复习计划，保持规律作息，适当运动，并通过深呼吸和正念练习缓解紧张情绪。', 'PUBLISHED');

INSERT INTO services (teacher_id, job_no, teacher_name, gender, photo, fee, working_time, teaching_years, introduction, honor) VALUES
(1, 'T001', '陈老师', '女', '', '免费', '周一至周五 14:00-17:00', '8年', '擅长情绪压力与学业焦虑咨询。', '校级优秀心理健康教师'),
(2, 'T002', '周老师', '男', '', '免费', '周二、周四 09:00-11:00', '6年', '擅长人际关系与职业规划咨询。', '心理健康教育先进个人');

INSERT INTO psychological_tests (name, duration_minutes, status, description) VALUES
('大学生心理压力自评量表', 20, 'ACTIVE', '用于简单了解近期压力状态的学生项目测试量表。'),
('情绪状态快速筛查', 15, 'ACTIVE', '用于快速了解近期情绪状态。');

INSERT INTO test_questions (test_id, question_name, options_json, score, answer, analysis, type, sequence_no) VALUES
(1, '最近一周我经常感到学习或生活压力较大。', '[{"label":"很少","value":"A","score":1},{"label":"有时","value":"B","score":2},{"label":"经常","value":"C","score":3}]', 3, 'A', '压力越高越需要及时调整作息和寻求支持。', 'SINGLE', 100),
(1, '我能较好地安排学习任务和休息时间。', '[{"label":"能","value":"A","score":1},{"label":"一般","value":"B","score":2},{"label":"不能","value":"C","score":3}]', 3, 'A', '良好的时间安排有助于降低压力水平。', 'SINGLE', 90),
(1, '遇到困难时，我愿意向朋友、老师或家人求助。', '[{"label":"愿意","value":"A","score":1},{"label":"不确定","value":"B","score":2},{"label":"不愿意","value":"C","score":3}]', 3, 'A', '主动求助是重要的心理保护因素。', 'SINGLE', 80),
(2, '最近我容易情绪低落或烦躁。', '[{"label":"很少","value":"A","score":1},{"label":"有时","value":"B","score":2},{"label":"经常","value":"C","score":3}]', 3, 'A', '如果负面情绪持续出现，建议及时咨询。', 'SINGLE', 100),
(2, '我能从日常生活中感到放松和愉快。', '[{"label":"能","value":"A","score":1},{"label":"一般","value":"B","score":2},{"label":"不能","value":"C","score":3}]', 3, 'A', '保持积极体验有助于稳定情绪。', 'SINGLE', 90);

INSERT INTO appointments (appointment_no, teacher_id, job_no, teacher_name, user_id, student_no, student_name, fee, working_time, appointment_time, note, status, review_reply, is_paid) VALUES
('YY202605100001', 1, 'T001', '陈老师', 1, '2022001', '李同学', '免费', '周一至周五 14:00-17:00', '2026-05-12 15:00:00', '最近考试压力较大，希望预约咨询。', 'APPROVED', '预约通过，请准时参加。', 'UNPAID');

INSERT INTO consultations (teacher_id, job_no, teacher_name, user_id, student_no, student_name, content, status, consultation_date) VALUES
(1, 'T001', '陈老师', 1, '2022001', '李同学', '老师您好，我最近复习效率不高，晚上也睡不好。', 'REPLIED', '2026-05-10');

INSERT INTO consultation_replies (consultation_id, teacher_id, job_no, teacher_name, user_id, student_no, student_name, consultation_content, reply_content, reply_date) VALUES
(1, 1, 'T001', '陈老师', 1, '2022001', '李同学', '老师您好，我最近复习效率不高，晚上也睡不好。', '建议先固定作息时间，把复习任务拆成小块完成。如果持续失眠，可以预约一次正式咨询。', '2026-05-10');

INSERT INTO system_infos (title, subtitle, content, picture1, picture2, picture3) VALUES
('关于在线心理咨询系统', '让校园心理服务更便捷', '本系统面向学生提供心理测试、预约咨询、在线留言咨询和咨询回复等服务，帮助心理老师提升服务效率。', '', '', '');

INSERT INTO system_configs (config_key, config_value, remark) VALUES
('consultation.session.minutes', '45', '默认咨询时长'),
('appointment.review.required', 'true', '预约是否需要老师审核');

