package com.project.Therapeutic_Connection_Platform.dto;

public class CreateCustomerRequest {
    private String email;
    private String name;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 