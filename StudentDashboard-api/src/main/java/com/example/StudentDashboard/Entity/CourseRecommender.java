package com.example.StudentDashboard.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "RecommendedCourse")
public class CourseRecommender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "email", nullable = false)
    private String email;

    @ElementCollection
    @CollectionTable(
            name = "recommended_course_domains",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "domain")
    private List<String> domains;

    @ElementCollection
    @CollectionTable(
            name = "recommended_course_skills",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "skill")
    private List<String> skills;


    public CourseRecommender(String courseName, String email,
                             List<String> domains, List<String> skills) {
        this.courseName = courseName;
        this.email = email;
        this.domains = domains;
        this.skills = skills;
    }


    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
