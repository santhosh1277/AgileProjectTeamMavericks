package com.example.StudentDashboard.Entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String FirstName;
    private String LastName;
    private String dob;
    private String email;
    private String password;

    // Constructors
    public Student() {}

    public Student(String fname, String lname, String dob, String email, String password) {
        this.FirstName = fname;
        this.LastName = lname;
        this.dob = dob;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return FirstName; }
    public void setFirstName(String fname) { this.FirstName = fname; }
    
    public String getLastName() { return LastName; }
    public void setLastName(String lname) { this.LastName = lname; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
