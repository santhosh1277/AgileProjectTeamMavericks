package com.example.StudentDashboard.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.StudentDashboard.entity.AcademicProfile;
import com.example.StudentDashboard.entity.CourseRecommender;
import com.example.StudentDashboard.entity.Student;
import com.example.StudentDashboard.entity.UserConsent;
import com.example.StudentDashboard.service.StudentService;

@RestController
@RequestMapping("/api/students") 
@CrossOrigin(origins = "http://localhost:3000") 
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Object> signup(@RequestBody Student student) {
        try {
            // Optional: Validate required fields
            if (student.getFirstName() == null || student.getLastName() == null || student.getEmail() == null || student.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Name, email, and password are required");
            }

            // Register the student
            Student savedStudent = studentService.registerStudent(student);

            // Return 201 Created with student data
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);

        } catch (Exception e) {
            // Return 500 if any error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering student: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getUsernameOrEmail();
            String password = loginRequest.getPassword();

            // Validate input
            if (email == null || password == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email and password are required");
            }

            Student student = studentService.login(email, password);
            student.setPassword(null);
            return ResponseEntity.ok(student);

        } catch (RuntimeException e) {
            // Return 401 for invalid credentials (when message is "Invalid email or password")
            if ("Invalid email or password".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }

    static class LoginRequest {
        private String usernameOrEmail;
        private String password;

        public String getUsernameOrEmail() {
            return usernameOrEmail;
        }

        public void setUsernameOrEmail(String usernameOrEmail) {
            this.usernameOrEmail = usernameOrEmail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestBody Student student) {

        boolean updated = studentService.updateStudentDetails(student);

        if (!updated) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update student details. Please check the input.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Student details updated successfully");
    }
    @PostMapping("/profile")
    public Student getStudent(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (email != null) {
            return studentService.getStudentDetails(email);
        }

        return new Student();
    }
    @PostMapping("/academic-profile")
    public ResponseEntity<?> AddAcademicProfile(@RequestBody AcademicProfile request) {
        
        
        if (request.getEmail() != null) {
            return ResponseEntity.ok(studentService.AddAcademicProfile(request));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
    }
   /**
 * @param profile
 * @return
 */
@PostMapping("/course-recommendations")
public ResponseEntity<List<CourseRecommender>> getCourseRecommendations(@RequestBody AcademicProfile profile) {
    List<CourseRecommender> recommendations = studentService.addCourseRecommendations(profile);
    return ResponseEntity.ok(recommendations);
}


    @PostMapping("/consent")
    public boolean getUserConsenttoCall(@RequestBody UserConsent consent)
    {
          return  studentService.UpdateUserConsent(consent);

    }
}
