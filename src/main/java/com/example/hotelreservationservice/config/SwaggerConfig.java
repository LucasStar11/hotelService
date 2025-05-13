package com.example.hotelreservationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setDescription("local");
        server.setUrl("http://localhost:8080/");

        OpenAPI openAPI = new OpenAPI();
        openAPI.setServers(List.of(server));


        return openAPI;
    }
}

