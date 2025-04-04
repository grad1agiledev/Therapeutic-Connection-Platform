import os
import json
from datetime import datetime
from .encryption import SecureNoteEncryption

class SecureNoteStorage:
    def __init__(self, storage_dir='secure_notes_data'):
        self.storage_dir = storage_dir
        self.encryption = SecureNoteEncryption()
        if not os.path.exists(storage_dir):
            os.makedirs(storage_dir)

    def save_note(self, therapist_id, session_id, note_content):
        """Oturum notunu şifreleyerek kaydeder"""
        timestamp = datetime.now().isoformat()
        encrypted_content = self.encryption.encrypt_note(note_content)
        
        note_data = {
            'therapist_id': therapist_id,
            'session_id': session_id,
            'timestamp': timestamp,
            'encrypted_content': encrypted_content.decode()
        }
        
        filename = f"{therapist_id}_{session_id}.json"
        filepath = os.path.join(self.storage_dir, filename)
        
        with open(filepath, 'w') as f:
            json.dump(note_data, f)
        
        return filepath

    def get_note(self, therapist_id, session_id):
        """Şifrelenmiş notu çözerek getirir"""
        filename = f"{therapist_id}_{session_id}.json"
        filepath = os.path.join(self.storage_dir, filename)
        
        if not os.path.exists(filepath):
            return None
        
        with open(filepath, 'r') as f:
            note_data = json.load(f)
        
        decrypted_content = self.encryption.decrypt_note(note_data['encrypted_content'].encode())
        note_data['content'] = decrypted_content
        return note_data 