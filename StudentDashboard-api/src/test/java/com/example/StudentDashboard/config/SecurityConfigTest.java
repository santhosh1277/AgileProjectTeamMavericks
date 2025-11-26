package com.example.StudentDashboard.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SuppressWarnings("null")
@WebMvcTest(controllers = {SecurityConfigTest.TestController.class, SecurityConfigTest.H2StubController.class})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @RestController
    static class TestController {
        @GetMapping("/test")
        public String get() {
            return "ok";
        }

        @PostMapping("/test")
        public String post() {
            return "ok";
        }
    }

    @RestController
    @RequestMapping("/h2-console")
    static class H2StubController {
        @GetMapping("/dummy")
        public String dummy() {
            return "h2";
        }
    }

  
    

    @Test
    @DisplayName("Frame options headers are disabled")
    void frameOptionsDisabled() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(header().doesNotExist("X-Frame-Options"));
    }

    @Test
    @DisplayName("CORS configuration source is created")
    void testCorsConfigurationBean() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
    }

    @Test
    @DisplayName("CORS allowed origins contains localhost:3000")
    void testCorsAllowedOrigins() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedOrigins().contains("http://localhost:3000"));
    }

    @Test
    @DisplayName("CORS credentials are allowed")
    void testCorsCredentials() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowCredentials());
    }

    @Test
    @DisplayName("CORS allows GET method")
    void testCorsAllowsGet() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("GET"));
    }

    @Test
    @DisplayName("CORS allows POST method")
    void testCorsAllowsPost() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("POST"));
    }

    @Test
    @DisplayName("CORS allows all headers")
    void testCorsAllowsAllHeaders() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedHeaders().contains("*"));
    }
}




