package com.projects.petshopNew;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(contact = @Contact(name = "test", email = "test@email.com", url = "https://test.com"), title = "Petshop API", version = "1", description = "API developed for a petshop", license = @License(name = "License", url = "https://license.com"), termsOfService = "Terms of service"),
		servers = {@Server(description = "Local env", url = "http://localhost:8080")})
@SpringBootApplication
public class PetshopNewApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetshopNewApplication.class, args);
	}

}
