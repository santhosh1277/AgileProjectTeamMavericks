package com.example.StudentDashboard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.StudentDashboard.Entity.AcademicProfile;
import com.example.StudentDashboard.Entity.CourseRecommender;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.Entity.UserConsent;
import com.example.StudentDashboard.service.StudentService;
import com.example.StudentDashboard.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StudentController.class)
@Disabled("Spring context loading issues - awaiting resolution")
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
    void testSignupMissingFirstName() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName(null);
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setEmail("john@example.com");
        incompleteStudent.setPassword("password123");

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
        incompleteStudent.setLastName(null);
        incompleteStudent.setEmail("john@example.com");
        incompleteStudent.setPassword("password123");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name, email, and password are required"));
    }

    @Test
    void testSignupMissingEmail() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setEmail(null);
        incompleteStudent.setPassword("password123");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incompleteStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Name, email, and password are required"));
    }

    @Test
    void testSignupMissingPassword() throws Exception {
        Student incompleteStudent = new Student();
        incompleteStudent.setFirstName("John");
        incompleteStudent.setLastName("Doe");
        incompleteStudent.setEmail("john@example.com");
        incompleteStudent.setPassword(null);

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

    @Test
    void testSignupDuplicateEmail() throws Exception {
        when(studentService.registerStudent(any(Student.class)))
                .thenThrow(new RuntimeException("Email already registered!"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error registering student: Email already registered!"));
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
    void testLoginMissingEmail() throws Exception {
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail(null);
        request.setPassword("password123");

        mockMvc.perform(post("/api/students/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email and password are required"));
    }

    @Test
    void testLoginMissingPassword() throws Exception {
        StudentController.LoginRequest request = new StudentController.LoginRequest();
        request.setUsernameOrEmail("john@example.com");
        request.setPassword(null);

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

    @Test
    void testGetStudentProfileEmptyRequest() throws Exception {
        Map<String, String> request = new HashMap<>();

        mockMvc.perform(post("/api/students/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist());
    }

    // ===================== Academic Profile =====================
    @Test
    void testAddAcademicProfileSuccess() throws Exception {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");

        List<CourseRecommender> recommendations = new ArrayList<>();
        CourseRecommender course = new CourseRecommender();
        course.setCourseName("MSc Data Science");
        recommendations.add(course);

        when(studentService.AddAcademicProfile(any(AcademicProfile.class))).thenReturn(recommendations);

        mockMvc.perform(post("/api/students/academic-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testAddAcademicProfileMissingEmail() throws Exception {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail(null);
        profile.setDegree("BTech");

        mockMvc.perform(post("/api/students/academic-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is required"));
    }

    // ===================== Course Recommendations =====================
    @Test
    void testGetCourseRecommendations() throws Exception {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");

        List<CourseRecommender> recommendations = new ArrayList<>();
        CourseRecommender course1 = new CourseRecommender();
        course1.setCourseName("MSc Data Science");

        CourseRecommender course2 = new CourseRecommender();
        course2.setCourseName("MSc AI");

        recommendations.add(course1);
        recommendations.add(course2);

        when(studentService.addCourseRecommendations(any(AcademicProfile.class))).thenReturn(recommendations);

        mockMvc.perform(post("/api/students/course-recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetRecommendedCourses() throws Exception {
        List<CourseRecommender> courses = new ArrayList<>();
        CourseRecommender course1 = new CourseRecommender();
        course1.setCourseName("MSc Data Science");
        course1.setEmail("student@test.com");

        CourseRecommender course2 = new CourseRecommender();
        course2.setCourseName("MSc AI");
        course2.setEmail("student@test.com");

        courses.add(course1);
        courses.add(course2);

        when(studentService.getRecommendedCourses("student@test.com")).thenReturn(courses);

        Map<String, String> request = new HashMap<>();
        request.put("email", "student@test.com");

        mockMvc.perform(post("/api/students/recommended_courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetRecommendedCoursesEmpty() throws Exception {
        when(studentService.getRecommendedCourses("student@test.com")).thenReturn(new ArrayList<>());

        Map<String, String> request = new HashMap<>();
        request.put("email", "student@test.com");

        mockMvc.perform(post("/api/students/recommended_courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ===================== User Consent =====================
    @Test
    void testUpdateUserConsentSuccess() throws Exception {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@test.com");
        consent.setConsentGiven(true);

        when(studentService.UpdateUserConsent(any(UserConsent.class))).thenReturn(true);

        mockMvc.perform(post("/api/students/consent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consent)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUserConsentFailure() throws Exception {
        UserConsent consent = new UserConsent();
        consent.setEmail(null);
        consent.setConsentGiven(true);

        when(studentService.UpdateUserConsent(any(UserConsent.class))).thenReturn(false);

        mockMvc.perform(post("/api/students/consent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consent)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserConsentDetailsTrue() throws Exception {
        when(studentService.userConsent("student@test.com")).thenReturn(true);

        Map<String, String> request = new HashMap<>();
        request.put("email", "student@test.com");

        mockMvc.perform(post("/api/students/userconsent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserConsentDetailsFalse() throws Exception {
        when(studentService.userConsent("student@test.com")).thenReturn(false);

        Map<String, String> request = new HashMap<>();
        request.put("email", "student@test.com");

        mockMvc.perform(post("/api/students/userconsent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

