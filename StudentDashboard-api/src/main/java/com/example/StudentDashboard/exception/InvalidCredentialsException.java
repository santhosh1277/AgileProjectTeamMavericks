package com.example.StudentDashboard.exception; // NOSONAR - package naming follows project convention

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}


