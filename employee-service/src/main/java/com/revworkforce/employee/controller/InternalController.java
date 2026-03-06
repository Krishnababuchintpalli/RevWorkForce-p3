package com.revworkforce.employee.controller;

import com.revworkforce.employee.dto.EmployeeSummaryResponse;
import com.revworkforce.employee.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal/employees")
public class InternalController {

    private final EmployeeService employeeService;

    public InternalController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSummaryResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeSummaryResponse employee = employeeService.getEmployeeSummary(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<EmployeeSummaryResponse>> getEmployeesByIds(
            @RequestBody List<Long> ids) {
        List<EmployeeSummaryResponse> employees = employeeService.getEmployeesByIds(ids);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<EmployeeSummaryResponse> getEmployeeByUserId(
            @PathVariable Long userId) {
        EmployeeSummaryResponse employee = employeeService.getEmployeeSummaryByUserId(userId);
        return ResponseEntity.ok(employee);
    }
}
