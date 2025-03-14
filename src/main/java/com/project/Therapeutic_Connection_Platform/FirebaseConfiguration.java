package com.project.Therapeutic_Connection_Platform;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

  public void init() throws IOException
  {
      InputStream firebaseServiceKey = this.getClass().getResourceAsStream("../resources/therapeutic-connection-firebase-adminsdk-fbsvc-4d0e0fc357.json");
      FirebaseOptions firebaseOptions = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(firebaseServiceKey)).build();

      if (FirebaseApp.getApps().isEmpty())
      {
          FirebaseApp.initializeApp(firebaseOptions);
      }
  }


}
