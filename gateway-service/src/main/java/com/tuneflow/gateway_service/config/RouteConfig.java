package com.tuneflow.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .uri("http://localhost:8082"))

                // Future services — uncomment as you build them
                .route("user-service", r -> r
                        .path("/api/v1/users/**")
                        .uri("http://localhost:8083"))

                // .route("music-service", r -> r
                //         .path("/api/v1/music/**")
                //         .uri("http://localhost:8084"))

                // .route("playlist-service", r -> r
                //         .path("/api/v1/playlists/**")
                //         .uri("http://localhost:8085"))

                .build();
    }
}