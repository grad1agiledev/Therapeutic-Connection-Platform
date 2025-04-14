package com.project.Therapeutic_Connection_Platform.dto;

public class UpdateSubscriptionRequest {
    private String sessionType;
    private Double discountPercentage;

    public UpdateSubscriptionRequest() {
    }

    public UpdateSubscriptionRequest(String sessionType, Double discountPercentage) {
        this.sessionType = sessionType;
        this.discountPercentage = discountPercentage;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
} 