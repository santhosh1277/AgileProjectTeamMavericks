package com.example.StudentDashboard.repository;
import com.example.StudentDashboard.Entity.Student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
}
