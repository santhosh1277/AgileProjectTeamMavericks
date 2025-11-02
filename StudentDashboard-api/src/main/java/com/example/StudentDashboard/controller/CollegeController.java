package com.example.StudentDashboard.controller;

import org.springframework.web.bind.annotation.*;

import com.example.StudentDashboard.model.College;
import com.example.StudentDashboard.service.CollegeService;

import java.util.*;


@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "http://localhost:3000") // allow React access
public class CollegeController {

	 private final CollegeService collegeService;

	    // Constructor injection of the service
	    public CollegeController(CollegeService collegeService) {
	        this.collegeService = collegeService;
	    }


	    @GetMapping
	    public List<College> getAllColleges() {
	        return collegeService.getAllColleges();
	    }
}
