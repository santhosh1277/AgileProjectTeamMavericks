package com.example.StudentDashboard.service;

import com.example.StudentDashboard.entity.AcademicProfile;
import com.example.StudentDashboard.entity.CourseRecommender;
import com.example.StudentDashboard.entity.Student;
import com.example.StudentDashboard.entity.UserConsent;
import com.example.StudentDashboard.model.CourseRecommendationRequest;
import com.example.StudentDashboard.repository.AcademicProfileRepository;
import com.example.StudentDashboard.repository.CourseRecommenderRepository;
import com.example.StudentDashboard.repository.StudentRepository;
import com.example.StudentDashboard.repository.UserConsentRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private AcademicProfileRepository academicProfileRepository;
    private CourseRecommenderRepository recommendedCourseRepository;
    private UserConsentRepository _userConsentRepository;
    private Student _student;
    private final String AI_RECOMMEND_URL = "http://127.0.0.1:5000/recommend";

    public StudentService(StudentRepository studentRepository, AcademicProfileRepository academicProfileRepository,
            CourseRecommenderRepository recommendedCourseRepository,UserConsentRepository userConsentRepository) {
        this.studentRepository = studentRepository;
        this.academicProfileRepository = academicProfileRepository;
        this.recommendedCourseRepository = recommendedCourseRepository;
        _userConsentRepository = userConsentRepository;
    }

    public Student registerStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Encrypt password before saving
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        student.setPassword(encoder.encode(student.getPassword()));

        return studentRepository.save(student);
    }

    public Student login(String email, String password) {
        // Find student by email
        Optional<Student> studentOpt = studentRepository.findByEmail(email);

        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        Student student = studentOpt.get();

        // Verify password using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, student.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        _student = student;

        return student;
    }

    public boolean updateStudentDetails(Student updatedStudent) {

        if (updatedStudent == null || updatedStudent.getEmail() == null) {
            return false;
        }
        Optional<Student> existingStudentOpt = studentRepository.findByEmail(updatedStudent.getEmail());

        if (existingStudentOpt.isEmpty()) {
            return false;
        }

        Student existingStudent = existingStudentOpt.get();
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setPassword(updatedStudent.getPassword());
        studentRepository.save(existingStudent);

        return true;
    }

    public Student getStudentDetails(String email) {

        return studentRepository.findByEmail(email)
                .orElse(new Student());
    }

    @Transactional
    public String AddAcademicProfile(AcademicProfile profile) {
        if (profile.getCertifications() == null)
            profile.setCertifications(new ArrayList<>());
        if (profile.getInterests() == null)
            profile.setInterests(new ArrayList<>());

        AcademicProfile existing = academicProfileRepository.findByEmail(profile.getEmail());

        if (existing != null) {
            // Update parent
            existing.setHighestDegree(profile.getHighestDegree());
            existing.setCertifications(profile.getCertifications());
            existing.setInterests(profile.getInterests());

            // Save parent first
            AcademicProfile saved = academicProfileRepository.save(existing);

            return "Academic Profile updated successfully";
        }

        // For new profile: save parent first to generate ID
        AcademicProfile savedProfile = academicProfileRepository.save(profile);

        // Now collections will have profile_id assigned
        return "Academic Profile added successfully";
    }

   public CourseRecommender AddCourseRecommendation(AcademicProfile profile) {
    try {
        if (profile == null) {
            throw new RuntimeException("AcademicProfile not found for email.");
        }

        // 1️⃣ Call AI recommendation service
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send minimal payload (or empty JSON)
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                AI_RECOMMEND_URL,
                HttpMethod.POST,
                request,
                String.class // raw JSON
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException(
                    "Failed to get recommendation from AI. Status: " + response.getStatusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // ignore extra fields
        JsonNode root = mapper.readTree(response.getBody());

        if (root.has("email")) {
            ((ObjectNode) root).remove("email");
        }

        CourseRecommender recommendedCourse = mapper.treeToValue(root, CourseRecommender.class);

        recommendedCourse.setEmail(profile.getEmail());

        return recommendedCourseRepository.save(recommendedCourse);

    } catch (Exception e) {
        throw new RuntimeException("Error adding course recommendation: " + e.getMessage(), e);
    }
}

   public boolean UpdateUserConsent(UserConsent consent) {
    if(consent.getEmail()!=null)
    {
        if(consent.isConsentGiven())
        _userConsentRepository.save(consent);
        return true;
    }
    return false;
   }
}