package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.model.UniversityApiResponse;
import com.example.StudentDashboard.service.UniversityApiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")

public class UniversityApiController {

    private final UniversityApiService universityApiService;

    public UniversityApiController(UniversityApiService universityApiService) {
        this.universityApiService = universityApiService;
    }

    // Example: /api/universities?country=Ireland
    @GetMapping
    public List<UniversityApiResponse> getUniversities(
            @RequestParam(defaultValue = "Ireland") String country) {
        return universityApiService.getUniversitiesByCountry(country);
    }
}
