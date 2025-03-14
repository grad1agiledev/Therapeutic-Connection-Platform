package com.project.Therapeutic_Connection_Platform.integration;

import com.project.Therapeutic_Connection_Platform.dto.CreateCustomerRequest;
import com.project.Therapeutic_Connection_Platform.dto.CreatePaymentIntentRequest;
import com.project.Therapeutic_Connection_Platform.dto.PaymentIntentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StripePaymentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPaymentFlow() {
   
        CreateCustomerRequest customerRequest = new CreateCustomerRequest("test@example.com", "Test User");
        ResponseEntity<String> customerResponse = restTemplate.postForEntity(
                "/api/payment/create-customer", customerRequest, String.class);
        
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        assertNotNull(customerResponse.getBody());
        String customerId = customerResponse.getBody();
        
        
        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest(
                2000L, "usd", "pm_card_visa", customerId);
        ResponseEntity<PaymentIntentResponse> paymentResponse = restTemplate.postForEntity(
                "/api/payment/create-payment-intent", paymentRequest, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, paymentResponse.getStatusCode());
        assertNotNull(paymentResponse.getBody());
        
        PaymentIntentResponse paymentIntent = paymentResponse.getBody();
        assertNotNull(paymentIntent.getId());
        assertNotNull(paymentIntent.getClientSecret());
        assertEquals("requires_confirmation", paymentIntent.getStatus());
        
  
        ResponseEntity<PaymentIntentResponse> confirmResponse = restTemplate.postForEntity(
                "/api/payment/confirm-payment-intent/" + paymentIntent.getId(), null, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, confirmResponse.getStatusCode());
        assertNotNull(confirmResponse.getBody());
        assertEquals("succeeded", confirmResponse.getBody().getStatus());
    }
    
    @Test
    void testPaymentFlowWithCancellation() {
  
        CreateCustomerRequest customerRequest = new CreateCustomerRequest("cancel@example.com", "Cancel User");
        ResponseEntity<String> customerResponse = restTemplate.postForEntity(
                "/api/payment/create-customer", customerRequest, String.class);
        
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        String customerId = customerResponse.getBody();
        
       
        CreatePaymentIntentRequest paymentRequest = new CreatePaymentIntentRequest(
                3000L, "usd", "pm_card_visa", customerId);
        ResponseEntity<PaymentIntentResponse> paymentResponse = restTemplate.postForEntity(
                "/api/payment/create-payment-intent", paymentRequest, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, paymentResponse.getStatusCode());
        PaymentIntentResponse paymentIntent = paymentResponse.getBody();
        
    
        ResponseEntity<PaymentIntentResponse> cancelResponse = restTemplate.postForEntity(
                "/api/payment/cancel-payment-intent/" + paymentIntent.getId(), null, PaymentIntentResponse.class);
        
        assertEquals(HttpStatus.OK, cancelResponse.getStatusCode());
        assertNotNull(cancelResponse.getBody());
        assertEquals("canceled", cancelResponse.getBody().getStatus());
    }
} 