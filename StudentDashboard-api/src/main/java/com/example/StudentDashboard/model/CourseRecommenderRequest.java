package com.example.StudentDashboard.model;

import com.example.StudentDashboard.entity.CourseRecommender;
import java.util.List;

public class CourseRecommenderRequest {
    private List<CourseRecommender> recommendations; // MUST be a List

    public List<CourseRecommender> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<CourseRecommender> recommendations) {
        this.recommendations = recommendations;
    }
}
