package com.example.StudentDashboard.Entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityMappingTest {

    @Test
    void collegeAndCoursesRelationship() {
        College college = new College();
        college.setId(1L);
        college.setName("Test University");
        college.setLocation("Dublin");
        college.setRank(100);

        CourseEntity course = new CourseEntity();
        course.setId(2L);
        course.setName("MSc Data Analytics");
        course.setCollege(college);

        college.setCourses(List.of(course));

        assertEquals(1L, college.getId());
        assertEquals("Test University", college.getName());
        assertEquals("Dublin", college.getLocation());
        assertEquals(100, college.getRank());
        assertEquals(1, college.getCourses().size());
        assertSame(college, college.getCourses().get(0).getCollege());
    }
}
