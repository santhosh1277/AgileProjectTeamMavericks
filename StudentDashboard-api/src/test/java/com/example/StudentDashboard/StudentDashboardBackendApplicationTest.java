package com.example.StudentDashboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentDashboardBackendApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    public void restTemplateBean() {
        assertNotNull(restTemplate);
    }

    @Test
    public void testApplicationClassExists() {
        assertNotNull(StudentDashboardBackendApplication.class);
    }

    @Test
    public void testEntityScanConfiguration() {
        // Verify entity scan configuration
        assertNotNull(applicationContext);
        assertTrue(applicationContext.containsBean("studentRepository"));
    }

    @Test
    public void testJpaRepositoriesConfiguration() {
        // Verify JPA repositories are configured
        assertNotNull(applicationContext);
        assertTrue(applicationContext.containsBean("collegeRepository"));
    }

    @Test
    public void testRestTemplateConfiguration() {
        // Verify RestTemplate is properly configured as a bean
        assertNotNull(restTemplate);
        assertEquals(RestTemplate.class, restTemplate.getClass());
    }
}
