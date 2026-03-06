package com.revworkforce.employee.controller;

import com.revworkforce.employee.dto.*;
import com.revworkforce.employee.exception.UnauthorizedException;
import com.revworkforce.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/profile")
    public ResponseEntity<EmployeeProfileResponse> getProfile(
            @RequestHeader("X-User-Id") Long userId) {
        EmployeeProfileResponse profile = employeeService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<EmployeeProfileResponse> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        EmployeeProfileResponse profile = employeeService.updateProfile(userId, request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/team")
    public ResponseEntity<List<TeamMemberResponse>> getTeam(
            @RequestHeader("X-User-Id") Long userId) {
        List<TeamMemberResponse> team = employeeService.getTeam(userId);
        return ResponseEntity.ok(team);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can view all employees");
        }
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody EmployeeCreateRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can create employees");
        }
        EmployeeResponse employee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> updateStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody Map<String, String> statusRequest) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can update employee status");
        }
        String status = statusRequest.get("status");
        EmployeeResponse employee = employeeService.activateDeactivate(id, status);
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{id}/manager")
    public ResponseEntity<EmployeeResponse> assignManager(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @Valid @RequestBody ManagerAssignRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("Only admins can assign managers");
        }
        EmployeeResponse employee = employeeService.assignManager(id, request.getManagerId());
        return ResponseEntity.ok(employee);
    }
}
