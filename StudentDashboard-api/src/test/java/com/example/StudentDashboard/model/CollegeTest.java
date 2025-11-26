package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;

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
}
