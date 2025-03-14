package com.project.Therapeutic_Connection_Platform.dto;

public class CreatePaymentIntentRequest {
    private Long amount;
    private String currency;
    private String paymentMethodId;
    private String customerId;

    public CreatePaymentIntentRequest() {
    }

    public CreatePaymentIntentRequest(Long amount, String currency, String paymentMethodId, String customerId) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
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
} 