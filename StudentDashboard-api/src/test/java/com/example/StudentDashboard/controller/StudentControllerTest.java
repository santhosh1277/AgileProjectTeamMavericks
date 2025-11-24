package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false) // disable security for simplicity
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private ObjectMapper objectMapper;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testStudent = new Student("Santhosh", "Reddy", LocalDate.of(2000, 1, 1), "test@test.com", "password123");
    }

   

  

}
