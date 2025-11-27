package com.example.StudentDashboard.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentDashboard.entity.College;
public interface CollegeRepository extends JpaRepository<College, Long> {
	Optional<College> findByNameAndCountry(String name, String country);
}
