package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.AcademicProfile;
import com.example.StudentDashboard.Entity.College;
import com.example.StudentDashboard.Entity.CourseEntity;
import com.example.StudentDashboard.Entity.CourseRecommender;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.Entity.UserConsent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryEdgeCasesTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private AcademicProfileRepository academicProfileRepository;

    @Autowired
    private UserConsentRepository userConsentRepository;

    @Autowired
    private CourseRecommenderRepository courseRecommenderRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Student Repository Edge Cases
    @Test
    void testStudentWithSpecialCharactersInEmail() {
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setDob(LocalDate.of(2000, 1, 1));
        student.setEmail("john+test@example.com");
        student.setPassword("pass");
        
        Student saved = studentRepository.save(student);
        assertTrue(studentRepository.existsByEmail("john+test@example.com"));
    }

    @Test
    void testStudentWithLongName() {
        Student student = new Student();
        student.setFirstName("VeryLongFirstNameThatShouldStillWork");
        student.setLastName("VeryLongLastNameThatShouldStillWork");
        student.setDob(LocalDate.of(2000, 1, 1));
        student.setEmail("long@example.com");
        student.setPassword("password");
        
        Student saved = studentRepository.save(student);
        assertNotNull(saved.getId());
    }

    @Test
    void testMultipleStudentsWithDifferentDates() {
        Student student1 = new Student("Alice", "Smith", LocalDate.of(1999, 1, 1), "alice@example.com", "pass1");
        Student student2 = new Student("Bob", "Jones", LocalDate.of(2000, 2, 2), "bob@example.com", "pass2");
        
        studentRepository.save(student1);
        studentRepository.save(student2);
        
        assertEquals(2, studentRepository.findAll().size());
    }

    // College Repository Edge Cases
    @Test
    void testCollegeWithOnlyName() {
        College college = new College();
        college.setName("Simple College");
        
        College saved = collegeRepository.save(college);
        assertNotNull(saved.getId());
        assertNull(saved.getCountry());
    }

    @Test
    void testCollegeWithCompleteData() {
        College college = new College();
        college.setName("Full College");
        college.setCountry("USA");
        college.setStateProvince("California");
        college.setAlphaTwoCode("US");
        college.setDomains("college.edu");
        college.setWebPages(new ArrayList<>());
        
        College saved = collegeRepository.save(college);
        assertNotNull(saved.getId());
        assertEquals("Full College", saved.getName());
    }

    @Test
    void testFindByNameAndCountryCaseSensitive() {
        College college = new College();
        college.setName("Oxford");
        college.setCountry("UK");
        collegeRepository.save(college);
        
        assertFalse(collegeRepository.findByNameAndCountry("oxford", "UK").isPresent());
        assertTrue(collegeRepository.findByNameAndCountry("Oxford", "UK").isPresent());
    }

    // Academic Profile Edge Cases
    @Test
    void testAcademicProfileWithEmptyCertifications() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("profile@test.com");
        profile.setDegree("BS");
        profile.setCertifications(new ArrayList<>());
        profile.setInterests(new ArrayList<>());
        
        AcademicProfile saved = academicProfileRepository.save(profile);
        assertEquals(0, saved.getCertifications().size());
    }

    @Test
    void testAcademicProfileWithManyItems() {
        AcademicProfile profile = new AcademicProfile();
        profile.setEmail("many@test.com");
        profile.setDegree("PhD");
        
        List<String> certs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            certs.add("Cert" + i);
        }
        profile.setCertifications(certs);
        
        AcademicProfile saved = academicProfileRepository.save(profile);
        assertEquals(10, saved.getCertifications().size());
    }

    // User Consent Edge Cases
    @Test
    void testMultipleConsentsWithSameEmail() {
        UserConsent consent = new UserConsent("shared@test.com", true);
        userConsentRepository.save(consent);
        
        UserConsent updated = new UserConsent("shared@test.com", false);
        userConsentRepository.save(updated);
        
        assertEquals(1, userConsentRepository.findAll().size());
    }

    @Test
    void testConsentWithMixedCase() {
        UserConsent consent = new UserConsent("MiXeD@TeSt.CoM", true);
        userConsentRepository.save(consent);
        
        assertTrue(userConsentRepository.findByEmailIgnoreCase("mixed@test.com").isPresent());
    }

    // Course Recommender Edge Cases
    @Test
    void testRecommenderWithDifferentCoursesForSameStudent() {
        CourseRecommender rec1 = new CourseRecommender();
        rec1.setEmail("student@test.com");
        rec1.setCourseName("Course 1");
        rec1.setDescription("Desc1");
        
        CourseRecommender rec2 = new CourseRecommender();
        rec2.setEmail("student@test.com");
        rec2.setCourseName("Course 2");
        rec2.setDescription("Desc2");
        
        courseRecommenderRepository.save(rec1);
        courseRecommenderRepository.save(rec2);
        
        List<CourseRecommender> found = courseRecommenderRepository.findByEmail("student@test.com");
        assertEquals(2, found.size());
    }

    @Test
    void testRecommenderWithLongDescriptions() {
        CourseRecommender rec = new CourseRecommender();
        rec.setEmail("test@test.com");
        rec.setCourseName("Advanced Course");
        rec.setDescription("This is a very long description " + "x".repeat(500));
        
        CourseRecommender saved = courseRecommenderRepository.save(rec);
        assertNotNull(saved.getId());
    }

    // Course Entity Edge Cases
    @Test
    void testCourseWithoutCollege() {
        CourseEntity course = new CourseEntity();
        course.setName("Standalone Course");
        
        CourseEntity saved = courseRepository.save(course);
        assertNotNull(saved.getId());
        assertNull(saved.getCollege());
    }

    @Test
    void testMultipleCoursesWithSameName() {
        CourseEntity course1 = new CourseEntity();
        course1.setName("Common Name");
        
        CourseEntity course2 = new CourseEntity();
        course2.setName("Common Name");
        
        courseRepository.save(course1);
        courseRepository.save(course2);
        
        assertEquals(2, courseRepository.findAll().size());
    }

    @Test
    void testCourseUpdateMultipleTimes() {
        CourseEntity course = new CourseEntity();
        course.setName("Original");
        
        CourseEntity saved = courseRepository.save(course);
        saved.setName("Updated1");
        courseRepository.save(saved);
        
        saved.setName("Updated2");
        CourseEntity final_version = courseRepository.save(saved);
        
        assertEquals("Updated2", final_version.getName());
    }
}
