package com.example.StudentDashboard.model;
import org.springframework.data.annotation.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Course {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    private String name;     
	    private String description; 

	    @ManyToOne
	    @JoinColumn(name = "college_id") 
	    private College college;

	    // Getters and Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }

	    public College getCollege() { return college; }
	    public void setCollege(College college) { this.college = college; }
	}

