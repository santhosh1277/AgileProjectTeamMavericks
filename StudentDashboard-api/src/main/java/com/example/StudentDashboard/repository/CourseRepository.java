package com.example.StudentDashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentDashboard.Entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
