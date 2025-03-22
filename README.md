# Therapeutic-Connection-Platform

The CS 576 term project of G-1 group.

## Environment Setup

1. Copy `.env.example` to `.env`:
```bash
cp .env.example .env
```

2. Update the `.env` file with your credentials:
```properties
# Stripe Configuration
STRIPE_API_KEY=your_stripe_api_key_here
STRIPE_WEBHOOK_SECRET=your_stripe_webhook_secret_here

# Firebase Configuration
FIREBASE_CONFIG_FILE_PATH=path_to_your_firebase_config_json
```

## Test Cards

For testing the payment system, you can use Stripe's test cards:

- **Success Card**: 4242 4242 4242 4242
- **Requires Authentication**: 4000 0025 0000 3155
- **Decline**: 4000 0000 0000 0002

## Available Endpoints

The following payment-related API endpoints are available:

- `POST /api/payment/create-customer` - Create a new Stripe customer
- `POST /api/payment/create-payment-intent` - Create a payment intent
- `POST /api/payment/confirm-payment-intent/{id}` - Confirm a payment intent
- `POST /api/payment/cancel-payment-intent/{id}` - Cancel a payment intent

All endpoints use JSON for requests and responses.

## Security Notes

- Never commit the `.env` file or Firebase configuration JSON to version control
- Always use environment variables for sensitive credentials
- Keep your API keys and service account files secure
- Store Firebase configuration JSON in a secure location
- Rotate credentials if they are compromised
