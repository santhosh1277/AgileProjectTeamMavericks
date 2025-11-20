package com.example.StudentDashboard;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;
import com.example.StudentDashboard.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateStudentDetailsSuccess() {
        Student student = new Student("Jane Doe", "2001-02-02", "jane@example.com", "pass123");
        student.setId(1L);  

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        boolean result = studentService.updateStudentDetails(student);

        assertTrue(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testUpdateStudentDetailsFailIfStudentNotFound() {
        Student student = new Student("Ghost", "1990-01-01", "ghost@example.com", "pass");

        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = studentService.updateStudentDetails(student);

        assertFalse(result);
        verify(studentRepository, never()).save(student);
    }
}
