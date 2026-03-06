package com.revworkforce.leave.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Configuration
public class SecurityConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract user information from headers set by API Gateway
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");

        // Store in request attributes for controller access
        if (userId != null) {
            request.setAttribute("userId", userId);
        }
        if (role != null) {
            request.setAttribute("role", role);
        }

        filterChain.doFilter(request, response);
    }
}
