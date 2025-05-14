# Therapeutic Connection Platform

## Özellikler

- Terapist profillerini görüntüleme
- Terapist arama ve filtreleme
- Hasta yorumları ve değerlendirmeleri
- Yönetici paneli ile yorum onaylama/reddetme
- Randevu sistemi
- Ödeme entegrasyonu
- Yönetici panelinde Azure veritabanından kullanıcı listesi görüntüleme
- Profile sayfasında Türkiye şehirleri dropdown seçeneği

## Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/yourusername/Therapeutic-Connection-Platform.git
```

2. Backend'i başlatın:
```bash
cd Therapeutic-Connection-Platform
./gradlew bootrun
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
- Azure veritabanındaki tüm kullanıcıları listeleyebilir ve filtreleyebilir

Yönetici paneline erişmek için:
1. Ana sayfadaki "Yorum Yönetimi" linkine tıklayın
2. Onay bekleyen yorumları görüntüleyin
3. Her yorum için "Onayla" veya "Reddet" seçeneğini kullanın
4. İsteğe bağlı olarak yönetici yorumu ekleyin

Kullanıcı listesine erişmek için:
1. Yönetici hesabıyla giriş yapın
2. Üst menüdeki "Terapistler" linkine tıklayın
3. Terapistleri görüntüleyin, arama çubuğunu kullanarak filtreleme yapabilirsiniz

**Not:** Terapist listesi sayfası sadece "therapist" rolüne sahip kullanıcıları gösterir.

## Teknolojiler

- Backend: Spring Boot
- Frontend: React.js
- Veritabanı: Azure MySQL
- Kimlik Doğrulama: Firebase Authentication
- Ödeme: Stripe API
- Stil: Material-UI, CSS

## Önemli Notlar

Therapist modelinde `isVirtual` alanı, primitive `boolean` yerine nesne tipteki `Boolean` olarak tanımlanmalıdır. Bu şekilde null değerler kabul edilebilir ve veritabanından null bir değer okunduğunda Java tarafında hata alınmaz.

```java
@Column(name = "is_virtual")
private Boolean isVirtual;
```

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

## Branch: redesign/ui-overhaul

Bu branch, platformun kullanıcı arayüzünü (UI) baştan sona modern ve kullanıcı dostu bir şekilde yeniden tasarlamak için oluşturulmuştur. Tüm UI değişiklikleri ve yeni tasarım bu branch üzerinde geliştirilecektir.

## View Users Table from Firestore

To print the Users table from Firestore in your terminal:

1. Create and activate a Python virtual environment:
   ```bash
   python -m venv venv
   source venv/bin/activate
   ```
2. Install dependencies:
   ```bash
   pip install firebase-admin
   ```
3. Make sure your `.env` file contains the correct path to your Firebase config JSON file:
   ```env
   FIREBASE_CONFIG_FILE_PATH=therapeutic-connection-firebase-adminsdk-fbsvc-cf1d07720f.json
   ```
4. Enable the Firestore API for your project if you haven't already:
   https://console.developers.google.com/apis/api/firestore.googleapis.com/overview?project=therapeutic-connection
5. Run the script:
   ```bash
   python view_users.py
   ```

This will print all users in the `users` collection in your Firestore database.

## List All Users from Backend (PostgreSQL)

After logging in, you can print all users in the backend PostgreSQL users table with the following steps:

1. Make sure your backend is running.
2. Use this Python script to print all users:

```python
import requests

response = requests.get('http://localhost:8080/api/users/all')
users = response.json()
print("\nUsers Table:")
print("-" * 50)
for user in users:
    print(f"ID: {user['id']}")
    print(f"Firebase UID: {user['firebaseUid']}")
    print(f"Full Name: {user['fullName']}")
    print(f"Email: {user['email']}")
    print(f"Phone: {user['phone']}")
    print(f"Address: {user['address']}")
    print(f"Role: {user['role']}")
    print(f"Date Created: {user['dateCreated']}")
    print("-" * 50)
```

This will print all users in the users table after a successful login.

## Güncellemeler

### 2024-07-11: Sekme Başlığı ve Logo Güncellemesi
- Tarayıcı sekmesindeki başlık "React App" yerine "Therapeutic Connection Platform" olarak değiştirildi
- Uygulamanın favicon ve logo görselleri yeni tasarımla güncellendi
- Uygulama manifest dosyasında isimlendirme "Therapeutic Connection Platform" olarak güncellendi

### 2024-07-09: Profil Sayfasına Türkiye Şehirleri Eklendi
- Edit Profile sayfasındaki location dropdown'una Türkiye'deki 81 il eklendi
- Türkiye şehirlerini kolay bir şekilde seçebilme özelliği
- Şehir seçildiğinde adres otomatik olarak "Şehir, Turkey" formatında kaydedilir
