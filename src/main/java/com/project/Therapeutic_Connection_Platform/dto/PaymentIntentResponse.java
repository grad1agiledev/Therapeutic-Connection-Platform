package com.project.Therapeutic_Connection_Platform.dto;

public class PaymentIntentResponse {
    private String id;
    private String status;
    private String clientSecret;

    public PaymentIntentResponse() {
    }

    public PaymentIntentResponse(String id, String status, String clientSecret) {
        this.id = id;
        this.status = status;
        this.clientSecret = clientSecret;
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

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
} 