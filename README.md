# Therapeutic-Connection-Platform

The CS 576 term project of G-1 group.

The goal of this project is to design and implement a comprehensive platform that connects patients with therapists, facilitates therapeutic relationships, and manages the entire therapy process.

### Test Cards

For testing the payment system, you can use Stripe's test cards:

- **Success Card**: 4242 4242 4242 4242
- **Requires Authentication**: 4000 0025 0000 3155
- **Decline**: 4000 0000 0000 0002

### Available Endpoints

The following payment-related API endpoints are available:

- `POST /api/payment/create-customer` - Create a new Stripe customer
- `POST /api/payment/create-payment-intent` - Create a payment intent
- `POST /api/payment/confirm-payment-intent/{id}` - Confirm a payment intent
- `POST /api/payment/cancel-payment-intent/{id}` - Cancel a payment intent

All endpoints use JSON for requests and responses.
