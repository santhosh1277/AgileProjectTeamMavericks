package com.example.StudentDashboard.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.StudentDashboard.Entity.Student;
import com.example.StudentDashboard.repository.StudentRepository;
import java.util.Optional;

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
        _student = student;
        
        return student;
    }
    public boolean updateStudentDetails(Student student) {
        if (student == null || student.getId() == null) {
            return false;
        }

        if (student.getFirstName() == null || student.getLastName() == null || student.getLastName().isEmpty() || student.getFirstName().isEmpty()) {
            return false;
        }

        Optional<Student> existingStudent = studentRepository.findById(student.getId());
        if (existingStudent.isEmpty()) {
            return false;
        }

        studentRepository.save(student);

        _student = student;
        return true;
    }
    public Student getCachedStudent(Student student) {
    	
        return _student;
    }
    public Student getStudentDetails(String email) {
    		if(email == _student.getEmail())
    		{
    			return _student;
    		}
    		return new Student();
    }
  
}