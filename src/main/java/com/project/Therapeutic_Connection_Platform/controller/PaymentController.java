package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.*;
import com.project.Therapeutic_Connection_Platform.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripePaymentService stripePaymentService;

    @Autowired
    public PaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(@RequestBody CreatePaymentIntentRequest request) {
        try {
            PaymentIntent paymentIntent = stripePaymentService.createPaymentIntent(
                    request.getAmount(),
                    request.getCurrency(),
                    request.getPaymentMethodId(),
                    request.getCustomerId(),
                    request.getSessionType(),
                    request.isHsa()
            );
            
            PaymentIntentResponse response = new PaymentIntentResponse(
                    paymentIntent.getId(),
                    paymentIntent.getStatus(),
                    paymentIntent.getClientSecret()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/confirm-payment-intent/{paymentIntentId}")
    public ResponseEntity<PaymentIntentResponse> confirmPaymentIntent(@PathVariable("paymentIntentId") String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripePaymentService.confirmPaymentIntent(paymentIntentId);
            
            PaymentIntentResponse response = new PaymentIntentResponse(
                    paymentIntent.getId(),
                    paymentIntent.getStatus(),
                    paymentIntent.getClientSecret()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/cancel-payment-intent/{paymentIntentId}")
    public ResponseEntity<PaymentIntentResponse> cancelPaymentIntent(@PathVariable("paymentIntentId") String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = stripePaymentService.cancelPaymentIntent(paymentIntentId);
            
            PaymentIntentResponse response = new PaymentIntentResponse(
                    paymentIntent.getId(),
                    paymentIntent.getStatus(),
                    paymentIntent.getClientSecret()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            String customerId = stripePaymentService.createCustomer(request.getEmail(), request.getName());
            return ResponseEntity.ok(customerId);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @GetMapping("/status/{paymentIntentId}")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable("paymentIntentId") String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            Map<String, Object> response = new HashMap<>();
            response.put("id", paymentIntent.getId());
            response.put("status", paymentIntent.getStatus());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    @PostMapping("/create-subscription")
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody CreateSubscriptionRequest request) {
        try {
            Subscription subscription = stripePaymentService.createSubscription(
                    request.getCustomerId(),
                    request.getPaymentMethodId(),
                    request.getSessionType(),
                    request.getIntervalCount(),
                    request.isHsa(),
                    request.getDiscountPercentage()
            );
            
            SubscriptionResponse response = new SubscriptionResponse(
                    subscription.getId(),
                    subscription.getStatus(),
                    subscription.getCurrentPeriodStart(),
                    subscription.getCurrentPeriodEnd()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PostMapping("/cancel-subscription/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable("subscriptionId") String subscriptionId) {
        try {
            Subscription subscription = stripePaymentService.cancelSubscription(subscriptionId);
            
            SubscriptionResponse response = new SubscriptionResponse(
                    subscription.getId(),
                    subscription.getStatus(),
                    subscription.getCurrentPeriodStart(),
                    subscription.getCurrentPeriodEnd()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PostMapping("/update-subscription/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable("subscriptionId") String subscriptionId,
            @RequestBody UpdateSubscriptionRequest request) {
        try {
            Subscription subscription = stripePaymentService.updateSubscription(
                    subscriptionId,
                    request.getSessionType(),
                    request.getDiscountPercentage()
            );
            
            SubscriptionResponse response = new SubscriptionResponse(
                    subscription.getId(),
                    subscription.getStatus(),
                    subscription.getCurrentPeriodStart(),
                    subscription.getCurrentPeriodEnd()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @PostMapping("/generate-receipt/{paymentIntentId}")
    public ResponseEntity<ReceiptResponse> generateReceipt(@PathVariable("paymentIntentId") String paymentIntentId) {
        try {
            Invoice invoice = stripePaymentService.generateReceipt(paymentIntentId);
            
            ReceiptResponse response = new ReceiptResponse(
                    invoice.getId(),
                    invoice.getNumber(),
                    invoice.getHostedInvoiceUrl(),
                    invoice.getInvoicePdf()
            );
            
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @GetMapping("/session-rates")
    public ResponseEntity<Map<String, Long>> getSessionRates() {
        Map<String, Long> rates = new HashMap<>();
        rates.put("initial_consultation", stripePaymentService.getSessionRate("initial_consultation"));
        rates.put("standard_session", stripePaymentService.getSessionRate("standard_session"));
        rates.put("extended_session", stripePaymentService.getSessionRate("extended_session"));
        rates.put("group_session", stripePaymentService.getSessionRate("group_session"));
        
        return ResponseEntity.ok(rates);
    }
} 