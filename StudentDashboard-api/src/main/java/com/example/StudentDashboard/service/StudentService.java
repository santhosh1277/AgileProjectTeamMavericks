package com.example.StudentDashboard.service;

import com.example.StudentDashboard.entity.AcademicProfile;
import com.example.StudentDashboard.entity.CourseRecommender;
import com.example.StudentDashboard.entity.Student;
import com.example.StudentDashboard.repository.AcademicProfileRepository;
import com.example.StudentDashboard.repository.CourseRecommenderRepository;
import com.example.StudentDashboard.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private AcademicProfileRepository academicProfileRepository;
    private CourseRecommenderRepository recommendedCourseRepository;
    private Student _student;
    private final String AI_RECOMMEND_URL = "http://127.0.0.1:5000/recommend";

    public StudentService(StudentRepository studentRepository, AcademicProfileRepository academicProfileRepository,
            CourseRecommenderRepository recommendedCourseRepository) {
        this.studentRepository = studentRepository;
        this.academicProfileRepository = academicProfileRepository;
        this.recommendedCourseRepository = recommendedCourseRepository;
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

    public AcademicProfile AddAcademicProfile(AcademicProfile profile) {
        AcademicProfile existing = academicProfileRepository.findByEmail(profile.getEmail());

        if (existing != null) {

            existing.setHighestDegree(profile.getHighestDegree());
            existing.setCertifications(profile.getCertifications());
            existing.setInterests(profile.getInterests());

            return academicProfileRepository.save(existing);
        }

        return academicProfileRepository.save(profile);
    }

    public CourseRecommender AddCourseRecommendation(String email) {

        // 1️⃣ Fetch AcademicProfile from DB
        AcademicProfile profile = academicProfileRepository.findByEmail(email);
        if (profile == null) {
            throw new RuntimeException("AcademicProfile not found for email: " + email);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("highestDegree", profile.getHighestDegree());
        payload.put("certifications", profile.getCertifications());
        payload.put("interests", profile.getInterests());
        payload.put("email", profile.getEmail());

        // 3️⃣ Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Explicit generic type to avoid errors
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CourseRecommender> response = restTemplate.exchange(
                AI_RECOMMEND_URL,
                HttpMethod.POST,
                request,
                CourseRecommender.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException(
                    "Failed to get recommendation from AI. Status: " + response.getStatusCode());
        }

        // 5️⃣ Add email to the received RecommendedCourse
        CourseRecommender recommendedCourse = response.getBody();
        recommendedCourse.setEmail(email); 

        CourseRecommender saved = recommendedCourseRepository.save(recommendedCourse);

        return saved;
    }
}
