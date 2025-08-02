package com.taxi.rideUp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName : com.taxi.rideUp.config
 * fileName    : SwaggerConfig
 * author      : hsj
 * date        : 2025. 8. 3.
 * description :
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Notification Service")
                .version("1.0")
                .description("알림 서비스"));
    }
}
