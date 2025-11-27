package com.example.StudentDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(basePackages = "com.example.StudentDashboard.entity")
@EnableJpaRepositories(basePackages = "com.example.StudentDashboard.repository")
public class StudentDashboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentDashboardBackendApplication.class, args);
		System.out.println("Hello Student");
	}

    @Bean
    RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
