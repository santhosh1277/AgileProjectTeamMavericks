package com.example.StudentDashboard.service;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

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
    void getStudentDetailsFound() {
        when(studentRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testStudent));
        Student student = studentService.getStudentDetails("test@test.com");
        assertEquals("test@test.com", student.getEmail());
    }

    @Test
    void updateStudentSuccess() {
        when(studentRepository.findByEmail(testStudent.getEmail())).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);
        boolean updated = studentService.updateStudentDetails(testStudent);
        assertTrue(updated);
    }
}
