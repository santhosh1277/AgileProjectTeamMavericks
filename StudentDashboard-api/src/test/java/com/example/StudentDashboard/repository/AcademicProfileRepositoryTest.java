package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.AcademicProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AcademicProfileRepositoryTest {

    @Autowired
    private AcademicProfileRepository academicProfileRepository;

    private AcademicProfile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new AcademicProfile();
        testProfile.setEmail("student@test.com");
        testProfile.setDegree("BTech");
        testProfile.setCertifications(new ArrayList<>());
        testProfile.setInterests(new ArrayList<>());
    }

    @Test
    void testSaveAcademicProfile() {
        AcademicProfile saved = academicProfileRepository.save(testProfile);
        assertNotNull(saved.getId());
        assertEquals("BTech", saved.getDegree());
    }

    @Test
    void testFindByEmailFound() {
        academicProfileRepository.save(testProfile);
        AcademicProfile found = academicProfileRepository.findByEmail("student@test.com");
        assertNotNull(found);
        assertEquals("BTech", found.getDegree());
    }

    @Test
    void testFindByEmailNotFound() {
        AcademicProfile found = academicProfileRepository.findByEmail("notexist@test.com");
        assertNull(found);
    }

    @Test
    void testUpdateAcademicProfile() {
        AcademicProfile saved = academicProfileRepository.save(testProfile);
        saved.setDegree("MTech");
        AcademicProfile updated = academicProfileRepository.save(saved);
        assertEquals("MTech", updated.getDegree());
    }

    @Test
    void testDeleteAcademicProfile() {
        AcademicProfile saved = academicProfileRepository.save(testProfile);
        Long id = saved.getId();
        academicProfileRepository.deleteById(id);
        assertFalse(academicProfileRepository.existsById(id));
    }

    @Test
    void testFindAllProfiles() {
        academicProfileRepository.save(testProfile);
        AcademicProfile profile2 = new AcademicProfile();
        profile2.setEmail("student2@test.com");
        profile2.setDegree("BCA");
        academicProfileRepository.save(profile2);
        
        assertTrue(academicProfileRepository.findAll().size() >= 2);
    }

    @Test
    void testAcademicProfileWithMultipleCertifications() {
        testProfile.getCertifications().add("AWS");
        testProfile.getCertifications().add("GCP");
        testProfile.getInterests().add("Cloud");
        
        AcademicProfile saved = academicProfileRepository.save(testProfile);
        assertNotNull(saved.getId());
        assertEquals(2, saved.getCertifications().size());
        assertEquals(1, saved.getInterests().size());
    }
}
