package com.example.StudentDashboard.Entity;

import org.junit.jupiter.api.Test;

import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.Entity.AcademicProfile;
import com.example.StudentDashboard.Entity.CourseRecommender;
import com.example.StudentDashboard.Entity.UserConsent;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Test
    void studentEntityMapping() {
        // Create student
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEmail("john@example.com");
        student.setPassword("hashed_password");
        student.setDob(LocalDate.of(2000, 5, 15));

        // Assertions
        assertEquals(1L, student.getId());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john@example.com", student.getEmail());
        assertEquals("hashed_password", student.getPassword());
        assertEquals(LocalDate.of(2000, 5, 15), student.getDob());
    }

    @Test
    void studentConstructorMapping() {
        Student student = new Student("Jane", "Smith", LocalDate.of(2001, 3, 20), "jane@example.com", "pass123");

        assertEquals("Jane", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("jane@example.com", student.getEmail());
        assertEquals("pass123", student.getPassword());
        assertEquals(LocalDate.of(2001, 3, 20), student.getDob());
    }

    @Test
    void courseEntityMapping() {
        College college = new College();
        college.setId(1L);
        college.setName("Test University");

        CourseEntity course = new CourseEntity();
        course.setId(1L);
        course.setName("MSc Software Engineering");
        course.setCollege(college);

        assertEquals(1L, course.getId());
        assertEquals("MSc Software Engineering", course.getName());
        assertEquals(college, course.getCollege());
        assertEquals("Test University", course.getCollege().getName());
    }

    @Test
    void academicProfileMapping() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("student@example.com");
        profile.setDegree("BTech Computer Science");
        profile.setCertifications(List.of("AWS", "Java"));
        profile.setInterests(List.of("AI", "Cloud"));

        assertEquals("student@example.com", profile.getEmail());
        assertEquals("BTech Computer Science", profile.getDegree());
        assertEquals(2, profile.getCertifications().size());
        assertEquals(2, profile.getInterests().size());
        assertTrue(profile.getCertifications().contains("AWS"));
        assertTrue(profile.getInterests().contains("AI"));
    }

    @Test
    void academicProfileConstructor() {
        AcademicProfile profile = new AcademicProfile("MSc AI", "student@email.com", 
            List.of("Python", "ML"), List.of("NLP", "CV"));

        assertEquals("MSc AI", profile.getDegree());
        assertEquals("student@email.com", profile.getEmail());
        assertEquals(2, profile.getCertifications().size());
        assertEquals(2, profile.getInterests().size());
    }

    @Test
    void courseRecommenderMapping() {
        CourseRecommender recommender = new CourseRecommender();
        recommender.setCourseName("MSc Data Science");
        recommender.setDescription("Advanced data science program");
        recommender.setEmail("student@example.com");
        recommender.setDomains(List.of("data.edu", "sci.edu"));
        recommender.setSkills(List.of("Python", "Statistics"));

        assertEquals("MSc Data Science", recommender.getCourseName());
        assertEquals("Advanced data science program", recommender.getDescription());
        assertEquals("student@example.com", recommender.getEmail());
        assertEquals(2, recommender.getDomains().size());
        assertEquals(2, recommender.getSkills().size());
    }

    @Test
    void courseRecommenderConstructor() {
        CourseRecommender recommender = new CourseRecommender(
            "MSc AI", "Artificial Intelligence Masters", "ai@student.com",
            List.of("ai.edu"), List.of("ML", "Deep Learning"));

        assertEquals("MSc AI", recommender.getCourseName());
        assertEquals("Artificial Intelligence Masters", recommender.getDescription());
        assertEquals("ai@student.com", recommender.getEmail());
    }

    @Test
    void userConsentMapping() {
        UserConsent consent = new UserConsent();
        consent.setEmail("student@example.com");
        consent.setConsentGiven(true);

        assertEquals("student@example.com", consent.getEmail());
        assertTrue(consent.isConsentGiven());
    }

    @Test
    void userConsentConstructor() {
        UserConsent consent = new UserConsent("john@example.com", false);

        assertEquals("john@example.com", consent.getEmail());
        assertFalse(consent.isConsentGiven());
    }

    @Test
    void multipleCoursesForCollege() {
        College college = new College();
        college.setId(1L);
        college.setName("University of Excellence");
        college.setCountry("UK");

        List<CourseEntity> courses = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            CourseEntity course = new CourseEntity();
            course.setId((long) i);
            course.setName("Course " + i);
            course.setCollege(college);
            courses.add(course);
        }

        college.setCourses(courses);

        assertEquals(3, college.getCourses().size());
        for (CourseEntity course : college.getCourses()) {
            assertEquals(college, course.getCollege());
            assertEquals("University of Excellence", course.getCollege().getName());
        }
    }

    @Test
    void collegeWebPagesMapping() {
        College college = new College();
        college.setId(1L);
        college.setName("Tech University");
        college.setWebPages(List.of("https://tech.edu", "https://www.tech.edu", "https://portal.tech.edu"));

        assertEquals(3, college.getWebPages().size());
        assertTrue(college.getWebPages().contains("https://tech.edu"));
        assertTrue(college.getWebPages().contains("https://portal.tech.edu"));
    }

    @Test
    void collegeFieldsMapping() {
        College college = new College();
        college.setId(5L);
        college.setName("University XYZ");
        college.setCountry("USA");
        college.setAlphaTwoCode("US");
        college.setStateProvince("California");
        college.setDomains("xyz.edu");
        college.setWebPages(List.of("https://xyz.edu"));

        assertEquals(5L, college.getId());
        assertEquals("University XYZ", college.getName());
        assertEquals("USA", college.getCountry());
        assertEquals("US", college.getAlphaTwoCode());
        assertEquals("California", college.getStateProvince());
        assertEquals("xyz.edu", college.getDomains());
    }

    @Test
    void academicProfileNullValues() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("test@example.com");
        profile.setDegree("Bachelors");

        assertEquals("test@example.com", profile.getEmail());
        assertEquals("Bachelors", profile.getDegree());
    }

    @Test
    void courseRecommenderEmptyCollections() {
        CourseRecommender recommender = new CourseRecommender();
        recommender.setCourseName("MSc");
        recommender.setEmail("test@example.com");
        recommender.setDomains(List.of());
        recommender.setSkills(List.of());

        assertEquals(0, recommender.getDomains().size());
        assertEquals(0, recommender.getSkills().size());
    }
}
