package com.example.StudentDashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentDashboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentDashboardBackendApplication.class, args);
		System.out.println("Hello Student");
	}

}
