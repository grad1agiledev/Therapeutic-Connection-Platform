from cryptography.fernet import Fernet
import os
from dotenv import load_dotenv

load_dotenv()

class SecureNoteEncryption:
    def __init__(self):
        self.key = os.getenv('ENCRYPTION_KEY')
        if not self.key:
            self.key = Fernet.generate_key()
            with open('.env', 'a') as f:
                f.write(f'\nENCRYPTION_KEY={self.key.decode()}')
        self.cipher_suite = Fernet(self.key)

    def encrypt_note(self, note_content):
        """Not içeriğini şifreler"""
        return self.cipher_suite.encrypt(note_content.encode())

    def decrypt_note(self, encrypted_content):
        """Şifrelenmiş notu çözer"""
        return self.cipher_suite.decrypt(encrypted_content).decode() 