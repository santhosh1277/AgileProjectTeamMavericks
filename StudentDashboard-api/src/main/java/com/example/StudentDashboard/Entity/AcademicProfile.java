package com.example.StudentDashboard.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "academic_profile")
public class AcademicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "degree", nullable = false, columnDefinition = "varchar(255) default 'Unknown'")
    private String degree;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @ElementCollection
    @CollectionTable(
            name = "academic_profile_certifications",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "certification")
    private List<String> certifications;

    @ElementCollection
    @CollectionTable(
            name = "academic_profile_interests",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "interest")
    private List<String> interests;

    public AcademicProfile() {}

    public AcademicProfile(String degree, String email,
                           List<String> certifications, List<String> interests) {
        this.degree = degree;
        this.email = email;
        this.certifications = certifications;
        this.interests = interests;
    }


    public Long getId() {
        return id;
    }

 public String getDegree() {
    return degree;
}

public void setDegree(String degree) {
    this.degree = degree;
}


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
