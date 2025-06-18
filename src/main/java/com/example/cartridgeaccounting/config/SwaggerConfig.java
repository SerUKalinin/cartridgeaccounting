package com.example.cartridgeaccounting.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация Swagger/OpenAPI для документации API.
 * Настраивает метаданные API и информацию о проекте.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * Создает конфигурацию OpenAPI для Swagger
     * 
     * @return настроенный объект OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("API Системы учёта картриджей МФУ")
                        .description("REST API для автоматизации учёта картриджей МФУ. " +
                                "Система позволяет управлять поступлением, выдачей, возвратом, " +
                                "заправкой и списанием картриджей на различных объектах.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Команда разработки")
                                .email("serukalinin@gmail.com.com")
                                .url("https://github.com/serukalinin"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальный сервер разработки"),
                        new Server()
                                .url("https://api.cartridge-accounting.com")
                                .description("Продакшн сервер")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
} 