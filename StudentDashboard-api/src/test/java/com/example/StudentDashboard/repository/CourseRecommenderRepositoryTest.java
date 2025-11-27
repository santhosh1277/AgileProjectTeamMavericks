package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.CourseRecommender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRecommenderRepositoryTest {

    @Autowired
    private CourseRecommenderRepository courseRecommenderRepository;

    private CourseRecommender testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new CourseRecommender();
        testCourse.setEmail("student@test.com");
        testCourse.setCourseName("MSc Data Science");
    }

    @Test
    void testSaveCourseRecommender() {
        CourseRecommender saved = courseRecommenderRepository.save(testCourse);
        assertNotNull(saved.getId());
        assertEquals("MSc Data Science", saved.getCourseName());
    }

    @Test
    void testFindByEmailSingleCourse() {
        courseRecommenderRepository.save(testCourse);
        List<CourseRecommender> found = courseRecommenderRepository.findByEmail("student@test.com");
        assertEquals(1, found.size());
        assertEquals("MSc Data Science", found.get(0).getCourseName());
    }

    @Test
    void testFindByEmailMultipleCourses() {
        courseRecommenderRepository.save(testCourse);
        
        CourseRecommender course2 = new CourseRecommender();
        course2.setEmail("student@test.com");
        course2.setCourseName("MSc AI");
        courseRecommenderRepository.save(course2);
        
        List<CourseRecommender> found = courseRecommenderRepository.findByEmail("student@test.com");
        assertEquals(2, found.size());
    }

    @Test
    void testFindByEmailNotFound() {
        List<CourseRecommender> found = courseRecommenderRepository.findByEmail("notexist@test.com");
        assertTrue(found.isEmpty());
    }

    @Test
    void testUpdateCourseRecommender() {
        CourseRecommender saved = courseRecommenderRepository.save(testCourse);
        saved.setCourseName("MSc Machine Learning");
        CourseRecommender updated = courseRecommenderRepository.save(saved);
        assertEquals("MSc Machine Learning", updated.getCourseName());
    }

    @Test
    void testDeleteCourseRecommender() {
        CourseRecommender saved = courseRecommenderRepository.save(testCourse);
        Long id = saved.getId();
        courseRecommenderRepository.deleteById(id);
        assertFalse(courseRecommenderRepository.existsById(id));
    }

    @Test
    void testFindByEmailDifferentStudents() {
        courseRecommenderRepository.save(testCourse);
        
        CourseRecommender course2 = new CourseRecommender();
        course2.setEmail("other@test.com");
        course2.setCourseName("MSc Software Engineering");
        courseRecommenderRepository.save(course2);
        
        List<CourseRecommender> student1Courses = courseRecommenderRepository.findByEmail("student@test.com");
        List<CourseRecommender> student2Courses = courseRecommenderRepository.findByEmail("other@test.com");
        
        assertEquals(1, student1Courses.size());
        assertEquals(1, student2Courses.size());
        assertNotEquals(student1Courses.get(0).getCourseName(), student2Courses.get(0).getCourseName());
    }

    @Test
    void testFindAll() {
        courseRecommenderRepository.save(testCourse);
        CourseRecommender course2 = new CourseRecommender();
        course2.setEmail("student2@test.com");
        course2.setCourseName("MSc Cloud Computing");
        courseRecommenderRepository.save(course2);
        
        assertTrue(courseRecommenderRepository.findAll().size() >= 2);
    }
}
