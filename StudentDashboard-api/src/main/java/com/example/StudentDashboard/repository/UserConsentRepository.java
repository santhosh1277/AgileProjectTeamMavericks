package com.example.StudentDashboard.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.StudentDashboard.Entity.UserConsent;
@Repository
public interface UserConsentRepository extends JpaRepository<UserConsent, String> {
    Optional<UserConsent> findByEmailIgnoreCase(String email);
}

