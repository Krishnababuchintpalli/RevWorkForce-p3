package com.revworkforce.employee.service;

import com.revworkforce.employee.client.AdminServiceClient;
import com.revworkforce.employee.dto.EmployeeSearchResponse;
import com.revworkforce.employee.entity.Employee;
import com.revworkforce.employee.repository.EmployeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectoryService {

    private final EmployeeRepository employeeRepository;
    private final AdminServiceClient adminServiceClient;

    public DirectoryService(EmployeeRepository employeeRepository, AdminServiceClient adminServiceClient) {
        this.employeeRepository = employeeRepository;
        this.adminServiceClient = adminServiceClient;
    }

    @Transactional(readOnly = true)
    public List<EmployeeSearchResponse> searchEmployees(String query) {
        List<Employee> employees = employeeRepository.searchByNameOrEmail(query);

        return employees.stream()
                .map(this::toEmployeeSearchResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeSearchResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toEmployeeSearchResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeSearchResponse> getEmployeesByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartmentId(departmentId);

        return employees.stream()
                .map(this::toEmployeeSearchResponse)
                .collect(Collectors.toList());
    }

    private EmployeeSearchResponse toEmployeeSearchResponse(Employee employee) {
        String departmentName = getDepartmentName(employee.getDepartmentId());
        String designationName = getDesignationName(employee.getDesignationId());

        return EmployeeSearchResponse.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .departmentName(departmentName)
                .designationName(designationName)
                .build();
    }

    @CircuitBreaker(name = "adminService", fallbackMethod = "getDepartmentNameFallback")
    private String getDepartmentName(Long departmentId) {
        try {
            Map<String, Object> department = adminServiceClient.getDepartmentById(departmentId);
            return (String) department.get("name");
        } catch (Exception e) {
            return "Unknown Department";
        }
    }

    @CircuitBreaker(name = "adminService", fallbackMethod = "getDesignationNameFallback")
    private String getDesignationName(Long designationId) {
        try {
            Map<String, Object> designation = adminServiceClient.getDesignationById(designationId);
            return (String) designation.get("title");
        } catch (Exception e) {
            return "Unknown Designation";
        }
    }

    private String getDepartmentNameFallback(Long departmentId, Exception e) {
        return "Unknown Department";
    }

    private String getDesignationNameFallback(Long designationId, Exception e) {
        return "Unknown Designation";
    }
}
