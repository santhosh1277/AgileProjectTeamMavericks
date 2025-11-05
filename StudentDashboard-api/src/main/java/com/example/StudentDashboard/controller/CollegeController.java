package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.repository.CollegeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "http://localhost:3000")
public class CollegeController {

    // ✅ Step 1: Declare the repository
    private final CollegeRepository collegeRepository;

    // ✅ Step 2: Inject it via constructor
    public CollegeController(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    // ✅ Step 3: Use it
    @GetMapping
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    @PostMapping
    public College addCollege(@RequestBody College college) {
        return collegeRepository.save(college);
    }
}
