package com.revworkforce.employee.service;

import com.revworkforce.employee.client.AdminServiceClient;
import com.revworkforce.employee.dto.*;
import com.revworkforce.employee.entity.Employee;
import com.revworkforce.employee.exception.ResourceNotFoundException;
import com.revworkforce.employee.exception.UnauthorizedException;
import com.revworkforce.employee.repository.EmployeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AdminServiceClient adminServiceClient;

    public EmployeeService(EmployeeRepository employeeRepository, AdminServiceClient adminServiceClient) {
        this.employeeRepository = employeeRepository;
        this.adminServiceClient = adminServiceClient;
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getProfile(Long userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user ID: " + userId));

        String departmentName = getDepartmentName(employee.getDepartmentId());
        String designationName = getDesignationName(employee.getDesignationId());
        String managerName = employee.getManagerId() != null ? getManagerName(employee.getManagerId()) : null;

        return EmployeeProfileResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .emergencyContact(employee.getEmergencyContact())
                .departmentName(departmentName)
                .designationName(designationName)
                .joiningDate(employee.getJoiningDate())
                .status(employee.getStatus())
                .managerName(managerName)
                .build();
    }

    @Transactional
    public EmployeeProfileResponse updateProfile(Long userId, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user ID: " + userId));

        // Only allow updating phone, address, and emergencyContact
        if (request.getPhone() != null) {
            employee.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }
        if (request.getEmergencyContact() != null) {
            employee.setEmergencyContact(request.getEmergencyContact());
        }

        employee = employeeRepository.save(employee);

        String departmentName = getDepartmentName(employee.getDepartmentId());
        String designationName = getDesignationName(employee.getDesignationId());
        String managerName = employee.getManagerId() != null ? getManagerName(employee.getManagerId()) : null;

        return EmployeeProfileResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .emergencyContact(employee.getEmergencyContact())
                .departmentName(departmentName)
                .designationName(designationName)
                .joiningDate(employee.getJoiningDate())
                .status(employee.getStatus())
                .managerName(managerName)
                .build();
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        // Check if employee already exists for this userId
        if (employeeRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Employee already exists for user ID: " + request.getUserId());
        }

        Employee employee = Employee.builder()
                .userId(request.getUserId())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .departmentId(request.getDepartmentId())
                .designationId(request.getDesignationId())
                .joiningDate(request.getJoiningDate())
                .salary(request.getSalary())
                .status("ACTIVE")
                .managerId(request.getManagerId())
                .roleId(request.getRoleId())
                .build();

        employee = employeeRepository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return toEmployeeResponse(employee);
    }

    @Transactional
    public EmployeeResponse activateDeactivate(Long id, String status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new IllegalArgumentException("Invalid status. Must be ACTIVE or INACTIVE");
        }

        employee.setStatus(status);
        employee = employeeRepository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Transactional
    public EmployeeResponse assignManager(Long id, Long managerId) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        // Verify manager exists
        if (!employeeRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager not found with ID: " + managerId);
        }

        employee.setManagerId(managerId);
        employee = employeeRepository.save(employee);

        return toEmployeeResponse(employee);
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeam(Long userId) {
        Employee manager = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user ID: " + userId));

        List<Employee> teamMembers = employeeRepository.findByManagerId(manager.getId());

        return teamMembers.stream()
                .map(this::toTeamMemberResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeSummaryResponse getEmployeeSummary(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        return EmployeeSummaryResponse.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .managerId(employee.getManagerId())
                .build();
    }

    @Transactional(readOnly = true)
    public EmployeeSummaryResponse getEmployeeSummaryByUserId(Long userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user ID: " + userId));

        return EmployeeSummaryResponse.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .managerId(employee.getManagerId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<EmployeeSummaryResponse> getEmployeesByIds(List<Long> ids) {
        return employeeRepository.findAllById(ids).stream()
                .map(employee -> EmployeeSummaryResponse.builder()
                        .id(employee.getId())
                        .fullName(employee.getFullName())
                        .email(employee.getEmail())
                        .managerId(employee.getManagerId())
                        .build())
                .collect(Collectors.toList());
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        String departmentName = getDepartmentName(employee.getDepartmentId());
        String designationName = getDesignationName(employee.getDesignationId());
        String managerName = employee.getManagerId() != null ? getManagerName(employee.getManagerId()) : null;

        return EmployeeResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .emergencyContact(employee.getEmergencyContact())
                .departmentId(employee.getDepartmentId())
                .departmentName(departmentName)
                .designationId(employee.getDesignationId())
                .designationName(designationName)
                .joiningDate(employee.getJoiningDate())
                .salary(employee.getSalary())
                .status(employee.getStatus())
                .managerId(employee.getManagerId())
                .managerName(managerName)
                .roleId(employee.getRoleId())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private TeamMemberResponse toTeamMemberResponse(Employee employee) {
        String designationName = getDesignationName(employee.getDesignationId());

        return TeamMemberResponse.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .email(employee.getEmail())
                .designation(designationName)
                .status(employee.getStatus())
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

    private String getManagerName(Long managerId) {
        try {
            Employee manager = employeeRepository.findById(managerId).orElse(null);
            return manager != null ? manager.getFullName() : "Unknown Manager";
        } catch (Exception e) {
            return "Unknown Manager";
        }
    }

    private String getDepartmentNameFallback(Long departmentId, Exception e) {
        return "Unknown Department";
    }

    private String getDesignationNameFallback(Long designationId, Exception e) {
        return "Unknown Designation";
    }
}
