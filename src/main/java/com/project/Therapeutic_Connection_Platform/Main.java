package com.project.Therapeutic_Connection_Platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("STRIPE_API_KEY", dotenv.get("STRIPE_API_KEY"));
		System.setProperty("STRIPE_WEBHOOK_SECRET", dotenv.get("STRIPE_WEBHOOK_SECRET"));
		SpringApplication.run(Main.class, args);
	}

}
