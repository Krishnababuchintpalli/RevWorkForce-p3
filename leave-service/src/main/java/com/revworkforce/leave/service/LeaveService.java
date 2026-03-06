package com.revworkforce.leave.service;

import com.revworkforce.leave.client.EmployeeServiceClient;
import com.revworkforce.leave.config.LeaveConfig;
import com.revworkforce.leave.dto.*;
import com.revworkforce.leave.entity.LeaveRequest;
import com.revworkforce.leave.enums.LeaveStatus;
import com.revworkforce.leave.exception.LeaveException;
import com.revworkforce.leave.exception.ResourceNotFoundException;
import com.revworkforce.leave.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final LeaveNotificationService notificationService;
    private final LeaveConfig leaveConfig;

    public LeaveService(LeaveRequestRepository leaveRepository,
                       EmployeeServiceClient employeeServiceClient,
                       LeaveNotificationService notificationService,
                       LeaveConfig leaveConfig) {
        this.leaveRepository = leaveRepository;
        this.employeeServiceClient = employeeServiceClient;
        this.notificationService = notificationService;
        this.leaveConfig = leaveConfig;
    }

    @Transactional
    public LeaveResponse applyLeave(Long userId, LeaveApplyRequest request) {
        // Validate dates
        if (!request.isValid()) {
            throw new LeaveException("End date must be greater than or equal to start date");
        }

        // Get employee details
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        // Calculate leave days
        int leaveDays = calculateLeaveDays(request.getStartDate(), request.getEndDate());

        // Check leave balance
        LeaveSummaryResponse summary = getLeaveSummary(userId);
        if (summary.getRemaining() < leaveDays) {
            throw new LeaveException(String.format(
                    "Insufficient leave balance. Required: %d days, Available: %d days",
                    leaveDays, summary.getRemaining()));
        }

        // Create leave request
        LeaveRequest leave = new LeaveRequest(
                employee.getId(),
                request.getLeaveType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getReason(),
                LeaveStatus.PENDING
        );

        leave = leaveRepository.save(leave);

        // Send notification to manager
        notificationService.notifyLeaveSubmitted(leave);

        return mapToResponse(leave, employee, null);
    }

    public List<LeaveResponse> getMyLeaves(Long userId) {
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        List<LeaveRequest> leaves = leaveRepository.findByEmployeeId(employee.getId());

        // Get manager details if available
        EmployeeDto manager = null;
        if (employee.getManagerId() != null) {
            try {
                manager = employeeServiceClient.getEmployee(employee.getManagerId());
            } catch (Exception e) {
                // Manager details not critical
            }
        }

        EmployeeDto finalManager = manager;
        return leaves.stream()
                .map(leave -> mapToResponse(leave, employee, finalManager))
                .collect(Collectors.toList());
    }

    public LeaveSummaryResponse getLeaveSummary(Long userId) {
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        int currentYear = Year.now().getValue();
        int totalAllocated = leaveConfig.getTotalAllocatedLeaves();

        // Get all leaves for the current year
        List<LeaveRequest> leaves = leaveRepository.findByEmployeeId(employee.getId()).stream()
                .filter(leave -> leave.getStartDate().getYear() == currentYear)
                .collect(Collectors.toList());

        // Calculate used (approved) leaves
        int used = leaves.stream()
                .filter(leave -> leave.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(leave -> calculateLeaveDays(leave.getStartDate(), leave.getEndDate()))
                .sum();

        // Calculate pending leaves
        int pending = leaves.stream()
                .filter(leave -> leave.getStatus() == LeaveStatus.PENDING)
                .mapToInt(leave -> calculateLeaveDays(leave.getStartDate(), leave.getEndDate()))
                .sum();

        int remaining = totalAllocated - used - pending;

        return new LeaveSummaryResponse(totalAllocated, used, pending, remaining);
    }

    public TeamLeaveResponse getTeamLeaves(Long userId, String role) {
        // Get current user's employee details
        EmployeeDto currentEmployee = employeeServiceClient.getEmployeeByUserId(userId);
        if (currentEmployee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        List<LeaveRequest> teamLeaves = new ArrayList<>();

        if ("ADMIN".equals(role)) {
            // Admin can see all leaves
            teamLeaves = leaveRepository.findAll();
        } else if ("MANAGER".equals(role)) {
            // Manager can see direct reports' leaves
            // This would require a call to employee service to get direct reports
            // For now, we'll get all leaves and filter by manager ID on employee service side
            List<LeaveRequest> allLeaves = leaveRepository.findAll();

            // Get all employee IDs
            List<Long> employeeIds = allLeaves.stream()
                    .map(LeaveRequest::getEmployeeId)
                    .distinct()
                    .collect(Collectors.toList());

            if (!employeeIds.isEmpty()) {
                // Get employee details in batch
                List<EmployeeDto> employees = employeeServiceClient.getEmployeesBatch(employeeIds);

                // Filter employees managed by current employee
                List<Long> directReportIds = employees.stream()
                        .filter(emp -> currentEmployee.getId().equals(emp.getManagerId()))
                        .map(EmployeeDto::getId)
                        .collect(Collectors.toList());

                teamLeaves = allLeaves.stream()
                        .filter(leave -> directReportIds.contains(leave.getEmployeeId()))
                        .collect(Collectors.toList());
            }
        } else {
            throw new LeaveException("You don't have permission to view team leaves");
        }

        // Map to responses with employee details
        List<LeaveResponse> responses = teamLeaves.stream()
                .map(leave -> {
                    try {
                        EmployeeDto employee = employeeServiceClient.getEmployee(leave.getEmployeeId());
                        EmployeeDto manager = null;
                        if (employee.getManagerId() != null) {
                            try {
                                manager = employeeServiceClient.getEmployee(employee.getManagerId());
                            } catch (Exception e) {
                                // Manager details not critical
                            }
                        }
                        return mapToResponse(leave, employee, manager);
                    } catch (Exception e) {
                        // If employee not found, return basic response
                        return mapToBasicResponse(leave);
                    }
                })
                .collect(Collectors.toList());

        return new TeamLeaveResponse(responses);
    }

    @Transactional
    public LeaveResponse cancelLeave(Long userId, Long leaveId) {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", leaveId));

        // Get employee details
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        // Only employee can cancel their own pending leaves
        if (!leave.getEmployeeId().equals(employee.getId())) {
            throw new LeaveException("You can only cancel your own leave requests");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveException("Only pending leave requests can be canceled");
        }

        leave.setStatus(LeaveStatus.CANCELED);
        leave = leaveRepository.save(leave);

        EmployeeDto manager = null;
        if (employee.getManagerId() != null) {
            try {
                manager = employeeServiceClient.getEmployee(employee.getManagerId());
            } catch (Exception e) {
                // Manager details not critical
            }
        }

        return mapToResponse(leave, employee, manager);
    }

    @Transactional
    public LeaveResponse approveLeave(Long userId, Long leaveId, LeaveDecisionRequest request, String role) {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", leaveId));

        // Get current user's employee details
        EmployeeDto currentEmployee = employeeServiceClient.getEmployeeByUserId(userId);
        if (currentEmployee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        // Get leave applicant details
        EmployeeDto applicant = employeeServiceClient.getEmployee(leave.getEmployeeId());

        // Check authorization: only manager of employee or admin can approve
        boolean isManager = applicant.getManagerId() != null &&
                           applicant.getManagerId().equals(currentEmployee.getId());
        boolean isAdmin = "ADMIN".equals(role);

        if (!isManager && !isAdmin) {
            throw new LeaveException("You don't have permission to approve this leave request");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveException("Only pending leave requests can be approved");
        }

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setManagerComment(request.getComment());
        leave = leaveRepository.save(leave);

        // Send notification to employee
        notificationService.notifyLeaveApproved(leave);

        return mapToResponse(leave, applicant, currentEmployee);
    }

    @Transactional
    public LeaveResponse rejectLeave(Long userId, Long leaveId, LeaveDecisionRequest request, String role) {
        // Rejection requires comment
        if (request.getComment() == null || request.getComment().trim().isEmpty()) {
            throw new LeaveException("Comment is required for rejecting a leave request");
        }

        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request", "id", leaveId));

        // Get current user's employee details
        EmployeeDto currentEmployee = employeeServiceClient.getEmployeeByUserId(userId);
        if (currentEmployee == null) {
            throw new ResourceNotFoundException("Employee not found for user ID: " + userId);
        }

        // Get leave applicant details
        EmployeeDto applicant = employeeServiceClient.getEmployee(leave.getEmployeeId());

        // Check authorization: only manager of employee or admin can reject
        boolean isManager = applicant.getManagerId() != null &&
                           applicant.getManagerId().equals(currentEmployee.getId());
        boolean isAdmin = "ADMIN".equals(role);

        if (!isManager && !isAdmin) {
            throw new LeaveException("You don't have permission to reject this leave request");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new LeaveException("Only pending leave requests can be rejected");
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setManagerComment(request.getComment());
        leave = leaveRepository.save(leave);

        // Send notification to employee
        notificationService.notifyLeaveRejected(leave);

        return mapToResponse(leave, applicant, currentEmployee);
    }

    public int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    private LeaveResponse mapToResponse(LeaveRequest leave, EmployeeDto employee, EmployeeDto manager) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setEmployeeId(leave.getEmployeeId());
        response.setEmployeeName(employee != null ? employee.getFullName() : "Unknown");
        response.setLeaveType(leave.getLeaveType());
        response.setStartDate(leave.getStartDate());
        response.setEndDate(leave.getEndDate());
        response.setReason(leave.getReason());
        response.setStatus(leave.getStatus());
        response.setManagerComment(leave.getManagerComment());
        response.setManagerName(manager != null ? manager.getFullName() : null);
        response.setCreatedAt(leave.getCreatedAt());
        response.setUpdatedAt(leave.getUpdatedAt());
        return response;
    }

    private LeaveResponse mapToBasicResponse(LeaveRequest leave) {
        LeaveResponse response = new LeaveResponse();
        response.setId(leave.getId());
        response.setEmployeeId(leave.getEmployeeId());
        response.setEmployeeName("Unknown");
        response.setLeaveType(leave.getLeaveType());
        response.setStartDate(leave.getStartDate());
        response.setEndDate(leave.getEndDate());
        response.setReason(leave.getReason());
        response.setStatus(leave.getStatus());
        response.setManagerComment(leave.getManagerComment());
        response.setCreatedAt(leave.getCreatedAt());
        response.setUpdatedAt(leave.getUpdatedAt());
        return response;
    }
}
