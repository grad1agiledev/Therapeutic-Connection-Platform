package com.project.Therapeutic_Connection_Platform.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class StripePaymentServiceTest {

    @Mock
    private PaymentIntent paymentIntent;

    @InjectMocks
    private StripePaymentService stripePaymentService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePaymentIntent() throws StripeException {
        
        Long amount = 2000L; 
        String currency = "usd";
        String paymentMethodId = "pm_card_visa";
        String customerId = "cus_123456789";
        
        
        PaymentIntent result = stripePaymentService.createPaymentIntent(amount, currency, paymentMethodId, customerId);
        
       
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("requires_confirmation", result.getStatus());
    }

    @Test
    void testConfirmPaymentIntent() throws StripeException {
     
        String paymentIntentId = "pi_123456789";
        
        when(PaymentIntent.retrieve(anyString())).thenReturn(paymentIntent);
        when(paymentIntent.confirm()).thenReturn(paymentIntent);
        when(paymentIntent.getStatus()).thenReturn("succeeded");
        
        PaymentIntent result = stripePaymentService.confirmPaymentIntent(paymentIntentId);
        
        assertNotNull(result);
        assertEquals("succeeded", result.getStatus());
        verify(paymentIntent, times(1)).confirm();
    }

    @Test
    void testCancelPaymentIntent() throws StripeException {

        String paymentIntentId = "pi_123456789";
        
        when(PaymentIntent.retrieve(anyString())).thenReturn(paymentIntent);
        when(paymentIntent.cancel()).thenReturn(paymentIntent);
        when(paymentIntent.getStatus()).thenReturn("canceled");
        
        PaymentIntent result = stripePaymentService.cancelPaymentIntent(paymentIntentId);
        
        assertNotNull(result);
        assertEquals("canceled", result.getStatus());
        verify(paymentIntent, times(1)).cancel();
    }

    @Test
    void testCreateCustomer() throws StripeException {

        String email = "test@example.com";
        String name = "Test User";
        
    
        String customerId = stripePaymentService.createCustomer(email, name);
        
   
        assertNotNull(customerId);
        assertFalse(customerId.isEmpty());
    }
} 