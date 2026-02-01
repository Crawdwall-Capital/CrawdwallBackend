package com.crawdwall_backend_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
        printBanner();
    }

    private static void printBanner() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                ║");
        System.out.println("║   ██████╗██████╗  █████╗ ██╗    ██╗██████╗ ██╗    ██╗ █████╗ ██╗     ██╗      ║");
        System.out.println("║  ██╔════╝██╔══██╗██╔══██╗██║    ██║██╔══██╗██║    ██║██╔══██╗██║     ██║      ║");
        System.out.println("║  ██║     ██████╔╝███████║██║ █╗ ██║██║  ██║██║ █╗ ██║███████║██║     ██║      ║");
        System.out.println("║  ██║     ██╔══██╗██╔══██║██║███╗██║██║  ██║██║███╗██║██╔══██║██║     ██║      ║");
        System.out.println("║  ╚██████╗██║  ██║██║  ██║╚███╔███╔╝██████╔╝╚███╔███╔╝██║  ██║███████╗███████╗ ║");
        System.out.println("║   ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚══╝╚══╝ ╚═════╝  ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝╚══════╝ ║");
        System.out.println("║                                                                                ║");
        System.out.println("║           B A C K E N D   A P I   S E R V I C E   S T A R T E D                ║");
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n");
    }
}