package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.*;
import com.project.Therapeutic_Connection_Platform.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentControllerTest {

    private static final String TEST_PAYMENT_INTENT_ID = "pi_123456789";
    private static final String TEST_SUBSCRIPTION_ID = "sub_123456789";
    private static final String TEST_PATIENT_ID = "pt_123456789";
    private static final String TEST_INVOICE_ID = "in_123456789";
    private static final String TEST_INVOICE_NUMBER = "INV-123";
    private static final String CLIENT_SECRET = "pi_123456789_secret_987654321";

    @Mock
    private StripePaymentService paymentService;

    @Mock
    private PaymentIntent paymentIntent;

    @Mock
    private Subscription subscription;

    @Mock
    private Invoice invoice;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupPaymentIntentMock();
        setupSubscriptionMock();
        setupInvoiceMock();
    }

    private void setupPaymentIntentMock() {
        when(paymentIntent.getId()).thenReturn(TEST_PAYMENT_INTENT_ID);
        when(paymentIntent.getClientSecret()).thenReturn(CLIENT_SECRET);
    }

    private void setupSubscriptionMock() {
        when(subscription.getId()).thenReturn(TEST_SUBSCRIPTION_ID);
        when(subscription.getCurrentPeriodStart()).thenReturn(1234567890L);
        when(subscription.getCurrentPeriodEnd()).thenReturn(1234657890L);
    }

    private void setupInvoiceMock() {
        when(invoice.getId()).thenReturn(TEST_INVOICE_ID);
        when(invoice.getNumber()).thenReturn(TEST_INVOICE_NUMBER);
        when(invoice.getHostedInvoiceUrl()).thenReturn("https://invoice.url");
        when(invoice.getInvoicePdf()).thenReturn("https://invoice.pdf");
    }

    @Test
    void testCreatePaymentIntentSuccess() throws StripeException {
        CreatePaymentIntentRequest request = createTestPaymentRequest();
        when(paymentIntent.getStatus()).thenReturn("requires_confirmation");
        when(paymentService.createPaymentIntent(anyLong(), anyString(), anyString(), 
                anyString(), anyString(), anyBoolean())).thenReturn(paymentIntent);
        
        ResponseEntity<PaymentIntentResponse> response = paymentController.createPaymentIntent(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testConfirmPaymentIntentSuccess() throws StripeException {
        when(paymentIntent.getStatus()).thenReturn("succeeded");
        when(paymentService.confirmPaymentIntent(anyString())).thenReturn(paymentIntent);
        
        ResponseEntity<?> response = paymentController.confirmPaymentIntent(TEST_PAYMENT_INTENT_ID);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCancelPaymentIntentSuccess() throws StripeException {
        when(paymentIntent.getStatus()).thenReturn("canceled");
        when(paymentService.cancelPaymentIntent(anyString())).thenReturn(paymentIntent);
        
        ResponseEntity<?> response = paymentController.cancelPaymentIntent(TEST_PAYMENT_INTENT_ID);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreatePatientSuccess() throws StripeException {
        CreateCustomerRequest request = new CreateCustomerRequest("patient@example.com", "Test Patient");
        when(paymentService.createCustomer(anyString(), anyString())).thenReturn(TEST_PATIENT_ID);
        
        ResponseEntity<String> response = paymentController.createCustomer(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_PATIENT_ID, response.getBody());
    }

    @Test
    void testCreatePaymentIntentFailsWithStripeException() throws StripeException {
        CreatePaymentIntentRequest request = createTestPaymentRequest();
        when(paymentService.createPaymentIntent(anyLong(), anyString(), anyString(), 
                anyString(), anyString(), anyBoolean())).thenThrow(StripeException.class);
        
        ResponseEntity<?> response = paymentController.createPaymentIntent(request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCancelSubscriptionSuccess() throws StripeException {
        when(subscription.getStatus()).thenReturn("canceled");
        when(paymentService.cancelSubscription(anyString())).thenReturn(subscription);
        
        ResponseEntity<SubscriptionResponse> response = paymentController.cancelSubscription(TEST_SUBSCRIPTION_ID);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("canceled", response.getBody().getStatus());
    }

    @Test
    void testUpdateSubscriptionSuccess() throws StripeException {
        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest("standard_session", 15.0);
        when(subscription.getStatus()).thenReturn("active");
        when(paymentService.updateSubscription(anyString(), anyString(), anyDouble())).thenReturn(subscription);
        
        ResponseEntity<SubscriptionResponse> response = 
            paymentController.updateSubscription(TEST_SUBSCRIPTION_ID, request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("active", response.getBody().getStatus());
    }

    @Test
    void testGenerateReceiptSuccess() throws StripeException {
        when(paymentService.generateReceipt(anyString())).thenReturn(invoice);
        
        ResponseEntity<ReceiptResponse> response = paymentController.generateReceipt(TEST_PAYMENT_INTENT_ID);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_INVOICE_NUMBER, response.getBody().getNumber());
    }

    @Test
    void testGetSessionRatesReturnsCorrectValues() {
        when(paymentService.getSessionRate("initial_consultation")).thenReturn(15000L);
        when(paymentService.getSessionRate("standard_session")).thenReturn(10000L);
        
        ResponseEntity<Map<String, Long>> response = paymentController.getSessionRates();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("initial_consultation"));
        assertTrue(response.getBody().containsKey("standard_session"));
        assertEquals(15000L, response.getBody().get("initial_consultation"));
        assertEquals(10000L, response.getBody().get("standard_session"));
    }

    @Test
    void testCancelSubscriptionFailsWithStripeException() throws StripeException {
        when(paymentService.cancelSubscription(anyString())).thenThrow(StripeException.class);
        
        ResponseEntity<SubscriptionResponse> response = paymentController.cancelSubscription(TEST_SUBSCRIPTION_ID);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateSubscriptionFailsWithStripeException() throws StripeException {
        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest("standard_session", 15.0);
        when(paymentService.updateSubscription(anyString(), anyString(), anyDouble()))
            .thenThrow(StripeException.class);
        
        ResponseEntity<SubscriptionResponse> response = 
            paymentController.updateSubscription(TEST_SUBSCRIPTION_ID, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGenerateReceiptFailsWithStripeException() throws StripeException {
        when(paymentService.generateReceipt(anyString())).thenThrow(StripeException.class);
        
        ResponseEntity<ReceiptResponse> response = paymentController.generateReceipt(TEST_PAYMENT_INTENT_ID);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
    
    private CreatePaymentIntentRequest createTestPaymentRequest() {
        return new CreatePaymentIntentRequest(
            2000L, "usd", "pm_card_visa", TEST_PATIENT_ID, "standard_session", false);
    }
} 