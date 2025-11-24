package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false) // IMPORTANT: disables security filters
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------------- SIGNUP TESTS ------------------------------

    @Test
    public void signupSuccess() throws Exception {
        Student input = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");
        Student saved = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");
        saved.setId(1L);

        when(studentService.registerStudent(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void signupMissingFields() throws Exception {
        Student input = new Student();
        input.setEmail(null); // missing email triggers 400

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void signupServiceThrows() throws Exception {
        Student input = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");

        when(studentService.registerStudent(any(Student.class)))
                .thenThrow(new RuntimeException("Database Down"));

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError());
    }

    // -------------------------- LOGIN TESTS ------------------------------

    @Test
    public void loginSuccess() throws Exception {
        Student student = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");
        when(studentService.login(eq("test@test.com"), eq("123"))).thenReturn(student);

        Map<String, String> req = new HashMap<>();
        req.put("usernameOrEmail", "test@test.com");
        req.put("password", "123");

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    public void loginInvalidCredentials() throws Exception {
        when(studentService.login(eq("wrong@test.com"), eq("bad")))
                .thenThrow(new RuntimeException("Invalid email or password"));

        Map<String, String> req = new HashMap<>();
        req.put("usernameOrEmail", "wrong@test.com");
        req.put("password", "bad");

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginMissingFields() throws Exception {
        Map<String, String> req = new HashMap<>();
        req.put("usernameOrEmail", null);
        req.put("password", null);

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginUnexpectedRuntimeException() throws Exception {
        when(studentService.login(any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        Map<String, String> req = new HashMap<>();
        req.put("usernameOrEmail", "test@test.com");
        req.put("password", "123");

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

    // -------------------------- PROFILE TEST ------------------------------

    @Test
    public void profileFetchSuccess() throws Exception {
        Student student = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");

        when(studentService.getStudentDetails(eq("test@test.com")))
                .thenReturn(student);

        Map<String, String> req = new HashMap<>();
        req.put("email", "test@test.com");

        mockMvc.perform(post("/api/students/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    // -------------------------- UPDATE TEST ------------------------------

    @Test
    public void updateSuccess() throws Exception {
        Student updated = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");

        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(true);

        mockMvc.perform(put("/api/students/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student details updated successfully"));
    }

    @Test
    public void updateFailure() throws Exception {
        Student updated = new Student("Santhosh", "Reddy", LocalDate.now(), "test@test.com", "123");

        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(false);

        mockMvc.perform(put("/api/students/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest());
    }
}
