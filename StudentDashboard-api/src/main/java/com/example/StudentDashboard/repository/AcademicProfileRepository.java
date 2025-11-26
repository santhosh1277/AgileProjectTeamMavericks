package com.example.StudentDashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentDashboard.Entity.AcademicProfile;

public interface AcademicProfileRepository extends JpaRepository<AcademicProfile, Long> {
    AcademicProfile findByEmail(String email);
}