package com.revworkforce.employee.controller;

import com.revworkforce.employee.dto.EmployeeSearchResponse;
import com.revworkforce.employee.service.DirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeSearchResponse>> searchEmployees(
            @RequestParam("q") String query) {
        List<EmployeeSearchResponse> employees = directoryService.searchEmployees(query);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeSearchResponse>> getEmployeesByDepartment(
            @PathVariable Long departmentId) {
        List<EmployeeSearchResponse> employees = directoryService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }
}
