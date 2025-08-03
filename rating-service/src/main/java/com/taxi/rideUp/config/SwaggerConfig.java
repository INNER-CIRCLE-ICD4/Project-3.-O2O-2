package com.taxi.rideUp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * packageName : com.taxi.rideUp.config
 * fileName    : SwaggerConfig
 * author      : ckr
 * date        : 25. 7. 31.
 * description :
 */

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .info(info())
            .servers(servers());
    }

    private Info info() {
        return new Info()
            .title("Rating Service")
            .description("평가 서비스")
            .version("1.0.0");
    }

    private List<Server> servers() {
        return List.of(new Server().url(contextPath));
    }
}
