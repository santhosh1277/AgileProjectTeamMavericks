package com.example.StudentDashboard.repository;

import com.example.StudentDashboard.Entity.UserConsent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserConsentRepositoryTest {

    @Autowired
    private UserConsentRepository userConsentRepository;

    private UserConsent testConsent;

    @BeforeEach
    void setUp() {
        testConsent = new UserConsent();
        testConsent.setEmail("user@test.com");
        testConsent.setConsentGiven(true);
    }

    @Test
    void testSaveUserConsent() {
        UserConsent saved = userConsentRepository.save(testConsent);
        assertNotNull(saved.getEmail());
        assertTrue(saved.isConsentGiven());
    }

    @Test
    void testFindByEmailIgnoreCaseFound() {
        userConsentRepository.save(testConsent);
        Optional<UserConsent> found = userConsentRepository.findByEmailIgnoreCase("USER@TEST.COM");
        assertTrue(found.isPresent());
        assertEquals("user@test.com", found.get().getEmail());
    }

    @Test
    void testFindByEmailIgnoreCaseNotFound() {
        Optional<UserConsent> found = userConsentRepository.findByEmailIgnoreCase("notexist@test.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateUserConsent() {
        UserConsent saved = userConsentRepository.save(testConsent);
        saved.setConsentGiven(false);
        UserConsent updated = userConsentRepository.save(saved);
        assertFalse(updated.isConsentGiven());
    }

    @Test
    void testDeleteUserConsent() {
        UserConsent saved = userConsentRepository.save(testConsent);
        String email = saved.getEmail();
        userConsentRepository.deleteById(email);
        assertFalse(userConsentRepository.existsById(email));
    }

    @Test
    void testFindAllConsents() {
        userConsentRepository.save(testConsent);
        UserConsent consent2 = new UserConsent();
        consent2.setEmail("user2@test.com");
        consent2.setConsentGiven(false);
        userConsentRepository.save(consent2);
        
        assertTrue(userConsentRepository.findAll().size() >= 2);
    }

    @Test
    void testConsentFalse() {
        UserConsent consentFalse = new UserConsent();
        consentFalse.setEmail("noconsent@test.com");
        consentFalse.setConsentGiven(false);
        
        UserConsent saved = userConsentRepository.save(consentFalse);
        assertFalse(saved.isConsentGiven());
    }
}
