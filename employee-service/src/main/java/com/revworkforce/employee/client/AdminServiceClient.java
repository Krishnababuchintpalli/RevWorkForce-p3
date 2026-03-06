package com.revworkforce.employee.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "admin-service")
public interface AdminServiceClient {

    @GetMapping("/api/internal/departments/{id}")
    Map<String, Object> getDepartmentById(@PathVariable("id") Long id);

    @GetMapping("/api/internal/designations/{id}")
    Map<String, Object> getDesignationById(@PathVariable("id") Long id);
}
