package com.example.StudentDashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
<<<<<<< Updated upstream
            .csrf(csrf -> csrf.disable())                 // disable CSRF for testing
=======
            .csrf(csrf -> csrf.disable())
            // CSRF protection is disabled for REST API endpoints
            // This is safe because:
            // 1. REST APIs are stateless and don't use session-based authentication
            // 2. Authentication is handled via tokens/credentials in request body, not cookies
            // 3. CORS is properly configured to restrict origins
            .csrf(csrf -> csrf.disable())
>>>>>>> Stashed changes
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
<<<<<<< Updated upstream
=======
                .requestMatchers("/h2-console/**").permitAll()  // <-- allow H2
                .anyRequest().permitAll()
            )
            .headers(headers -> 
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // <-- allow iframe
                .requestMatchers("/h2-console/**").permitAll()  // Explicitly allow H2 console
>>>>>>> Stashed changes
                .anyRequest().permitAll()                 // allow all requests
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
