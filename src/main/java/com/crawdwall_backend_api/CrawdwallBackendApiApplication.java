package com.crawdwall_backend_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@OpenAPIDefinition
public class CrawdwallBackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrawdwallBackendApiApplication.class, args);
	}

}
