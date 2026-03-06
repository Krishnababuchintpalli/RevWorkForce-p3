package com.revworkforce.leave.client;

import com.revworkforce.leave.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "employee-service", configuration = com.revworkforce.leave.config.FeignConfig.class)
public interface EmployeeServiceClient {

    @GetMapping("/api/internal/employees/{id}")
    EmployeeDto getEmployee(@PathVariable("id") Long id);

    @GetMapping("/api/internal/employees/user/{userId}")
    EmployeeDto getEmployeeByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/api/internal/employees/batch")
    List<EmployeeDto> getEmployeesBatch(@RequestBody List<Long> employeeIds);
}
