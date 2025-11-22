package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"null", "removal"})
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    @DisplayName("Signup returns 201 when student is saved")
    void signupSuccess() throws Exception {
        Student payload = new Student();
        payload.setFirstName("John");
        payload.setLastName("play");
        payload.setEmail("john@example.com");
        payload.setPassword("pass123");

        Student saved = new Student();
        saved.setId(1L);
        saved.setFirstName("John");
        saved.setLastName("Play");
        saved.setEmail("john@example.com");
        saved.setPassword("hidden");

        when(studentService.registerStudent(any(Student.class))).thenReturn(saved);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentService).registerStudent(any(Student.class));
    }

    @Test
    @DisplayName("Signup returns 400 when mandatory fields missing")
    void signupMissingFields() throws Exception {
        Student payload = new Student();
        payload.setEmail("john@example.com");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).registerStudent(any(Student.class));
    }

    @Test
    @DisplayName("Signup returns 500 when service fails")
    void signupServiceThrows() throws Exception {
        Student payload = new Student();
        payload.setFirstName("John");
        payload.setLastName("Play");
        payload.setEmail("john@example.com");
        payload.setPassword("pass123");

        when(studentService.registerStudent(any(Student.class)))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Login returns 200 with sanitized student data")
    void loginSuccess() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("John");
        student.setEmail("john@example.com");
        student.setPassword("secret");

        when(studentService.login("john@example.com", "pass123")).thenReturn(student);

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"john@example.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("Login returns 401 when credentials are invalid")
    void loginInvalidCredentials() throws Exception {
        when(studentService.login("john@example.com", "badpass"))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"john@example.com\",\"password\":\"badpass\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login returns 400 when payload is incomplete")
    void loginMissingFields() throws Exception {
        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"john@example.com\"}"))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).login(anyString(), anyString());
    }

    @Test
    @DisplayName("Login returns 500 when unexpected runtime exception occurs")
    void loginUnexpectedRuntimeException() throws Exception {
        when(studentService.login("john@example.com", "pass123"))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"john@example.com\",\"password\":\"pass123\"}"))
                .andExpect(status().isInternalServerError());
    }
}


