package com.example.StudentDashboard.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTestNew {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("CSRF is disabled for REST APIs")
    void testCsrfDisabled() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Frame options headers are disabled for H2 console")
    void frameOptionsDisabled() throws Exception {
        mockMvc.perform(get("/h2-console/"))
                .andExpect(status().isOk());
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
    @DisplayName("CORS allows PUT method")
    void testCorsAllowsPut() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("PUT"));
    }

    @Test
    @DisplayName("CORS allows DELETE method")
    void testCorsAllowsDelete() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("DELETE"));
    }

    @Test
    @DisplayName("CORS allows OPTIONS method")
    void testCorsAllowsOptions() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));
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

    @Test
    @DisplayName("H2 console is accessible without authentication")
    void testH2ConsoleAccessible() throws Exception {
        mockMvc.perform(get("/h2-console/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("OPTIONS requests are permitted for all endpoints")
    void testOptionsRequestsPermitted() throws Exception {
        mockMvc.perform(options("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("API endpoints are accessible without authentication")
    void testApiEndpointsAccessible() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Security filter chain is properly configured")
    void testSecurityFilterChainConfiguration() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("CORS configuration applies to all endpoints")
    void testCorsConfigurationAppliesToAllEndpoints() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/any/endpoint");
        var config = source.getCorsConfiguration(request);
        assertNotNull(config);
        assertEquals(1, config.getAllowedOrigins().size());
    }
}
