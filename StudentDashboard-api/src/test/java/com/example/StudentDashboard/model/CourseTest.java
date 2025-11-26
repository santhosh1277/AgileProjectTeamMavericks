package com.example.StudentDashboard.model;

import org.junit.jupiter.api.Test;

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
}
