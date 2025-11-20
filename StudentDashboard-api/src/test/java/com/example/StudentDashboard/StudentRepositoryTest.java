package com.example.StudentDashboard;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void testSaveAndFindStudent() {
        Student student = new Student( "John Doe", "2000-01-01", "john@example.com", "pass123");
        studentRepository.save(student);

        boolean exists = studentRepository.existsByEmail("john@example.com");
        assertThat(exists).isTrue();
    }
}
