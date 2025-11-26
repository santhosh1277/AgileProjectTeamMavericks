package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
