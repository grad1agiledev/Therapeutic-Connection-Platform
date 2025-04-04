# Güvenli Oturum Notları Sistemi

Bu sistem, terapistlerin oturum notlarını güvenli bir şekilde şifreleyerek saklamalarını sağlar.

## Özellikler

- Notlar Fernet şifreleme ile güvenli bir şekilde saklanır
- Her not için benzersiz şifreleme anahtarı kullanılır
- Notlar JSON formatında saklanır
- Zaman damgası ile notların oluşturulma zamanı kaydedilir

## Kurulum

1. Python sanal ortamı oluşturun:
```bash
python3 -m venv venv
source venv/bin/activate
```

2. Gerekli paketleri yükleyin:
```bash
pip install -r requirements.txt
```

## Kullanım

```python
from secure_notes import SecureNoteStorage

# Not depolama nesnesi oluştur
storage = SecureNoteStorage()

# Not kaydet
storage.save_note(
    therapist_id="therapist123",
    session_id="session456",
    note_content="Oturum notu içeriği"
)

# Notu getir
note = storage.get_note("therapist123", "session456")
print(note['content'])
```

## Güvenlik

- Notlar şifrelenerek saklanır
- Şifreleme anahtarı .env dosyasında güvenli bir şekilde tutulur
- Her not için benzersiz dosya adı kullanılır 