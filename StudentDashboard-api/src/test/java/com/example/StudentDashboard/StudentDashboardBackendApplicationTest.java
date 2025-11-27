package com.example.StudentDashboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentDashboardBackendApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void restTemplateBeanExists() {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        assertNotNull(restTemplate);
    }

    @Test
    void restTemplateBeanIsNotNull() {
        Object restTemplate = applicationContext.getBean("restTemplate");
        assertNotNull(restTemplate);
        assertTrue(restTemplate instanceof RestTemplate);
    }

    @Test
    void applicationCanStart() {
        // Verifies that the application context loads successfully
        assertNotNull(applicationContext);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        assertTrue(beanNames.length > 0);
    }

}
