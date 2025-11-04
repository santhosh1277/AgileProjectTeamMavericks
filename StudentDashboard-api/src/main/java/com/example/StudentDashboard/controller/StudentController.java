package com.example.StudentDashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.service.StudentService;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/signup")
    public Student signup(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }
}
