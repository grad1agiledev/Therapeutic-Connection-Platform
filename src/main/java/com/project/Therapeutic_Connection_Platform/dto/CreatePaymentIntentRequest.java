package com.project.Therapeutic_Connection_Platform.dto;

public class CreatePaymentIntentRequest {
    private Long amount;
    private String currency;
    private String paymentMethodId;
    private String customerId;
    private String sessionType;
    private boolean hsa;

    public CreatePaymentIntentRequest() {
    }

    public CreatePaymentIntentRequest(Long amount, String currency, String paymentMethodId, String customerId, String sessionType, boolean hsa) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
        this.sessionType = sessionType;
        this.hsa = hsa;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public boolean isHsa() {
        return hsa;
    }

    public void setHsa(boolean hsa) {
        this.hsa = hsa;
    }
} 