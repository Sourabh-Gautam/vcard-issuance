package com.nium.virtualcard.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Virtual Card Issuance API",
                description = "APIs for creating and spending virtual cards",
                version = "1.0",
                contact = @Contact(
                        name = "Sourabh Gautam",
                        email = "sourabhpaul87448@email.com"
                ),
                license = @License(
                        name = "For evaluation purpose only"
                )
        )
)
public class OpenApiConfig {
}
