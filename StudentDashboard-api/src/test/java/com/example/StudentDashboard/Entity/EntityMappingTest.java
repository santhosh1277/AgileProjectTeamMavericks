package com.example.StudentDashboard.Entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityMappingTest {

	 @Test
	    void collegeAndCoursesRelationship() {
	        // Create college
	        College college = new College();
	        college.setId(1L);
	        college.setName("Test University");
	        college.setCountry("Ireland");
	        college.setAlphaTwoCode("IE");
	        college.setStateProvince("Leinster");
	        college.setDomains("testuniversity.ie");
	        college.setWebPages(List.of("https://testuniversity.ie"));

	        // Create course
	        CourseEntity course = new CourseEntity();
	        course.setId(2L);
	        course.setName("MSc Data Analytics");
	        course.setCollege(college);

	        // Link course to college
	        college.setCourses(List.of(course));

	        // Assertions
	        assertEquals(1L, college.getId());
	        assertEquals("Test University", college.getName());
	        assertEquals("Ireland", college.getCountry());
	        assertEquals("IE", college.getAlphaTwoCode());
	        assertEquals("Leinster", college.getStateProvince());
	        assertEquals("testuniversity.ie", college.getDomains());
	        assertEquals(1, college.getWebPages().size());
	        assertEquals("https://testuniversity.ie", college.getWebPages().get(0));
	        assertEquals(1, college.getCourses().size());
	        assertSame(college, college.getCourses().get(0).getCollege());
	    }
}
