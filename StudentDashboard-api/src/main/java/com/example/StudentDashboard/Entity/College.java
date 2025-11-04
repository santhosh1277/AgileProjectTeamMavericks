package com.example.StudentDashboard.Entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private Integer rank;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseEntity> courses;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public List<CourseEntity> getCourses() { return courses; }
    public void setCourses(List<CourseEntity> courses) { this.courses = courses; }
}
