package com.accenture.franchiseapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI franchiseApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Franchise API")
                        .version("v1")
                        .description("Reactive API for managing franchises, branches, and products"));
    }
}
