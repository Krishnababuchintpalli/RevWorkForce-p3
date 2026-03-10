package com.revworkforce.gateway.config;

import com.revworkforce.gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public RouteConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth Service - No JWT required for login/register
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))

                // Employee Service - JWT required
                .route("employee-service", r -> r.path("/api/employees/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://employee-service"))

                // Leave Service - JWT required
                .route("leave-service", r -> r.path("/api/leaves/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://leave-service"))

                // Performance Service - JWT required
                .route("performance-service", r -> r.path("/api/performance/**", "/api/goals/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://performance-service"))

                // Admin Service - JWT required
                .route("admin-service", r -> r.path("/api/admin/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://admin-service"))

                // Users alias route - maps /api/users/** to /api/employees/** (employee-service)
                .route("users-service", r -> r.path("/api/users", "/api/users/**")
                        .filters(f -> f.filter(jwtFilter))
                        .uri("lb://auth-service"))

                .build();
    }
}
