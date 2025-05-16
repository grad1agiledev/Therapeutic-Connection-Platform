package com.project.Therapeutic_Connection_Platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		System.setProperty("FIREBASE_CONFIG_FILE_PATH", "/etc/secrets/firebase-key.json");

		System.setProperty("STRIPE_API_KEY", System.getenv("STRIPE_API_KEY"));
		System.setProperty("STRIPE_WEBHOOK_SECRET", System.getenv("STRIPE_WEBHOOK_SECRET"));
		//System.setProperty("FIREBASE_CONFIG_FILE_PATH", System.getenv("FIREBASE_CONFIG_FILE_PATH"));
		SpringApplication.run(Main.class, args);
	}

}
