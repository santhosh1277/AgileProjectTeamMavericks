package com.example.StudentDashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentDashboard.entity.AcademicProfile;



public interface AcademicProfileRepository extends JpaRepository<AcademicProfile, Long> {
    AcademicProfile findByEmail(String email);
}