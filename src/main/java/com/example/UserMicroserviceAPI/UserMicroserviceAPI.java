package com.example.UserMicroserviceAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.UserMicroserviceAPI.model")
public class UserMicroserviceAPI {

	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceAPI.class, args);
	}

}
