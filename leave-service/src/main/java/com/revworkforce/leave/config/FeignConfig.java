package com.revworkforce.leave.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();

                    // Forward headers to downstream services
                    String userId = request.getHeader("X-User-Id");
                    String role = request.getHeader("X-User-Role");

                    if (userId != null) {
                        template.header("X-User-Id", userId);
                    }
                    if (role != null) {
                        template.header("X-User-Role", role);
                    }
                }
            }
        };
    }
}
