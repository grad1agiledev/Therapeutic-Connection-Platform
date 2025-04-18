# Therapeutic Connection Platform

## Özellikler

- Terapist profillerini görüntüleme
- Terapist arama ve filtreleme
- Hasta yorumları ve değerlendirmeleri
- Yönetici paneli ile yorum onaylama/reddetme
- Randevu sistemi
- Ödeme entegrasyonu

## Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/Therapeutic-Connection-Platform.git
```

2. Backend'i başlatın:
```bash
cd Therapeutic-Connection-Platform
./mvnw spring-boot:run
```

3. Frontend'i başlatın:
```bash
cd ui_react_therapeutic_platform
npm install
npm start
```

## Yönetici Paneli

Yönetici paneli, hasta yorumlarını yönetmek için kullanılır. Yöneticiler:

- Onay bekleyen yorumları görüntüleyebilir
- Yorumları onaylayabilir veya reddedebilir
- Yorumlara açıklama ekleyebilir

Yönetici paneline erişmek için:
1. Ana sayfadaki "Yorum Yönetimi" linkine tıklayın
2. Onay bekleyen yorumları görüntüleyin
3. Her yorum için "Onayla" veya "Reddet" seçeneğini kullanın
4. İsteğe bağlı olarak yönetici yorumu ekleyin

## Teknolojiler

- Backend: Spring Boot
- Frontend: React.js
- Veritabanı: PostgreSQL
- Ödeme: Stripe API
- Stil: CSS

## Lisans

MIT

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
