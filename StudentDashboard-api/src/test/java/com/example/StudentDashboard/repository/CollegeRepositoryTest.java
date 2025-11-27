package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.College;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CollegeRepositoryTest {

    @Autowired
    private CollegeRepository collegeRepository;

    private College testCollege;

    @BeforeEach
    void setUp() {
        testCollege = new College();
        testCollege.setName("Trinity College Dublin");
        testCollege.setCountry("Ireland");
        testCollege.setStateProvince("Dublin");
        testCollege.setAlphaTwoCode("IE");
        testCollege.setDomains("tcd.ie");
    }

    @Test
    void testSaveCollege() {
        College saved = collegeRepository.save(testCollege);
        assertNotNull(saved.getId());
        assertEquals("Trinity College Dublin", saved.getName());
    }

    @Test
    void testFindByNameAndCountryFound() {
        collegeRepository.save(testCollege);
        Optional<College> found = collegeRepository.findByNameAndCountry("Trinity College Dublin", "Ireland");
        assertTrue(found.isPresent());
        assertEquals("IE", found.get().getAlphaTwoCode());
    }

    @Test
    void testFindByNameAndCountryNotFound() {
        Optional<College> found = collegeRepository.findByNameAndCountry("NonExistent", "NoWhere");
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateCollege() {
        College saved = collegeRepository.save(testCollege);
        saved.setCountry("UK");
        College updated = collegeRepository.save(saved);
        assertEquals("UK", updated.getCountry());
    }

    @Test
    void testDeleteCollege() {
        College saved = collegeRepository.save(testCollege);
        Long id = saved.getId();
        collegeRepository.deleteById(id);
        assertFalse(collegeRepository.existsById(id));
    }

    @Test
    void testFindAllColleges() {
        collegeRepository.save(testCollege);
        College college2 = new College();
        college2.setName("University College Dublin");
        college2.setCountry("Ireland");
        collegeRepository.save(college2);
        
        assertTrue(collegeRepository.findAll().size() >= 2);
    }

    @Test
    void testCollegeWithNullValues() {
        College college = new College();
        college.setName("Test College");
        College saved = collegeRepository.save(college);
        assertNotNull(saved.getId());
        assertNull(saved.getCountry());
    }
}
