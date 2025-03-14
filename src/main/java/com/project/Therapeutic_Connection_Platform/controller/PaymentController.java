package com.project.Therapeutic_Connection_Platform.controller;

import com.project.Therapeutic_Connection_Platform.dto.CreateCustomerRequest;
import com.project.Therapeutic_Connection_Platform.dto.CreatePaymentIntentRequest;
import com.project.Therapeutic_Connection_Platform.dto.PaymentIntentResponse;
import com.project.Therapeutic_Connection_Platform.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
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
                    request.getCustomerId()
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
    public ResponseEntity<PaymentIntentResponse> confirmPaymentIntent(@PathVariable String paymentIntentId) {
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
    public ResponseEntity<PaymentIntentResponse> cancelPaymentIntent(@PathVariable String paymentIntentId) {
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
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable String paymentIntentId) {
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
} 