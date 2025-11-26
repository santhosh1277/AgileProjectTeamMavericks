package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void gettersAndSettersWork() {
        Course course = new Course();
        College college = new College(1, "Uni", java.util.List.of(), "City", 50);

        course.setId(10L);
        course.setName("MSc Software Engineering");
        course.setDescription("A masters course");
        course.setCollege(college);

        assertEquals(10L, course.getId());
        assertEquals("MSc Software Engineering", course.getName());
        assertEquals("A masters course", course.getDescription());
        assertSame(college, course.getCollege());
    }

    @Test
    void testSetId() {
        Course course = new Course();
        course.setId(100L);
        assertEquals(100L, course.getId());
    }

    @Test
    void testSetName() {
        Course course = new Course();
        course.setName("Algorithms");
        assertEquals("Algorithms", course.getName());
    }

    @Test
    void testSetDescription() {
        Course course = new Course();
        course.setDescription("Introduction to algorithms");
        assertEquals("Introduction to algorithms", course.getDescription());
    }

    @Test
    void testSetCollege() {
        Course course = new Course();
        College college = new College(1, "MIT", Arrays.asList("CS"), "Boston", 1);
        course.setCollege(college);
        
        assertNotNull(course.getCollege());
        assertEquals("MIT", course.getCollege().getName());
    }

    @Test
    void testCourseWithNullCollege() {
        Course course = new Course();
        course.setName("Independent Study");
        assertNull(course.getCollege());
    }

    @Test
    void testMultipleCoursesSameCollege() {
        College college = new College(1, "Harvard", Arrays.asList("CS", "Math"), "Cambridge", 3);
        
        Course course1 = new Course();
        course1.setName("Computer Science");
        course1.setCollege(college);
        
        Course course2 = new Course();
        course2.setName("Mathematics");
        course2.setCollege(college);
        
        assertEquals(course1.getCollege(), course2.getCollege());
    }
}
