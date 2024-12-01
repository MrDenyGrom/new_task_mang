package com.example.taskmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    public SwaggerConfig() {
        logger.info("Initializing SwaggerConfig.");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Creating OpenAPI bean for Swagger configuration.");
        Info apiInfo = new Info()
                .title("Task Management API")
                .version("v1.0")
                .description("API documentation for Task Management application.");
        logger.debug("API Info created with title: '{}', version: '{}', description: '{}'.",
                apiInfo.getTitle(), apiInfo.getVersion(), apiInfo.getDescription());

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        logger.debug("Security scheme created with type: '{}', scheme: '{}', bearerFormat: '{}'.",
                securityScheme.getType(), securityScheme.getScheme(), securityScheme.getBearerFormat());

        Components components = new Components()
                .addSecuritySchemes("bearer-key", securityScheme);
        logger.debug("Components configured with security scheme: 'bearer-key'.");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearer-key");
        logger.debug("Security requirement added for 'bearer-key'.");

        OpenAPI openAPI = new OpenAPI()
                .info(apiInfo)
                .components(components)
                .addSecurityItem(securityRequirement);
        logger.info("OpenAPI configuration completed successfully.");

        return openAPI;
    }

    public void customise(OpenAPI openApi) {
        if (openApi == null) {
            logger.warn("Received null OpenAPI instance for customisation. Skipping...");
            return;
        }
        logger.info("Customising OpenAPI instance.");
        logger.debug("OpenAPI customisation is currently empty. Extend this method if needed.");
    }
}
