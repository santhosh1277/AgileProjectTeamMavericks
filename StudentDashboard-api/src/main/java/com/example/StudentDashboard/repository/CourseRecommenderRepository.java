package com.example.StudentDashboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.StudentDashboard.entity.CourseRecommender;

@Repository
public interface CourseRecommenderRepository extends JpaRepository<CourseRecommender, Long> {
     Optional<CourseRecommender> findByEmail(String email);
}