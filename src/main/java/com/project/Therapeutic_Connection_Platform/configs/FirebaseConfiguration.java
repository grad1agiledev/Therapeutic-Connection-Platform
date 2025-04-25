package com.project.Therapeutic_Connection_Platform.configs;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfiguration {

    @Value("${FIREBASE_CONFIG_FILE_PATH}")
    private String firebaseConfigFilePath;

    @PostConstruct
    public void init() throws IOException {
        try (InputStream firebaseServiceKey = new FileInputStream(firebaseConfigFilePath))
        {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(firebaseServiceKey)).build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }
        }
    }
}
