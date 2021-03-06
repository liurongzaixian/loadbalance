package com.example.providera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ProviderAApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderAApplication.class, args);
	}

}

