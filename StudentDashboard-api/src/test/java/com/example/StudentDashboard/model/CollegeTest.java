package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollegeTest {

    @Test
    void constructorAndGettersWork() {
        List<String> courses = List.of("MSc Software Engineering", "MSc Data Analytics");
        College c = new College(1, "Test College", courses, "Dublin", 100);

        assertEquals(1, c.getId());
        assertEquals("Test College", c.getName());
        assertEquals(courses, c.getCourses());
        assertEquals("Dublin", c.getLocation());
        assertEquals(100, c.getWorldRank());
    }

    @Test
    void testGetId() {
        College college = new College(100, "Stanford", Arrays.asList("CS"), "California", 2);
        assertEquals(100, college.getId());
    }

    @Test
    void testGetName() {
        College college = new College(1, "Harvard", Arrays.asList("Law"), "Cambridge", 3);
        assertEquals("Harvard", college.getName());
    }

    @Test
    void testGetCourses() {
        List<String> courses = Arrays.asList("Medicine", "Biology", "Chemistry");
        College college = new College(1, "Johns Hopkins", courses, "Baltimore", 10);
        assertEquals(3, college.getCourses().size());
        assertTrue(college.getCourses().contains("Medicine"));
    }

    @Test
    void testGetLocation() {
        College college = new College(1, "Oxford", Arrays.asList("Philosophy"), "Oxford", 5);
        assertEquals("Oxford", college.getLocation());
    }

    @Test
    void testGetWorldRank() {
        College college = new College(1, "Cambridge", Arrays.asList("Math"), "Cambridge", 4);
        assertEquals(4, college.getWorldRank());
    }

    @Test
    void testCollegeWithEmptyCourses() {
        College college = new College(1, "NewUniversity", Arrays.asList(), "NewCity", 100);
        assertNotNull(college.getCourses());
        assertTrue(college.getCourses().isEmpty());
    }

    @Test
    void testCollegeWithMultipleCourses() {
        List<String> courses = Arrays.asList("CS", "Math", "Physics", "Chemistry", "Biology");
        College college = new College(5, "TechInstitute", courses, "TechCity", 50);
        assertEquals(5, college.getCourses().size());
    }
}
