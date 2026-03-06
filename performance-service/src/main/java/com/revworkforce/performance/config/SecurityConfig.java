package com.revworkforce.performance.config;

import org.springframework.context.annotation.Configuration;

/**
 * Security configuration for Performance Service.
 * User authentication and authorization is handled by the API Gateway.
 * User information (userId, role) is extracted from request headers:
 * - X-User-Id: The authenticated user's ID
 * - X-User-Role: The user's role (EMPLOYEE, MANAGER, ADMIN)
 *
 * These headers are set by the API Gateway after JWT validation.
 */
@Configuration
public class SecurityConfig {
    // Security is handled by API Gateway
    // User info is passed via headers (X-User-Id, X-User-Role)
}
