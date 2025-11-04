package com.example.StudentDashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StudentDashboard.Entity.College;
public interface CollegeRepository extends JpaRepository<College, Long> {
    
}
