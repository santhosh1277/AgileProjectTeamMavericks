package com.example.StudentDashboard.service;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private Student testStudent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testStudent = new Student(
                "Santhosh",
                "Reddy",
                LocalDate.of(2000, 1, 1),
                "test@test.com",
                "password123"
        );
    }

    // ----------------- REGISTER ------------------------

    @Test
    void registerStudent_Success() {
        when(studentRepository.existsByEmail(testStudent.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.registerStudent(testStudent);

        assertNotNull(saved);
        assertNotEquals("password123", saved.getPassword()); // Password should be encrypted
        assertTrue(encoder.matches("password123", saved.getPassword()));
    }

    @Test
    void registerStudent_EmailAlreadyExists() {
        when(studentRepository.existsByEmail(testStudent.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.registerStudent(testStudent));

        assertEquals("Email already registered!", ex.getMessage());
    }

    // ----------------- LOGIN ------------------------

    @Test
    void login_Success() {
        String hashed = encoder.encode("password123");
        testStudent.setPassword(hashed);

        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));

        Student loggedIn = studentService.login("test@test.com", "password123");

        assertNotNull(loggedIn);
        assertEquals("test@test.com", loggedIn.getEmail());
    }

    @Test
    void login_InvalidEmail() {
        when(studentRepository.findByEmail("wrong@test.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.login("wrong@test.com", "password123"));

        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    void login_InvalidPassword() {
        testStudent.setPassword(encoder.encode("correctPassword"));
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.login("test@test.com", "wrongPassword"));

        assertEquals("Invalid email or password", ex.getMessage());
    }

    // ----------------- UPDATE ------------------------

    @Test
    void updateStudentDetails_Success() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        testStudent.setFirstName("Updated");
        testStudent.setLastName("Student");
        testStudent.setPassword("newpass");

        boolean updated = studentService.updateStudentDetails(testStudent);

        assertTrue(updated);
        verify(studentRepository, times(1)).save(any(Student.class));
        assertEquals("Updated", testStudent.getFirstName());
    }

    @Test
    void updateStudentDetails_StudentNotFound() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.empty());

        boolean updated = studentService.updateStudentDetails(testStudent);
        assertFalse(updated);
    }

    @Test
    void updateStudentDetails_NullEmail() {
        testStudent.setEmail(null);
        boolean updated = studentService.updateStudentDetails(testStudent);
        assertFalse(updated);
    }

    // ----------------- GET STUDENT ------------------------

    @Test
    void getStudentDetails_Found() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));

        Student fetched = studentService.getStudentDetails(testStudent.getEmail());
        assertEquals(testStudent.getEmail(), fetched.getEmail());
    }

    @Test
    void getStudentDetails_NotFound() {
        when(studentRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        Student fetched = studentService.getStudentDetails("unknown@test.com");
        assertNotNull(fetched); // returns new Student()
        assertNull(fetched.getEmail());
    }

}
