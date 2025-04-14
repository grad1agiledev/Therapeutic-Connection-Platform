package com.project.Therapeutic_Connection_Platform.dto;

public class SubscriptionResponse {
    private String id;
    private String status;
    private Long currentPeriodStart;
    private Long currentPeriodEnd;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(String id, String status, Long currentPeriodStart, Long currentPeriodEnd) {
        this.id = id;
        this.status = status;
        this.currentPeriodStart = currentPeriodStart;
        this.currentPeriodEnd = currentPeriodEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    public void setCurrentPeriodStart(Long currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    public Long getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    public void setCurrentPeriodEnd(Long currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }
} 