package com.example.StudentDashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.StudentDashboard.Entity.CourseRecommender;

@Repository
public interface CourseRecommenderRepository extends JpaRepository<CourseRecommender, Long> {
     List<CourseRecommender> findByEmail(String email);
}