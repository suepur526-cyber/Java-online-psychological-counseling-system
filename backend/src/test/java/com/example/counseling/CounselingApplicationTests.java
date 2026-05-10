package com.example.counseling;

import com.example.counseling.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CounselingApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void logsInAllRolesAndRejectsPendingTeacher() throws Exception {
        login("admin", "admin123", "ADMIN")
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
        login("2022001", "123456", "USER")
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("USER"));
        login("T001", "123456", "TEACHER")
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.role").value("TEACHER"));
        login("T002", "123456", "TEACHER")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void registersStudentAndTeacher() throws Exception {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setStudentNo("2022999");
        user.setPassword("123456");
        user.setName("测试学生");
        user.setGender("女");
        user.setEmail("new@example.com");
        user.setPhone("13811112222");

        mockMvc.perform(post("/api/auth/register/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.studentNo").value("2022999"));

        RegisterTeacherRequest teacher = new RegisterTeacherRequest();
        teacher.setJobNo("T999");
        teacher.setPassword("123456");
        teacher.setName("测试老师");
        teacher.setSpecialty("压力疏导");

        mockMvc.perform(post("/api/auth/register/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    void managesAnnouncements() throws Exception {
        mockMvc.perform(get("/api/announcements").param("publicOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(2))));

        Map<String, Object> announcement = new HashMap<>();
        announcement.put("title", "测试公告");
        announcement.put("introduction", "公告简介");
        announcement.put("picture", "");
        announcement.put("content", "公告内容");
        announcement.put("status", "PUBLISHED");

        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(announcement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("测试公告"));
    }

    @Test
    void submitsPsychologicalTestAndCreatesRecord() throws Exception {
        Map<Long, String> answers = new HashMap<>();
        answers.put(1L, "A");
        answers.put(2L, "B");
        answers.put(3L, "C");
        TestSubmitRequest request = new TestSubmitRequest();
        request.setUserId(1L);
        request.setAnswers(answers);

        mockMvc.perform(post("/api/tests/1/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalScore").value(6))
                .andExpect(jsonPath("$.data.resultLevel").isNotEmpty());

        mockMvc.perform(get("/api/tests/records").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].testId").value(1));
    }

    @Test
    void submitsAndReviewsAppointment() throws Exception {
        AppointmentRequest request = new AppointmentRequest();
        request.setUserId(1L);
        request.setTeacherId(1L);
        request.setAppointmentTime("2026-05-20 15:00:00");
        request.setNote("希望预约一次压力疏导");

        String response = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).path("data").path("id").asLong();

        ReviewRequest review = new ReviewRequest();
        review.setStatus("APPROVED");
        review.setReviewReply("预约通过");
        mockMvc.perform(put("/api/appointments/" + id + "/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    void submitsConsultationAndTeacherReply() throws Exception {
        ConsultationRequest request = new ConsultationRequest();
        request.setUserId(1L);
        request.setTeacherId(1L);
        request.setContent("最近学习状态不稳定，希望得到建议。");

        String response = mockMvc.perform(post("/api/consultations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).path("data").path("id").asLong();

        ReplyRequest reply = new ReplyRequest();
        reply.setReplyContent("建议先记录每日状态，并保持规律作息。");
        mockMvc.perform(post("/api/consultations/" + id + "/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reply)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.replyContent").value("建议先记录每日状态，并保持规律作息。"));

        mockMvc.perform(get("/api/consultations/replies").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void adminUpdatesTeacherAndUserStatus() throws Exception {
        mockMvc.perform(put("/api/teachers/2/status").param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        login("T002", "123456", "TEACHER")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(put("/api/users/2/status").param("status", "DISABLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        login("2022002", "123456", "USER")
                .andExpect(status().isBadRequest());
    }

    private org.springframework.test.web.servlet.ResultActions login(String account, String password, String role) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount(account);
        request.setPassword(password);
        request.setRole(role);
        return mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
}
