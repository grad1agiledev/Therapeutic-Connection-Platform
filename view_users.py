import firebase_admin
from firebase_admin import credentials, firestore
import os

# Firebase yapılandırma dosyasının yolunu al
cred = credentials.Certificate(os.getenv('FIREBASE_CONFIG_FILE_PATH'))
firebase_admin.initialize_app(cred)

# Firestore veritabanına bağlan
db = firestore.client()

# Users koleksiyonunu al
users_ref = db.collection('users')
users = users_ref.get()

# Kullanıcıları yazdır
print("\nUsers Table:")
print("-" * 50)
for user in users:
    user_data = user.to_dict()
    print(f"User ID: {user.id}")
    for key, value in user_data.items():
        print(f"{key}: {value}")
    print("-" * 50) 