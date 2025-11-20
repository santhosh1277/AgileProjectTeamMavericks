package com.example.StudentDashboard.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    private Student _student;

    public Student registerStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Encrypt password before saving
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        student.setPassword(encoder.encode(student.getPassword()));

        return studentRepository.save(student);
    }
<<<<<<< Updated upstream
}
=======
    public boolean updateStudentDetails(Student student) {
        if (student == null || student.getId() == null) {
            return false; // Null check
        }

        // Additional validation
        if (student.getName() == null || student.getName().isEmpty()) {
            return false;
        }

        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isEmpty()) {
            return false; // Student not found
        }

        // Update student in DB
        studentRepository.save(student);

        getCachedStudent(student);
        return true;
    }

    // Optional: get student from cache
    public Student getCachedStudent(Student student) {
    	
        return student;
    }
}

    
    public Student login(String email, String password) {
        // Find student by email
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }
        
        Student student = studentOpt.get();
        
        // Verify password using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, student.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        return student;
    }
}
>>>>>>> Stashed changes
