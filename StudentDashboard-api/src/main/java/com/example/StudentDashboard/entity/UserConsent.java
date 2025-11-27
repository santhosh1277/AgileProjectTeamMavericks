package com.example.StudentDashboard.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Consent")
public class UserConsent {


    @Id
    @Column(nullable = false, unique = true)
    private String email;  

    @Column(nullable = false)
    private boolean consentGiven;

    // Constructors
    public UserConsent() {}

    public UserConsent(String email, boolean consentGiven) {
        this.email = email;
        this.consentGiven = consentGiven;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConsentGiven() {
        return consentGiven;
    }

    public void setConsentGiven(boolean consentGiven) {
        this.consentGiven = consentGiven;
    }
}
