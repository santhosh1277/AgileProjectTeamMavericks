package com.example.StudentDashboard.controller;

import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.repository.CourseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Get all courses (unified list for all colleges)
     * @return List of all courses
     */
    @GetMapping
    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Get master's degree courses only
     * @return List of master's degree courses
     */
    @GetMapping("/masters")
    public List<CourseEntity> getMastersCourses() {
        // Return courses that start with "MSc", "MA", or "MEng"
        return courseRepository.findAll().stream()
                .filter(course -> course.getName().startsWith("MSc") || 
                               course.getName().startsWith("MA ") || 
                               course.getName().startsWith("MEng"))
                .distinct()
                .toList();
    }
}
