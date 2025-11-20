package com.example.StudentDashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;

@RestController
@RequestMapping("/api/students") // Use plural to match REST conventions
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Object> signup(@RequestBody Student student) {
        try {
            // Optional: Validate required fields
            if (student.getName() == null || student.getEmail() == null || student.getPassword() == null) {
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

            // Attempt login
            Student student = studentService.login(email, password);

            // Return success with student data (excluding password)
            student.setPassword(null); // Don't send password back
            return ResponseEntity.ok(student);

        } catch (RuntimeException e) {
            // Return 401 for invalid credentials (when message is "Invalid email or password")
            if ("Invalid email or password".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(e.getMessage());
            }
            // Return 500 for other runtime exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        } catch (Exception e) {
            // Return 500 for other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }

    // Inner class for login request
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
}
