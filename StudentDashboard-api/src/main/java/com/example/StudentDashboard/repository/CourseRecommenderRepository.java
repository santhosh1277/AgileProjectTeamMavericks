package com.example.StudentDashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StudentDashboard.Entity.CourseRecommender;

public interface CourseRecommenderRepository extends JpaRepository<CourseRecommender, Long> {
    CourseRecommender findByEmail(String email);
}