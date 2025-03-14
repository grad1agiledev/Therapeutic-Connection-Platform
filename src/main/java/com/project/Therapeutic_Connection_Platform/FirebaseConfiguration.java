package com.project.Therapeutic_Connection_Platform;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class FirebaseConfiguration {

    @Value("${FIREBASE_CONFIG_FILE_PATH:src/main/resources/firebase-config.json}")
    private String firebaseConfigFilePath;

    @PostConstruct
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
