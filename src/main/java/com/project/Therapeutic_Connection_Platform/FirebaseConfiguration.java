package com.project.Therapeutic_Connection_Platform;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

    @Value("${FIREBASE_CONFIG_FILE_PATH}")
    private String firebaseConfigFilePath;

    public void init() throws IOException {

        try (InputStream firebaseServiceKey = new FileInputStream(firebaseConfigFilePath))
        {
            FirebaseOptions firebaseOptions = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(firebaseServiceKey)).build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }
        }

    }
}
