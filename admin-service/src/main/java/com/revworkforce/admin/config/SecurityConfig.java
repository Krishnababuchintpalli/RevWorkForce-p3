package com.revworkforce.admin.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    // Security is handled at the API Gateway level
    // User information is extracted from headers:
    // - X-User-Id: Current user's ID
    // - X-User-Role: Current user's role (ADMIN, EMPLOYEE, etc.)
    // - X-User-Email: Current user's email

    // All /api/admin/** endpoints require ADMIN role
    // This is verified in the controller methods using @RequestHeader
}
