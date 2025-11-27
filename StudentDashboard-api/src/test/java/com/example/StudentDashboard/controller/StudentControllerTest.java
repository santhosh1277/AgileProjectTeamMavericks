package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StudentController.class)
@Disabled("Requires database setup - will be enabled after H2 configuration is added")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
    }

    // ===================== Signup =====================
    @Test
    void testSignupSuccess() throws Exception {
        when(studentService.registerStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testSignupMissingFields() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name, email, and password are required"));
    }

    @Test
    void testSignupException() throws Exception {
        when(studentService.registerStudent(any(Student.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error registering student: DB error"));
    }

    // ===================== Login =====================
    @Test
    void testLoginSuccess() throws Exception {
        student.setPassword(null); // Because controller nullifies password
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail("john@example.com");
        request.setPassword("password123");

        when(studentService.login("john@example.com", "password123")).thenReturn(student);

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testLoginMissingFields() throws Exception {
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail(null);

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email and password are required"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail("john@example.com");
        request.setPassword("wrongpassword");

        when(studentService.login("john@example.com", "wrongpassword"))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLoginOtherException() throws Exception {
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail("john@example.com");
        request.setPassword("password123");

        when(studentService.login("john@example.com", "password123"))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error during login: Unexpected error"));
    }

    // ===================== Update Student =====================
    @Test
    void testUpdateStudentSuccess() throws Exception {
        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(true);

        mockMvc.perform(put("/api/students/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student details updated successfully"));
    }

    @Test
    void testUpdateStudentFailure() throws Exception {
        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(false);

        mockMvc.perform(put("/api/students/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update student details. Please check the input."));
    }

    // ===================== Get Student Profile =====================
    @Test
    void testGetStudentProfileSuccess() throws Exception {
        when(studentService.getStudentDetails("john@example.com")).thenReturn(student);

        Map<String, String> request = new HashMap<>();
        request.put("email", "john@example.com");

        mockMvc.perform(post("/api/students/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetStudentProfileMissingEmail() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", null);

        mockMvc.perform(post("/api/students/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist());
    }
}
