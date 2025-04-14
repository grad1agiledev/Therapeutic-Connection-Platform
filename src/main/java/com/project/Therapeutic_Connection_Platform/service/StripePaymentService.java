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

    private final Map<String, Long> sessionRates = Map.of(
        "initial_consultation", 15000L,  
        "standard_session", 12000L,   
        "extended_session", 18000L,     
        "group_session", 8000L        
    );

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency, String paymentMethodId, 
                                            String customerId, String sessionType, boolean isHSA) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("payment_method", paymentMethodId);
        params.put("customer", customerId);
        params.put("confirm", false);
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("session_type", sessionType);
        metadata.put("is_hsa", String.valueOf(isHSA));
        metadata.put("for_insurance", "true");
        params.put("metadata", metadata);
        
        if (isHSA) {
            params.put("description", "Therapy Session - " + sessionType.replace("_", " "));
            
            params.put("statement_descriptor", "THERAPY SESSION");
            
            params.put("statement_descriptor_suffix", "HEALTHCARE");
        }
        
        Map<String, Object> automaticPaymentMethods = new HashMap<>();
        automaticPaymentMethods.put("enabled", true);
        automaticPaymentMethods.put("allow_redirects", "never");
        params.put("automatic_payment_methods", automaticPaymentMethods);
        
        params.put("receipt_email", getCustomerEmail(customerId));
        
        return PaymentIntent.create(params);
    }

    public Subscription createSubscription(String customerId, String paymentMethodId, 
                                          String sessionType, Integer intervalCount, 
                                          boolean isHSA, Double discountPercentage) throws StripeException {

        attachPaymentMethodToCustomer(paymentMethodId, customerId);
        
        updateCustomerDefaultPaymentMethod(customerId, paymentMethodId);
        
        String priceId = createOrRetrievePrice(sessionType, discountPercentage);
        
        Map<String, Object> subscriptionParams = new HashMap<>();
        subscriptionParams.put("customer", customerId);
        
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("price", priceId);
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(itemParams);
        subscriptionParams.put("items", items);
        
        if (intervalCount != null && intervalCount > 1) {
            Map<String, Object> billingCycleAnchor = new HashMap<>();
            billingCycleAnchor.put("interval", "week");
            billingCycleAnchor.put("interval_count", intervalCount);
            subscriptionParams.put("billing_cycle_anchor", billingCycleAnchor);
        }
        
        Map<String, String> metadata = new HashMap<>();
        metadata.put("session_type", sessionType);
        metadata.put("is_hsa", String.valueOf(isHSA));
        metadata.put("for_insurance", "true");
        if (discountPercentage != null && discountPercentage > 0) {
            metadata.put("discount_percentage", String.valueOf(discountPercentage));
        }
        subscriptionParams.put("metadata", metadata);
        
        subscriptionParams.put("collection_method", "charge_automatically");
        
        return Subscription.create(subscriptionParams);
    }

    private String createOrRetrievePrice(String sessionType, Double discountPercentage) throws StripeException {
        Long baseAmount = sessionRates.getOrDefault(sessionType, 12000L);
        
        if (discountPercentage != null && discountPercentage > 0) {
            double discountMultiplier = 1 - (discountPercentage / 100.0);
            baseAmount = Math.round(baseAmount * discountMultiplier);
        }
        
        String priceIdSuffix = sessionType;
        if (discountPercentage != null && discountPercentage > 0) {
            priceIdSuffix += "_discount_" + discountPercentage.intValue();
        }
        
        try {
            Price existingPrice = Price.retrieve("price_" + priceIdSuffix);
            return existingPrice.getId();
        } catch (StripeException e) {
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

    private PaymentMethod attachPaymentMethodToCustomer(String paymentMethodId, String customerId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        return paymentMethod.attach(params);
    }

    private Customer updateCustomerDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        Map<String, Object> params = new HashMap<>();
        params.put("invoice_settings", Map.of(
            "default_payment_method", paymentMethodId
        ));
        return customer.update(params);
    }

    private String getCustomerEmail(String customerId) throws StripeException {
        Customer customer = Customer.retrieve(customerId);
        return customer.getEmail();
    }

    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        Map<String, Object> params = new HashMap<>();
        params.put("return_url", "http://localhost:8080/payment-success.html");
        return paymentIntent.confirm(params);
    }

    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.cancel();
    }

    public String createCustomer(String email, String name) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        customerParams.put("name", name);
        
        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }
    
    public Subscription cancelSubscription(String subscriptionId) throws StripeException {
        Subscription subscription = Subscription.retrieve(subscriptionId);
        return subscription.cancel();
    }
    
    public Subscription updateSubscription(String subscriptionId, String sessionType, Double discountPercentage) throws StripeException {
        String newPriceId = createOrRetrievePrice(sessionType, discountPercentage);
        
        Subscription subscription = Subscription.retrieve(subscriptionId);
        
        String subscriptionItemId = subscription.getItems().getData().get(0).getId();
        
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> itemParams = new HashMap<>();
        itemParams.put("id", subscriptionItemId);
        itemParams.put("price", newPriceId);
        
        List<Map<String, Object>> items = new ArrayList<>();
        items.add(itemParams);
        params.put("items", items);
        
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
    
    public Invoice generateReceipt(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        
        Map<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("customer", paymentIntent.getCustomer());
        
        Map<String, Object> lineItemParams = new HashMap<>();
        lineItemParams.put("amount", paymentIntent.getAmount());
        lineItemParams.put("currency", paymentIntent.getCurrency());
        lineItemParams.put("description", "Therapy Session: " + paymentIntent.getMetadata().get("session_type").replace("_", " "));
        
        List<Map<String, Object>> lineItems = new ArrayList<>();
        lineItems.add(lineItemParams);
        invoiceParams.put("lines", lineItems);
        
        Map<String, String> metadata = new HashMap<>(paymentIntent.getMetadata());
        metadata.put("for_insurance", "true");
        invoiceParams.put("metadata", metadata);
        
        invoiceParams.put("auto_advance", true);
        
        Invoice invoice = Invoice.create(invoiceParams);
        return invoice.finalizeInvoice();
    }
    
    public Long getSessionRate(String sessionType) {
        return sessionRates.getOrDefault(sessionType, 12000L);
    }
} 