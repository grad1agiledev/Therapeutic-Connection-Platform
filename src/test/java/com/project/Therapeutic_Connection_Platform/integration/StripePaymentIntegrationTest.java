package com.project.Therapeutic_Connection_Platform.integration;

import com.project.Therapeutic_Connection_Platform.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StripePaymentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPaymentFlow() {
        String patientId = createTestPatient("test@example.com", "Test Patient");
        
        PaymentIntentResponse paymentIntent = createPaymentIntent(patientId, 2000L, "standard_session", false);
        
        PaymentIntentResponse confirmedPayment = confirmPaymentIntent(paymentIntent.getId());
        assertEquals("succeeded", confirmedPayment.getStatus());
    }

    @Test
    void testSubscriptionFlow() {
        String patientId = createTestPatient("subscription@example.com", "Subscription Patient");
        
        SubscriptionResponse subscription = createSubscription(patientId, "standard_session", 1, false, 0.0);
        assertEquals("active", subscription.getStatus());
        
        SubscriptionResponse updatedSubscription = updateSubscription(subscription.getId(), "standard_session", 15.0);
        assertEquals("active", updatedSubscription.getStatus());
        
        SubscriptionResponse canceledSubscription = cancelSubscription(subscription.getId());
        assertEquals("canceled", canceledSubscription.getStatus());
    }

    @Test
    void testReceiptGeneration() {
        String patientId = createTestPatient("receipt@example.com", "Receipt Patient");
        
        PaymentIntentResponse paymentIntent = createPaymentIntent(patientId, 5000L, "initial_consultation", false);
        
        confirmPaymentIntent(paymentIntent.getId());
        
        ReceiptResponse receipt = generateReceipt(paymentIntent.getId());
        assertNotNull(receipt.getHostedUrl());
        assertNotNull(receipt.getPdfUrl());
    }

    @Test
    void testGetSessionRates() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/api/payment/session-rates", Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("initial_consultation"));
        assertTrue(response.getBody().containsKey("standard_session"));
    }
    
    private String createTestPatient(String email, String name) {
        CreateCustomerRequest patientRequest = new CreateCustomerRequest(email, name);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/payment/create-customer", patientRequest, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String patientId = response.getBody();
        assertNotNull(patientId);
        return patientId;
    }
    
    private PaymentIntentResponse createPaymentIntent(String patientId, Long amount, String sessionType, boolean isHsa) {
        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest(
                amount, "usd", "pm_card_visa", patientId, sessionType, isHsa);
        ResponseEntity<PaymentIntentResponse> response = restTemplate.postForEntity(
                "/api/payment/create-payment-intent", paymentRequest, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentIntentResponse paymentIntent = response.getBody();
        assertNotNull(paymentIntent);
        assertNotNull(paymentIntent.getId());
        assertEquals("requires_confirmation", paymentIntent.getStatus());
        return paymentIntent;
    }
    
    private PaymentIntentResponse confirmPaymentIntent(String paymentIntentId) {
        ResponseEntity<PaymentIntentResponse> response = restTemplate.postForEntity(
                "/api/payment/confirm-payment-intent/" + paymentIntentId, null, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentIntentResponse confirmedIntent = response.getBody();
        assertNotNull(confirmedIntent);
        return confirmedIntent;
    }
    
    private SubscriptionResponse createSubscription(String patientId, String sessionType, 
                                                  Integer intervalCount, boolean isHsa, Double discountPercentage) {
        CreateSubscriptionRequest subscriptionRequest = new CreateSubscriptionRequest(
                patientId, "pm_card_visa", sessionType, intervalCount, isHsa, discountPercentage);
        ResponseEntity<SubscriptionResponse> response = restTemplate.postForEntity(
                "/api/payment/create-subscription", subscriptionRequest, SubscriptionResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SubscriptionResponse subscription = response.getBody();
        assertNotNull(subscription);
        return subscription;
    }
    
    private SubscriptionResponse updateSubscription(String subscriptionId, String sessionType, Double discountPercentage) {
        UpdateSubscriptionRequest updateRequest = new UpdateSubscriptionRequest(sessionType, discountPercentage);
        ResponseEntity<SubscriptionResponse> response = restTemplate.postForEntity(
                "/api/payment/update-subscription/" + subscriptionId, updateRequest, SubscriptionResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SubscriptionResponse updatedSubscription = response.getBody();
        assertNotNull(updatedSubscription);
        return updatedSubscription;
    }
    
    private SubscriptionResponse cancelSubscription(String subscriptionId) {
        ResponseEntity<SubscriptionResponse> response = restTemplate.postForEntity(
                "/api/payment/cancel-subscription/" + subscriptionId, null, SubscriptionResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SubscriptionResponse canceledSubscription = response.getBody();
        assertNotNull(canceledSubscription);
        return canceledSubscription;
    }
    
    private ReceiptResponse generateReceipt(String paymentIntentId) {
        ResponseEntity<ReceiptResponse> response = restTemplate.postForEntity(
                "/api/payment/generate-receipt/" + paymentIntentId, null, ReceiptResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReceiptResponse receipt = response.getBody();
        assertNotNull(receipt);
        return receipt;
    }
} 