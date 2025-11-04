package com.example.StudentDashboard.Entity;

import jakarta.persistence.*;

@Entity
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public College getCollege() { return college; }
    public void setCollege(College college) { this.college = college; }
}
