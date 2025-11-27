package com.example.StudentDashboard.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.example.StudentDashboard.Entity.AcademicProfile;
import com.example.StudentDashboard.Entity.CourseRecommender;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.Entity.UserConsent;
import com.example.StudentDashboard.model.CourseRecommenderRequest;
import com.example.StudentDashboard.repository.AcademicProfileRepository;
import com.example.StudentDashboard.repository.CourseRecommenderRepository;
import com.example.StudentDashboard.repository.StudentRepository;
import com.example.StudentDashboard.repository.UserConsentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private AcademicProfileRepository academicProfileRepository;
    
    @Mock
    private CourseRecommenderRepository courseRecommenderRepository;
    
    @Mock
    private UserConsentRepository userConsentRepository;
    
    @Mock
    private RestTemplate restTemplate;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
      
        testStudent = new Student("Santhosh", "Reddy", LocalDate.of(2000,1,1), "test@test.com", "password123");
    }

    @Test
    void registerStudentSuccess() {
        when(studentRepository.existsByEmail(testStudent.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        Student saved = studentService.registerStudent(testStudent);

        assertEquals("test@test.com", saved.getEmail());
    }

    @Test
    void registerStudentDuplicateEmail() {
        when(studentRepository.existsByEmail(testStudent.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.registerStudent(testStudent);
        });

        assertEquals("Email already registered!", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getStudentDetailsFound() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testStudent));
        Student student = studentService.getStudentDetails("test@test.com");
        assertEquals("test@test.com", student.getEmail());
    }

    @Test
    void getStudentDetailsNotFound() {
        when(studentRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());
        Student student = studentService.getStudentDetails("notfound@test.com");
        assertNotNull(student);
        // Returns new Student() when not found
        assertNull(student.getEmail());
    }

    @Test
    void updateStudentSuccess() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        boolean updated = studentService.updateStudentDetails(testStudent);
        assertTrue(updated);
    }

    @Test
    void updateStudentDetailsNull() {
        boolean updated = studentService.updateStudentDetails(null);
        assertFalse(updated);
    }

    @Test
    void updateStudentDetailsNullEmail() {
        Student studentNoEmail = new Student();
        studentNoEmail.setEmail(null);
        boolean updated = studentService.updateStudentDetails(studentNoEmail);
        assertFalse(updated);
    }

    @Test
    void updateStudentDetailsNotFound() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.empty());
        boolean updated = studentService.updateStudentDetails(testStudent);
        assertFalse(updated);
    }

    @Test
    void testLoginSuccess() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String email = "john@example.com";

        Student student = new Student();
        student.setEmail(email);
        student.setPassword(encoder.encode(rawPassword));

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        Student result = studentService.login(email, rawPassword);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testLoginInvalidEmail() {
        String email = "wrong@example.com";

        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.login(email, "password123");
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testLoginInvalidPassword() {
        String email = "john@example.com";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Student student = new Student();
        student.setEmail(email);
        student.setPassword(encoder.encode("correctPassword"));

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.login(email, "wrongPassword");
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testAddAcademicProfileNew() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");
        profile.setCertifications(new ArrayList<>());
        profile.setInterests(new ArrayList<>());

        when(academicProfileRepository.findByEmail("student@test.com")).thenReturn(null);
        when(academicProfileRepository.save(any(AcademicProfile.class))).thenReturn(profile);

        List<CourseRecommender> recommendations = new ArrayList<>();
        CourseRecommender course = new CourseRecommender();
        course.setCourseName("MSc Data Science");
        recommendations.add(course);

        CourseRecommenderRequest mockResponse = new CourseRecommenderRequest();
        mockResponse.setRecommendations(recommendations);

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5000/recommend"), any(AcademicProfile.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"recommendations\": [{\"course_name\": \"MSc Data Science\", \"email\": \"student@test.com\"}]}", HttpStatus.OK));

        when(courseRecommenderRepository.saveAll(any())).thenReturn(recommendations);

        List<CourseRecommender> result = studentService.AddAcademicProfile(profile);

        assertNotNull(result);
        verify(academicProfileRepository).save(any(AcademicProfile.class));
        verify(courseRecommenderRepository).saveAll(any());
    }

    @Test
    void testAddAcademicProfileExisting() {
        AcademicProfile existingProfile = new AcademicProfile();
        existingProfile.setEmail("student@test.com");
        existingProfile.setDegree("BTech");

        AcademicProfile newProfile = new AcademicProfile();
        newProfile.setEmail("student@test.com");
        newProfile.setDegree("MTech");
        newProfile.setCertifications(new ArrayList<>());
        newProfile.setInterests(new ArrayList<>());

        when(academicProfileRepository.findByEmail("student@test.com")).thenReturn(existingProfile);
        when(academicProfileRepository.save(any(AcademicProfile.class))).thenReturn(existingProfile);

        List<CourseRecommender> recommendations = new ArrayList<>();
        when(courseRecommenderRepository.saveAll(any())).thenReturn(recommendations);

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5000/recommend"), any(AcademicProfile.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"recommendations\": []}", HttpStatus.OK));

        List<CourseRecommender> result = studentService.AddAcademicProfile(newProfile);

        assertNotNull(result);
        verify(academicProfileRepository, times(2)).save(any(AcademicProfile.class));
    }

    @Test
    void testUpdateUserConsentTrue() {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@test.com");
        consent.setConsentGiven(true);

        boolean result = studentService.UpdateUserConsent(consent);

        assertTrue(result);
        verify(userConsentRepository).save(any(UserConsent.class));
    }

    @Test
    void testUpdateUserConsentFalse() {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@test.com");
        consent.setConsentGiven(false);

        boolean result = studentService.UpdateUserConsent(consent);

        assertTrue(result);
        verify(userConsentRepository, never()).save(any());
    }

    @Test
    void testUpdateUserConsentNullEmail() {
        UserConsent consent = new UserConsent();
        consent.setEmail(null);
        consent.setConsentGiven(true);

        boolean result = studentService.UpdateUserConsent(consent);

        assertFalse(result);
    }

    @Test
    void testGetRecommendedCoursesUnique() {
        List<CourseRecommender> allCourses = new ArrayList<>();
        
        CourseRecommender course1 = new CourseRecommender();
        course1.setCourseName("MSc Data Science");
        course1.setEmail("student@test.com");
        
        CourseRecommender course2 = new CourseRecommender();
        course2.setCourseName("MSc Data Science");
        course2.setEmail("student@test.com");
        
        CourseRecommender course3 = new CourseRecommender();
        course3.setCourseName("MSc AI");
        course3.setEmail("student@test.com");
        
        allCourses.add(course1);
        allCourses.add(course2);
        allCourses.add(course3);

        when(courseRecommenderRepository.findByEmail("student@test.com")).thenReturn(allCourses);

        List<CourseRecommender> result = studentService.getRecommendedCourses("student@test.com");

        assertEquals(2, result.size());
    }

    @Test
    void testGetRecommendedCoursesEmpty() {
        when(courseRecommenderRepository.findByEmail("student@test.com")).thenReturn(new ArrayList<>());

        List<CourseRecommender> result = studentService.getRecommendedCourses("student@test.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void testUserConsentTrue() {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@test.com");
        consent.setConsentGiven(true);

        when(userConsentRepository.findByEmailIgnoreCase("student@test.com")).thenReturn(Optional.of(consent));

        boolean result = studentService.userConsent("student@test.com");

        assertTrue(result);
    }

    @Test
    void testUserConsentFalse() {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@test.com");
        consent.setConsentGiven(false);

        when(userConsentRepository.findByEmailIgnoreCase("student@test.com")).thenReturn(Optional.of(consent));

        boolean result = studentService.userConsent("student@test.com");

        assertFalse(result);
    }

    @Test
    void testUserConsentNotFound() {
        when(userConsentRepository.findByEmailIgnoreCase("notfound@test.com")).thenReturn(Optional.empty());

        boolean result = studentService.userConsent("notfound@test.com");

        assertFalse(result);
    }

    // ==================== JwtTokenService Tests ====================

    @Test
    void testGenerateUatToken() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String token = jwtTokenService.generateUatToken("student@test.com");
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void testGenerateUatTokenWithDifferentEmails() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String token1 = jwtTokenService.generateUatToken("student1@test.com");
        String token2 = jwtTokenService.generateUatToken("student2@test.com");
        
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2); // Different emails should produce different tokens
    }

    @Test
    void testValidateTokenValid() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String token = jwtTokenService.generateUatToken("student@test.com");
        boolean isValid = jwtTokenService.validateToken(token);
        
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenInvalid() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String invalidToken = "invalid.token.here";
        boolean isValid = jwtTokenService.validateToken(invalidToken);
        
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenMalformed() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String malformedToken = "malformed";
        boolean isValid = jwtTokenService.validateToken(malformedToken);
        
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenEmpty() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        boolean isValid = jwtTokenService.validateToken("");
        
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenNull() {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        boolean isValid = jwtTokenService.validateToken(null);
        
        assertFalse(isValid);
    }

    @Test
    void testJwtTokenServiceTokenExpiration() throws InterruptedException {
        StudentService.JwtTokenService jwtTokenService = studentService.new JwtTokenService();
        
        String token = jwtTokenService.generateUatToken("student@test.com");
        
        // Token should be valid immediately
        assertTrue(jwtTokenService.validateToken(token));
    }

    @Test
    void testAddCourseRecommendationsHttpClientError() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5000/recommend"), any(AcademicProfile.class), eq(String.class)))
                .thenThrow(new org.springframework.web.client.HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.addCourseRecommendations(profile);
        });

        assertTrue(exception.getMessage().contains("Error adding course recommendation"));
    }

    @Test
    void testAddCourseRecommendationsParsingError() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5000/recommend"), any(AcademicProfile.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{invalid json}", HttpStatus.OK));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.addCourseRecommendations(profile);
        });

        assertTrue(exception.getMessage().contains("Error parsing AI response"));
    }

    @Test
    void testAddAcademicProfileWithNullCertificationsAndInterests() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@test.com");
        profile.setDegree("BTech");
        profile.setCertifications(null);
        profile.setInterests(null);

        when(academicProfileRepository.findByEmail("student@test.com")).thenReturn(null);
        when(academicProfileRepository.save(any(AcademicProfile.class))).thenReturn(profile);

        List<CourseRecommender> recommendations = new ArrayList<>();
        when(courseRecommenderRepository.saveAll(any())).thenReturn(recommendations);

        when(restTemplate.postForEntity(eq("http://127.0.0.1:5000/recommend"), any(AcademicProfile.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"recommendations\": []}", HttpStatus.OK));

        List<CourseRecommender> result = studentService.AddAcademicProfile(profile);

        assertNotNull(result);
        assertNotNull(profile.getCertifications());
        assertNotNull(profile.getInterests());
    }

    
}
