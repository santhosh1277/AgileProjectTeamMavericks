package com.example.StudentDashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    @SuppressWarnings("java:S4502") // CSRF disabled for REST API - safe for stateless authentication
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS using the global CORS bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Allow H2 console frames
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Allow preflight OPTIONS requests for all endpoints
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Allow H2 console without authentication
                .requestMatchers("/h2-console/**").permitAll()
                
                // Permit all other requests (you can lock this down later)
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow React app
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        
        // Allow all standard HTTP methods + OPTIONS for preflight
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow cookies/credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // Apply this CORS config to all endpoints
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
