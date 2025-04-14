package com.project.Therapeutic_Connection_Platform.dto;

public class CreateSubscriptionRequest {
    private String customerId;
    private String paymentMethodId;
    private String sessionType;
    private Integer intervalCount;
    private boolean hsa;
    private Double discountPercentage;

    public CreateSubscriptionRequest() {
    }

    public CreateSubscriptionRequest(String customerId, String paymentMethodId, String sessionType, 
                                    Integer intervalCount, boolean hsa, Double discountPercentage) {
        this.customerId = customerId;
        this.paymentMethodId = paymentMethodId;
        this.sessionType = sessionType;
        this.intervalCount = intervalCount;
        this.hsa = hsa;
        this.discountPercentage = discountPercentage;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getIntervalCount() {
        return intervalCount;
    }

    public void setIntervalCount(Integer intervalCount) {
        this.intervalCount = intervalCount;
    }

    public boolean isHsa() {
        return hsa;
    }

    public void setHsa(boolean hsa) {
        this.hsa = hsa;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
} 