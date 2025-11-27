package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.Entity.AcademicProfile;
import com.example.StudentDashboard.Entity.CourseRecommender;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.Entity.UserConsent;
import com.example.StudentDashboard.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StudentController.class)
class StudentControllerTestNew {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student testStudent;
    private AcademicProfile testAcademicProfile;
    private CourseRecommender testCourseRecommender;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setPassword("password123");
        testStudent.setDob(LocalDate.of(2000, 1, 1));

        testAcademicProfile = new AcademicProfile();
        testAcademicProfile.setEmail("john@example.com");
        testAcademicProfile.setDegree("Masters");
        testAcademicProfile.setCertifications(new ArrayList<>());
        testAcademicProfile.setInterests(new ArrayList<>());

        testCourseRecommender = new CourseRecommender();
        testCourseRecommender.setCourseName("CS501");
        testCourseRecommender.setDescription("Advanced CS");
        testCourseRecommender.setEmail("john@example.com");
    }

    // ===================== Signup Tests =====================
    @Test
    void testSignupSuccess() throws Exception {
        when(studentService.registerStudent(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentService, times(1)).registerStudent(any());
    }

    @Test
    void testSignupMissingFirstName() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setEmail("test@test.com");
        incompleteStudent.setPassword("pass123");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name, email, and password are required"));
    }

    @Test
    void testSignupMissingLastName() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");
        incompleteStudent.setEmail("test@test.com");
        incompleteStudent.setPassword("pass123");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupMissingEmail() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setPassword("pass123");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupMissingPassword() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setEmail("test@test.com");

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupException() throws Exception {
        when(studentService.registerStudent(any(Student.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error registering student: Database error"));
    }

    // ===================== Login Tests =====================
    @Test
    void testLoginSuccess() throws Exception {
        Student loginStudent = new Student();
        loginStudent.setEmail("john@example.com");
        loginStudent.setPassword(null);

        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail("john@example.com");
        loginRequest.setPassword("password123");

        when(studentService.login("john@example.com", "password123")).thenReturn(loginStudent);

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentService, times(1)).login("john@example.com", "password123");
    }

    @Test
    void testLoginMissingEmail() throws Exception {
        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail(null);
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email and password are required"));
    }

    @Test
    void testLoginMissingPassword() throws Exception {
        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail("john@example.com");
        loginRequest.setPassword(null);

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail("john@example.com");
        loginRequest.setPassword("wrongpassword");

        when(studentService.login("john@example.com", "wrongpassword"))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLoginRuntimeException() throws Exception {
        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail("john@example.com");
        loginRequest.setPassword("password123");

        when(studentService.login("john@example.com", "password123"))
                .thenThrow(new RuntimeException("DB Connection failed"));

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error during login: DB Connection failed"));
    }

    @Test
    void testLoginGeneralException() throws Exception {
        StudentController.LoginRequest loginRequest = new StudentController.LoginRequest();
        loginRequest.setUsernameOrEmail("john@example.com");
        loginRequest.setPassword("password123");

        when(studentService.login("john@example.com", "password123"))
                .thenThrow(new Exception("Unexpected error"));

        mockMvc.perform(post("/api/students/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error during login: Unexpected error"));
    }

    // ===================== Update Student Tests =====================
    @Test
    void testUpdateStudentSuccess() throws Exception {
        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(true);

        mockMvc.perform(put("/api/students/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isOk())
                .andExpect(content().string("Student details updated successfully"));

        verify(studentService, times(1)).updateStudentDetails(any());
    }

    @Test
    void testUpdateStudentFailure() throws Exception {
        when(studentService.updateStudentDetails(any(Student.class))).thenReturn(false);

        mockMvc.perform(put("/api/students/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update student details. Please check the input."));
    }

    // ===================== Get Student Profile Tests =====================
    @Test
    void testGetStudentProfileSuccess() throws Exception {
        when(studentService.getStudentDetails("john@example.com")).thenReturn(testStudent);

        Map<String, String> request = new HashMap<>();
        request.put("email", "john@example.com");

        mockMvc.perform(post("/api/students/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testGetStudentProfileNullEmail() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", null);

        mockMvc.perform(post("/api/students/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist());
    }

    @Test
    void testGetStudentProfileEmptyEmail() throws Exception {
        Student emptyStudent = new Student();
        when(studentService.getStudentDetails("")).thenReturn(emptyStudent);

        Map<String, String> request = new HashMap<>();
        request.put("email", "");

        mockMvc.perform(post("/api/students/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ===================== Academic Profile Tests =====================
    @Test
    void testAddAcademicProfileSuccess() throws Exception {
        List<CourseRecommender> recommendations = new ArrayList<>();
        recommendations.add(testCourseRecommender);

        when(studentService.AddAcademicProfile(any(AcademicProfile.class)))
                .thenReturn(recommendations);

        mockMvc.perform(post("/api/students/academic-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcademicProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(studentService, times(1)).AddAcademicProfile(any());
    }

    @Test
    void testAddAcademicProfileMissingEmail() throws Exception {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail(null);

        mockMvc.perform(post("/api/students/academic-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is required"));

        verify(studentService, times(0)).AddAcademicProfile(any());
    }

    @Test
    void testAddAcademicProfileWithEmail() throws Exception {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("john@example.com");
        profile.setDegree("Masters");

        List<CourseRecommender> recommendations = new ArrayList<>();
        recommendations.add(testCourseRecommender);

        when(studentService.AddAcademicProfile(any(AcademicProfile.class)))
                .thenReturn(recommendations);

        mockMvc.perform(post("/api/students/academic-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk());
    }

    // ===================== Course Recommendations Tests =====================
    @Test
    void testGetCourseRecommendations() throws Exception {
        List<CourseRecommender> recommendations = new ArrayList<>();
        recommendations.add(testCourseRecommender);

        when(studentService.addCourseRecommendations(any(AcademicProfile.class)))
                .thenReturn(recommendations);

        mockMvc.perform(post("/api/students/course-recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcademicProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("CS501"));

        verify(studentService, times(1)).addCourseRecommendations(any());
    }

    @Test
    void testGetCourseRecommendationsEmpty() throws Exception {
        when(studentService.addCourseRecommendations(any(AcademicProfile.class)))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/api/students/course-recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAcademicProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetRecommendedCourses() throws Exception {
        List<CourseRecommender> courses = new ArrayList<>();
        courses.add(testCourseRecommender);

        when(studentService.getRecommendedCourses("john@example.com"))
                .thenReturn(courses);

        Map<String, String> request = new HashMap<>();
        request.put("email", "john@example.com");

        mockMvc.perform(post("/api/students/recommended_courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("CS501"));

        verify(studentService, times(1)).getRecommendedCourses("john@example.com");
    }

    // ===================== User Consent Tests =====================
    @Test
    void testGetUserConsentToCall() throws Exception {
        UserConsent consent = new UserConsent();
        consent.setEmail("john@example.com");
        consent.setConsentGiven(true);

        when(studentService.UpdateUserConsent(any(UserConsent.class))).thenReturn(true);

        mockMvc.perform(post("/api/students/consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(consent)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(studentService, times(1)).UpdateUserConsent(any());
    }

    @Test
    void testGetUserConsentToCallFalse() throws Exception {
        UserConsent consent = new UserConsent();
        consent.setEmail("john@example.com");
        consent.setConsentGiven(false);

        when(studentService.UpdateUserConsent(any(UserConsent.class))).thenReturn(false);

        mockMvc.perform(post("/api/students/consent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(consent)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetUserConsentDetails() throws Exception {
        when(studentService.userConsent("john@example.com")).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("email", "john@example.com");

        mockMvc.perform(post("/api/students/userconsent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(studentService, times(1)).userConsent("john@example.com");
    }

    @Test
    void testGetUserConsentDetailsFalse() throws Exception {
        when(studentService.userConsent("john@example.com")).thenReturn(false);

        Map<String, String> request = new HashMap<>();
        request.put("email", "john@example.com");

        mockMvc.perform(post("/api/students/userconsent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
