package com.example.taskmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Task Management Backend API",
                version = "1.1",
                description = "Comprehensive REST API for managing tasks, users, and comments",
                contact = @Contact(
                        name = "Support Team",
                        email = "",
                        url = ""
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Environment"),
                @Server(url = "https://api.example.com", description = "Production Environment")
        }
)
public class TaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
        System.out.println("Task Management Backend API is running...");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new CustomCorsConfigurer(List.of("http://localhost:8080", "http://frontend_server"));
    }
}

@Configuration
class CustomCorsConfigurer implements WebMvcConfigurer {
    private final List<String> allowedOrigins;

    public CustomCorsConfigurer(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
