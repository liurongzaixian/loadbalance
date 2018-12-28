package com.example.providerb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ProviderBApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderBApplication.class, args);
	}

}

