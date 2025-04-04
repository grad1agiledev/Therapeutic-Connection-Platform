package com.project.Therapeutic_Connection_Platform.model;

import java.time.LocalDateTime;

public class Review {
    private String id;
    private String therapistId;
    private String patientId;
    private String sessionId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private ReviewStatus status;
    private String adminComment;

    public enum ReviewStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public Review() {
        this.status = ReviewStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Review(String id, String therapistId, String patientId, String sessionId, 
                 int rating, String comment) {
        this.id = id;
        this.therapistId = therapistId;
        this.patientId = patientId;
        this.sessionId = sessionId;
        this.rating = rating;
        this.comment = comment;
        this.status = ReviewStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
} 