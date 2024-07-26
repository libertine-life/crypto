package com.epam.xm.crypto.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Configuration providing the API information to be displayed via Swagger UI.
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    /**
     * Creates and returns an OpenAPI object.
     *
     * @return OpenAPI instance with predefined settings.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Crypto API")
                        .version("1.0")
                        .description("This is API for xm crypto service"));
    }
}