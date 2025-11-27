package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setDob(LocalDate.of(2000, 1, 1));
        testStudent.setEmail("john@test.com");
        testStudent.setPassword("hashedPassword123");
    }

    @Test
    void testSaveStudent() {
        Student saved = studentRepository.save(testStudent);
        assertNotNull(saved.getId());
        assertEquals("john@test.com", saved.getEmail());
    }

    @Test
    void testFindByEmailFound() {
        studentRepository.save(testStudent);
        Optional<Student> found = studentRepository.findByEmail("john@test.com");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<Student> found = studentRepository.findByEmail("notexist@test.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByEmailTrue() {
        studentRepository.save(testStudent);
        boolean exists = studentRepository.existsByEmail("john@test.com");
        assertTrue(exists);
    }

    @Test
    void testExistsByEmailFalse() {
        boolean exists = studentRepository.existsByEmail("notexist@test.com");
        assertFalse(exists);
    }

    @Test
    void testUpdateStudent() {
        Student saved = studentRepository.save(testStudent);
        saved.setFirstName("Jane");
        Student updated = studentRepository.save(saved);
        assertEquals("Jane", updated.getFirstName());
    }

    @Test
    void testDeleteStudent() {
        Student saved = studentRepository.save(testStudent);
        Long id = saved.getId();
        studentRepository.deleteById(id);
        Optional<Student> deleted = studentRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindAllStudents() {
        studentRepository.save(testStudent);
        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setDob(LocalDate.of(2001, 3, 10));
        student2.setEmail("jane@test.com");
        student2.setPassword("password123");
        studentRepository.save(student2);
        
        assertTrue(studentRepository.findAll().size() >= 2);
    }
}
