package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.service.CollegeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "http://localhost:3000")
public class CollegeController {

    // ✅ Step 1: Declare the repository
    private CollegeService collegeService;

    // ✅ Step 2: Inject it via constructor
    public CollegeController(CollegeService collegeService ) {
        this.collegeService = collegeService;
    }

    @GetMapping
    public List<College> getAllColleges() {
    	
        return collegeService.getAllColleges();
    }

    @GetMapping("updatecolleges")
    public void UpdateCollegesList() {
        collegeService.UpdateCollegesList();
    }
   
}
