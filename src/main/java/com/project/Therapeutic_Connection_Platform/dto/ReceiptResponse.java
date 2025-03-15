package com.project.Therapeutic_Connection_Platform.dto;

public class ReceiptResponse {
    private String id;
    private String number;
    private String hostedUrl;
    private String pdfUrl;

    public ReceiptResponse() {
    }

    public ReceiptResponse(String id, String number, String hostedUrl, String pdfUrl) {
        this.id = id;
        this.number = number;
        this.hostedUrl = hostedUrl;
        this.pdfUrl = pdfUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHostedUrl() {
        return hostedUrl;
    }

    public void setHostedUrl(String hostedUrl) {
        this.hostedUrl = hostedUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
} 