package com.example.StudentDashboard.model;

import java.util.List;

public class CourseRecommendationRequest {
    private String degree;
    private List<String> certifications;
    private List<String> interests;

    // Getters and setters
    public String getHighestDegree() { return degree; }
    public void setHighestDegree(String degree) { this.degree = degree; }

    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
}
