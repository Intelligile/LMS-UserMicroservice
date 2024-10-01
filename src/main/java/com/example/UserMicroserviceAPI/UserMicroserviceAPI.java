package com.example.UserMicroserviceAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@SpringBootApplication
@EntityScan(basePackages = "com.example.UserMicroserviceAPI.model")  // Ensure this points to your entity package
@EnableJpaRepositories(basePackages = "com.example.UserMicroserviceAPI.repository")  // Ensure this points to your repository package
public class UserMicroserviceAPI {

	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceAPI.class, args);
	}

}
