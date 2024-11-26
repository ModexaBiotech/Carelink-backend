package com.modexa.carelink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.examples.Example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Carelink API Documentation")
                        .description("API documentation for Carelink Healthcare Management System")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Modexa Biotech")
                                .email("modexanio@gmail.com"))
                        .license(new License()
                                .name("Private License")
                                .url("https://modexa.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token in the format: Bearer <token>"))
                        .addExamples("registerRequest", new Example()
                                .value("{" +
                                        "\"username\": \"johndoe\"," +
                                        "\"email\": \"john.doe@example.com\"," +
                                        "\"password\": \"securepassword123\"," +
                                        "\"firstName\": \"John\"," +
                                        "\"lastName\": \"Doe\"," +
                                        "\"contactNumber\": \"1234567890\"," +
                                        "\"organizationId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\"," +
                                        "\"professionalTitle\": \"Physician\"," +
                                        "\"licenseNumber\": \"12345\"," +
                                        "\"role\": \"Physician\"" +
                                        "}")));
    }
}
