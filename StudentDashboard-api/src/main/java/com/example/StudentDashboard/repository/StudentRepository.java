package com.example.StudentDashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentDashboard.entity.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);
}
