package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.CourseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private CourseEntity testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new CourseEntity();
        testCourse.setName("MSc Data Science");
    }

    @Test
    void testSaveCourse() {
        CourseEntity saved = courseRepository.save(testCourse);
        assertNotNull(saved.getId());
        assertEquals("MSc Data Science", saved.getName());
    }

    @Test
    void testFindCourseById() {
        CourseEntity saved = courseRepository.save(testCourse);
        Optional<CourseEntity> found = courseRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("MSc Data Science", found.get().getName());
    }

    @Test
    void testFindCourseByIdNotFound() {
        Optional<CourseEntity> found = courseRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateCourse() {
        CourseEntity saved = courseRepository.save(testCourse);
        saved.setName("MSc Machine Learning");
        CourseEntity updated = courseRepository.save(saved);
        assertEquals("MSc Machine Learning", updated.getName());
    }

    @Test
    void testDeleteCourse() {
        CourseEntity saved = courseRepository.save(testCourse);
        Long id = saved.getId();
        courseRepository.deleteById(id);
        assertFalse(courseRepository.existsById(id));
    }

    @Test
    void testFindAll() {
        courseRepository.save(testCourse);
        CourseEntity course2 = new CourseEntity();
        course2.setName("MSc AI");
        courseRepository.save(course2);
        
        assertTrue(courseRepository.findAll().size() >= 2);
    }

    @Test
    void testCourseWithDifferentNames() {
        CourseEntity course1 = new CourseEntity();
        course1.setName("Course 1");
        CourseEntity course2 = new CourseEntity();
        course2.setName("Course 2");
        
        CourseEntity saved1 = courseRepository.save(course1);
        CourseEntity saved2 = courseRepository.save(course2);
        
        Optional<CourseEntity> result1 = courseRepository.findById(saved1.getId());
        Optional<CourseEntity> result2 = courseRepository.findById(saved2.getId());
        
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertEquals("Course 1", result1.get().getName());
        assertEquals("Course 2", result2.get().getName());
    }
}
