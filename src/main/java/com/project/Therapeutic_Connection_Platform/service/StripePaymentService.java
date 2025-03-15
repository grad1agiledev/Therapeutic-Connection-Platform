package com.project.Therapeutic_Connection_Platform.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripePaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // Session types and their default rates in cents
    private final Map<String, Long> sessionRates = Map.of(
        "initial_consultation", 15000L,  // $150.00
        "standard_session", 12000L,      // $120.00
        "extended_session", 18000L,      // $180.00
        "group_session", 8000L           // $80.00
    );

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * Creates a payment intent for a one-time payment
     */
    public PaymentIntent createPaymentIntent(Long amount, String currency, String paymentMethodId, 
                                            String customerId, String sessionType, boolean isHSA) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("payment_method", paymentMethodId);
        params.put("customer", customerId);
        params.put("confirm", false);
        
        // Add metadata for reporting and receipts
        Map<String, String> metadata = new HashMap<>();
        metadata.put("session_type", sessionType);
        metadata.put("is_hsa", String.valueOf(isHSA));
        metadata.put("for_insurance", "true");
        params.put("metadata", metadata);
        
        // Configure for HSA/FSA cards if needed
        if (isHSA) {
            params.put("description", "Therapy Session - " + sessionType.replace("_", " "));
            
            // For HSA/FSA cards, we need to specify the statement descriptor
            params.put("statement_descriptor", "THERAPY SESSION");
            
            // Set the appropriate MCC code for healthcare
            params.put("statement_descriptor_suffix", "HEALTHCARE");
        }
        
        // Configure automatic payment methods
        Map<String, Object> automaticPaymentMethods = new HashMap<>();
        automaticPaymentMethods.put("enabled", true);
        automaticPaymentMethods.put("allow_redirects", "never");
        params.put("automatic_payment_methods", automaticPaymentMethods);
        
        // Enable receipt generation
        params.put("receipt_email", getCustomerEmail(customerId));
        
        return PaymentIntent.create(params);
    }

    /**
     * Creates a subscription for recurring therapy sessions
     */
    public Subscription createSubscription(String customerId, String paymentMethodId, 
                                          String sessionType, Integer intervalCount, 
                                          boolean isHSA, Double discountPercentage) throws StripeException {
        // First, attach the payment method to the customer
        attachPaymentMethodToCustomer(paymentMethodId, customerId);
        
        // Set the customer's default payment method
        updateCustomerDefaultPaymentMethod(customerId, paymentMethodId);
        
        // Create a price for the subscription if it doesn't exist
        String priceId = createOrRetrievePrice(sessionType, discountPercentage);
        
        // Create subscription parameters
        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", customerId);
        
        // Add the price to the subscription
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("price", priceId);
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(itemParams);
        subscriptionParams.put("items", items);
        
        // Set billing cycle (default: weekly)
        if (intervalCount != null && intervalCount > 1) {
            Map<String, Object> billingCycleAnchor = new HashMap<>();
            billingCycleAnchor.put("interval", "week");
            billingCycleAnchor.put("interval_count", intervalCount);
            subscriptionParams.put("billing_cycle_anchor", billingCycleAnchor);
        }
        
        // Add metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("session_type", sessionType);
        metadata.put("is_hsa", String.valueOf(isHSA));
        metadata.put("for_insurance", "true");
        if (discountPercentage != null && discountPercentage > 0) {
            metadata.put("discount_percentage", String.valueOf(discountPercentage));
        }
        subscriptionParams.put("metadata", metadata);
        
        // Enable receipt generation
        subscriptionParams.put("collection_method", "charge_automatically");
        
        return Subscription.create(subscriptionParams);
    }

    /**
     * Creates or retrieves a price for a session type
     */
    private String createOrRetrievePrice(String sessionType, Double discountPercentage) throws StripeException {
        Long baseAmount = sessionRates.getOrDefault(sessionType, 12000L);
        
        // Apply discount if applicable
        if (discountPercentage != null && discountPercentage > 0) {
            double discountMultiplier = 1 - (discountPercentage / 100.0);
            baseAmount = Math.round(baseAmount * discountMultiplier);
        }
        
        // Create a unique price ID based on session type and discount
        String priceIdSuffix = sessionType;
        if (discountPercentage != null && discountPercentage > 0) {
            priceIdSuffix += "_discount_" + discountPercentage.intValue();
        }
        
        // Check if price already exists
        try {
            Price existingPrice = Price.retrieve("price_" + priceIdSuffix);
            return existingPrice.getId();
        } catch (StripeException e) {
            // Price doesn't exist, create a new one
            Map<String, Object> priceParams = new HashMap<>();
            priceParams.put("unit_amount", baseAmount);
            priceParams.put("currency", "usd");
            priceParams.put("recurring", Map.of(
                "interval", "week",
                "interval_count", 1
            ));
            priceParams.put("product_data", Map.of(
                "name", "Therapy Session: " + sessionType.replace("_", " "),
                "metadata", Map.of("session_type", sessionType)
            ));
            
            Price newPrice = Price.create(priceParams);
            return newPrice.getId();
        }
    }

    /**
     * Attaches a payment method to a customer
     */
    private PaymentMethod attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        return paymentMethod.attach(params);
    }

    /**
     * Updates a customer's default payment method
     */
    private Customer updateCustomerDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Map<String, Object> params = new HashMap<>();
        params.put("invoice_settings", Map.of(
            "default_payment_method", paymentMethodId
        ));
        return customer.update(params);
    }

    /**
     * Gets a customer's email
     */
    private String getCustomerEmail(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        return customer.getEmail();
    }

    /**
     * Confirms a payment intent
     */
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        Map<String, Object> params = new HashMap<>();
        params.put("return_url", "http://localhost:8080/payment-success.html");
        return paymentIntent.confirm(params);
    }

    /**
     * Cancels a payment intent
     */
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.cancel();
    }

    /**
     * Creates a customer
     */
    public String createCustomer(String email, String name) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        customerParams.put("name", name);
        
        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }
    
    /**
     * Cancels a subscription
     */
    public Subscription cancelSubscription(String subscriptionId) throws StripeException {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        return subscription.cancel();
    }
    
    /**
     * Updates a subscription with a new price (for changing session types or applying discounts)
     */
    public Subscription updateSubscription(String subscriptionId, String sessionType, Double discountPercentage) throws StripeException {
        String newPriceId = createOrRetrievePrice(sessionType, discountPercentage);
        
        Subscription subscription = Subscription.retrieve(subscriptionId);
        
        // Get the subscription item ID
        String subscriptionItemId = subscription.getItems().getData().get(0).getId();
        
        // Update the subscription item with the new price
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("id", subscriptionItemId);
        itemParams.put("price", newPriceId);
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(itemParams);
        params.put("items", items);
        
        // Update metadata
        Map<String, String> metadata = new HashMap<>(subscription.getMetadata());
        metadata.put("session_type", sessionType);
        if (discountPercentage != null && discountPercentage > 0) {
            metadata.put("discount_percentage", String.valueOf(discountPercentage));
        } else {
            metadata.remove("discount_percentage");
        }
        params.put("metadata", metadata);
        
        return subscription.update(params);
    }
    
    /**
     * Generates a receipt for a payment
     */
    public Invoice generateReceipt(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        
        // Create an invoice for the payment
        Map<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("customer", paymentIntent.getCustomer());
        
        // Add line items
        Map<String, Object> lineItemParams = new HashMap<>();
        lineItemParams.put("amount", paymentIntent.getAmount());
        lineItemParams.put("currency", paymentIntent.getCurrency());
        lineItemParams.put("description", "Therapy Session: " + paymentIntent.getMetadata().get("session_type").replace("_", " "));
        
        List<Map<String, Object>> lineItems = new ArrayList<>();
        lineItems.add(lineItemParams);
        invoiceParams.put("lines", lineItems);
        
        // Add metadata for insurance
        Map<String, String> metadata = new HashMap<>(paymentIntent.getMetadata());
        metadata.put("for_insurance", "true");
        invoiceParams.put("metadata", metadata);
        
        // Set auto-finalization to true
        invoiceParams.put("auto_advance", true);
        
        // Create and finalize the invoice
        Invoice invoice = Invoice.create(invoiceParams);
        return invoice.finalizeInvoice();
    }
    
    /**
     * Gets the default rate for a session type
     */
    public Long getSessionRate(String sessionType) {
        return sessionRates.getOrDefault(sessionType, 12000L);
    }
} 