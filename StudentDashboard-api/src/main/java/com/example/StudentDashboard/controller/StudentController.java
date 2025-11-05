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
    public ResponseEntity<?> signup(@RequestBody Student student) {
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
}
