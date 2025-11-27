package com.example.StudentDashboard.service;

import com.example.StudentDashboard.entity.AcademicProfile;
import com.example.StudentDashboard.entity.CourseRecommender;
import com.example.StudentDashboard.entity.Student;
import com.example.StudentDashboard.entity.UserConsent;
import com.example.StudentDashboard.model.CourseRecommenderRequest;
import com.example.StudentDashboard.repository.AcademicProfileRepository;
import com.example.StudentDashboard.repository.CourseRecommenderRepository;
import com.example.StudentDashboard.repository.StudentRepository;
import com.example.StudentDashboard.repository.UserConsentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private AcademicProfileRepository academicProfileRepository;
    private CourseRecommenderRepository recommendedCourseRepository;
    private UserConsentRepository _userConsentRepository;
    private RestTemplate restTemplate;
    private Student _student;
    private final String AI_RECOMMEND_URL = "http://127.0.0.1:5000/recommend";

    public StudentService(StudentRepository studentRepository, AcademicProfileRepository academicProfileRepository,
            CourseRecommenderRepository recommendedCourseRepository, UserConsentRepository userConsentRepository,
            RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.academicProfileRepository = academicProfileRepository;
        this.recommendedCourseRepository = recommendedCourseRepository;
        _userConsentRepository = userConsentRepository;
        this.restTemplate = restTemplate;
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
    public List<CourseRecommender> AddAcademicProfile(AcademicProfile profile) {
        if (profile.getCertifications() == null)
            profile.setCertifications(new ArrayList<>());
        if (profile.getInterests() == null)
            profile.setInterests(new ArrayList<>());

        AcademicProfile existing = academicProfileRepository.findByEmail(profile.getEmail());

        if (existing != null) {
            // Update parent
            existing.setDegree(profile.getDegree());
            existing.setCertifications(profile.getCertifications());
            existing.setInterests(profile.getInterests());

            // Save parent first
            AcademicProfile saved = academicProfileRepository.save(existing);
        }

        // For new profile: save parent first to generate ID
        AcademicProfile savedProfile = academicProfileRepository.save(profile);

        return addCourseRecommendations(profile);
    }

    public List<CourseRecommender> addCourseRecommendations(AcademicProfile profile) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(AI_RECOMMEND_URL, profile, String.class);

            // Map JSON response to CourseRecommenderRequest
            ObjectMapper mapper = new ObjectMapper();
            CourseRecommenderRequest courseResponse = mapper.readValue(response.getBody(),
                    CourseRecommenderRequest.class);

            // Get the list of recommendations
            List<CourseRecommender> courses = courseResponse.getRecommendations();

            // Set email for each recommendation
            for (CourseRecommender course : courses) {
                course.setEmail(profile.getEmail());
            }

            // Save all recommendations to DB
            return recommendedCourseRepository.saveAll(courses);

        } catch (HttpClientErrorException e) {
            System.out.println("AI Service Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error adding course recommendation: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing AI response: " + e.getMessage());
        }
    }

    public boolean UpdateUserConsent(UserConsent consent) {
        if (consent.getEmail() != null) {
            if (consent.isConsentGiven())
                _userConsentRepository.save(consent);
            return true;
        }
        return false;
    }

   public List<CourseRecommender> getRecommendedCourses(String email) {
    List<CourseRecommender> allCourses = recommendedCourseRepository.findByEmail(email);
    
    Map<String, CourseRecommender> uniqueCourses = new LinkedHashMap<>();
    for (CourseRecommender course : allCourses) {
        uniqueCourses.putIfAbsent(course.getCourseName(), course);
    }
    
    return new ArrayList<>(uniqueCourses.values());
}

   public boolean userConsent(String email) {
    return _userConsentRepository.findByEmailIgnoreCase(email)
                                 .map(UserConsent::isConsentGiven)
                                 .orElse(false);
}
}