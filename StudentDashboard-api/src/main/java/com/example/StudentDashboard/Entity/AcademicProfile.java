package com.example.StudentDashboard.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "academic_profile")
public class AcademicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "highest_degree", nullable = false)
    private String highestDegree;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @ElementCollection
    @CollectionTable(
            name = "academic_profile_certifications",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "certification")
    private List<String> certifications;

    // -------------------------
    // Interests
    // -------------------------
    @ElementCollection
    @CollectionTable(
            name = "academic_profile_interests",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "interest")
    private List<String> interests;

    // -------------------------
    // Constructors
    // -------------------------

    public AcademicProfile() {}

    public AcademicProfile(String highestDegree, String email,
                           List<String> certifications, List<String> interests) {
        this.highestDegree = highestDegree;
        this.email = email;
        this.certifications = certifications;
        this.interests = interests;
    }


    public Long getId() {
        return id;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
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
