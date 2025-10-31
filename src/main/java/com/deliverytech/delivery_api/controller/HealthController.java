package com.deliverytech.delivery_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        String json = """
                {
                    "status": "UP",
                    "timestamp": "%s",
                    "service": "Delivery API",
                    "javaVersion": "%s"
                }
                """.formatted(LocalDateTime.now(), System.getProperty("java.version"));
        return json;
    }

    @GetMapping("/info")
    public AppInfo info() {
        return new AppInfo(
                "Delivery Tech API",
                "1.0.0",
                "Marcelo Duarte de Aguiar",
                "JDK 21",
                "Spring Boot 3.5.7"
        );
    }

    public record AppInfo(
            String application,
            String version,
            String developer,
            String javaVersion,
            String framework
    ) {}
}
