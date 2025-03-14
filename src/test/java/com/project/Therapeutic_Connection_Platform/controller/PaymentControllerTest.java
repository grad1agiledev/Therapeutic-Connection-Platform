package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.CreateCustomerRequest;
import com.project.Therapeutic_Connection_Platform.dto.CreatePaymentIntentRequest;
import com.project.Therapeutic_Connection_Platform.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentControllerTest {

    @Mock
    private StripePaymentService stripePaymentService;

    @Mock
    private PaymentIntent paymentIntent;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePaymentIntent() throws StripeException {
 
        CreatePaymentIntentRequest request = new CreatePaymentIntentRequest(
                2000L, "usd", "pm_card_visa", "cus_123456789");
        
        when(stripePaymentService.createPaymentIntent(
                anyLong(), anyString(), anyString(), anyString()))
                .thenReturn(paymentIntent);
        
        when(paymentIntent.getId()).thenReturn("pi_123456789");
        when(paymentIntent.getStatus()).thenReturn("requires_confirmation");
        when(paymentIntent.getClientSecret()).thenReturn("pi_123456789_secret_987654321");
        
 
        ResponseEntity<?> response = paymentController.createPaymentIntent(request);
        
  
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testConfirmPaymentIntent() throws StripeException {
  
        String paymentIntentId = "pi_123456789";
        
        when(stripePaymentService.confirmPaymentIntent(anyString())).thenReturn(paymentIntent);
        when(paymentIntent.getId()).thenReturn(paymentIntentId);
        when(paymentIntent.getStatus()).thenReturn("succeeded");
        when(paymentIntent.getClientSecret()).thenReturn("pi_123456789_secret_987654321");
        
  
        ResponseEntity<?> response = paymentController.confirmPaymentIntent(paymentIntentId);
        
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCancelPaymentIntent() throws StripeException {
      
        String paymentIntentId = "pi_123456789";
        
        when(stripePaymentService.cancelPaymentIntent(anyString())).thenReturn(paymentIntent);
        when(paymentIntent.getId()).thenReturn(paymentIntentId);
        when(paymentIntent.getStatus()).thenReturn("canceled");
        when(paymentIntent.getClientSecret()).thenReturn("pi_123456789_secret_987654321");
        
     
        ResponseEntity<?> response = paymentController.cancelPaymentIntent(paymentIntentId);
        
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateCustomer() throws StripeException {
        
        CreateCustomerRequest request = new CreateCustomerRequest("test@example.com", "Test User");
        String customerId = "cus_123456789";
        
        when(stripePaymentService.createCustomer(anyString(), anyString())).thenReturn(customerId);
        
    
        ResponseEntity<String> response = paymentController.createCustomer(request);
        
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerId, response.getBody());
    }

    @Test
    void testCreatePaymentIntentWithStripeException() throws StripeException {
     
        CreatePaymentIntentRequest request = new CreatePaymentIntentRequest(
                2000L, "usd", "pm_card_visa", "cus_123456789");
        
        when(stripePaymentService.createPaymentIntent(
                anyLong(), anyString(), anyString(), anyString()))
                .thenThrow(StripeException.class);
        
        
        ResponseEntity<?> response = paymentController.createPaymentIntent(request);
        
       
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
} 