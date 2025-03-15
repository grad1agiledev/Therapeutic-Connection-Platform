package com.project.Therapeutic_Connection_Platform.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class StripePaymentServiceTest {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Mock
    private PaymentIntent mockPaymentIntent;

    @Mock
    private Customer mockCustomer;

    @Mock
    private PaymentMethod mockPaymentMethod;

    private StripePaymentService stripePaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Stripe.apiKey = stripeApiKey;
        stripePaymentService = new StripePaymentService();
        
        try {
            when(mockPaymentIntent.getId()).thenReturn("pi_test123");
            when(mockPaymentIntent.getStatus()).thenReturn("requires_confirmation");
            when(mockPaymentIntent.getClientSecret()).thenReturn("secret_test123");
            when(mockCustomer.getId()).thenReturn("cus_test123");
            when(mockPaymentMethod.getId()).thenReturn("pm_test123");
            
            // Mock static methods
            mockStaticStripeCreation();
        } catch (StripeException e) {
            fail("Failed to set up mocks: " + e.getMessage());
        }
    }

    private void mockStaticStripeCreation() throws StripeException {
        // No need to mock static methods in actual test class
        // This would be needed in a real implementation
    }

    @Test
    void testCreatePaymentIntent() throws StripeException {
        Long amount = 2000L;
        String currency = "usd";
        String paymentMethodId = "pm_card_visa";
        String customerId = "cus_123456789";
        String sessionType = "standard_session";
        boolean isHsa = false;

        PaymentIntent result = stripePaymentService.createPaymentIntent(
            amount, currency, paymentMethodId, customerId, sessionType, isHsa);

        assertNotNull(result);
        assertTrue(result.getId().startsWith("pi_"));
        assertEquals("requires_confirmation", result.getStatus());
    }

    @Test
    void testConfirmPaymentIntent() throws StripeException {
        String paymentIntentId = "pi_123456789";
        PaymentIntent result = stripePaymentService.confirmPaymentIntent(paymentIntentId);
        
        assertNotNull(result);
        assertTrue(result.getId().startsWith("pi_"));
    }

    @Test
    void testCancelPaymentIntent() throws StripeException {
        String paymentIntentId = "pi_123456789";
        PaymentIntent result = stripePaymentService.cancelPaymentIntent(paymentIntentId);
        
        assertNotNull(result);
        assertTrue(result.getId().startsWith("pi_"));
        assertEquals("canceled", result.getStatus());
    }

    @Test
    void testCreateCustomer() throws StripeException {
        String email = "test@example.com";
        String name = "Test Customer";
        
        String customerId = stripePaymentService.createCustomer(email, name);
        
        assertNotNull(customerId);
        assertTrue(customerId.startsWith("cus_"));
    }

    @Test
    void testGetSessionRate() {
        assertEquals(15000L, stripePaymentService.getSessionRate("initial_consultation"));
        assertEquals(12000L, stripePaymentService.getSessionRate("standard_session"));
        assertEquals(18000L, stripePaymentService.getSessionRate("extended_session"));
        assertEquals(8000L, stripePaymentService.getSessionRate("group_session"));
        assertEquals(12000L, stripePaymentService.getSessionRate("unknown_session"));
    }
} 