package com.example.StudentDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
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
