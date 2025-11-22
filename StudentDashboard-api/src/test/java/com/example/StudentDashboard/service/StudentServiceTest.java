package com.example.StudentDashboard.service;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Signup hashes password and saves student when email is new")
    void registerStudent_successfulSignup() {
        Student student = new Student();
        student.setFirstName("Test User");
        student.setLastName("tom");
        student.setEmail("test@example.com");
        student.setPassword("plainPassword");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0, Student.class));

        Student savedStudent = studentService.registerStudent(student);

        assertNotNull(savedStudent);
        verify(studentRepository).save(any(Student.class));
        assertNotEquals("plainPassword", savedStudent.getPassword(), "Password should be hashed");
        assertTrue(encoder.matches("plainPassword", savedStudent.getPassword()), "Hashed password should match raw password");
    }

    @Test
    @DisplayName("Signup throws when email already exists")
    void registerStudent_duplicateEmailThrows() {
        Student student = new Student();
        student.setEmail("existing@example.com");
        student.setPassword("password");

        when(studentRepository.existsByEmail(student.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.registerStudent(student));
        assertEquals("Email already registered!", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Login succeeds with valid credentials")
    void login_successful() {
        String email = "user@example.com";
        String rawPassword = "securePass";

        Student storedStudent = new Student();
        storedStudent.setEmail(email);
        storedStudent.setPassword(encoder.encode(rawPassword));

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(storedStudent));

        Student result = studentService.login(email, rawPassword);

        assertSame(storedStudent, result);
    }

    @Test
    @DisplayName("Login throws when email not found")
    void login_emailNotFoundThrows() {
        when(studentRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.login("missing@example.com", "password"));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Login throws when password is incorrect")
    void login_incorrectPasswordThrows() {
        String email = "user@example.com";

        Student storedStudent = new Student();
        storedStudent.setEmail(email);
        storedStudent.setPassword(encoder.encode("correctPassword"));

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(storedStudent));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studentService.login(email, "wrongPassword"));
        assertEquals("Invalid email or password", exception.getMessage());
    }
}


