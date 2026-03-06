package com.revworkforce.performance.service;

import com.revworkforce.performance.client.EmployeeServiceClient;
import com.revworkforce.performance.dto.EmployeeDto;
import com.revworkforce.performance.dto.GoalCommentRequest;
import com.revworkforce.performance.dto.GoalRequest;
import com.revworkforce.performance.dto.GoalResponse;
import com.revworkforce.performance.dto.GoalStatusUpdateRequest;
import com.revworkforce.performance.dto.TeamGoalResponse;
import com.revworkforce.performance.entity.Goal;
import com.revworkforce.performance.enums.GoalStatus;
import com.revworkforce.performance.exception.ResourceNotFoundException;
import com.revworkforce.performance.exception.UnauthorizedException;
import com.revworkforce.performance.repository.GoalRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final PerformanceNotificationService notificationService;

    @Autowired
    public GoalService(GoalRepository goalRepository,
                      EmployeeServiceClient employeeServiceClient,
                      PerformanceNotificationService notificationService) {
        this.goalRepository = goalRepository;
        this.employeeServiceClient = employeeServiceClient;
        this.notificationService = notificationService;
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "createGoalFallback")
    public GoalResponse createGoal(Long userId, GoalRequest request) {
        // Get employee by userId
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user id: " + userId);
        }

        Goal goal = new Goal();
        goal.setEmployeeId(employee.getId());
        goal.setDescription(request.getDescription());
        goal.setDeadline(request.getDeadline());
        goal.setPriority(request.getPriority());
        goal.setStatus(GoalStatus.NOT_STARTED);

        Goal savedGoal = goalRepository.save(goal);
        return mapToResponse(savedGoal);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "getMyGoalsFallback")
    public List<GoalResponse> getMyGoals(Long userId) {
        // Get employee by userId
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found for user id: " + userId);
        }

        List<Goal> goals = goalRepository.findByEmployeeId(employee.getId());
        return goals.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "getTeamGoalsFallback")
    public TeamGoalResponse getTeamGoals(Long userId, String role) {
        // Only manager/admin can view team goals
        if (!role.equals("MANAGER") && !role.equals("ADMIN")) {
            throw new UnauthorizedException("Only managers and admins can view team goals");
        }

        // Get manager's employee record
        EmployeeDto manager = employeeServiceClient.getEmployeeByUserId(userId);
        if (manager == null) {
            throw new ResourceNotFoundException("Employee not found for user id: " + userId);
        }

        // For ADMIN, get all goals; for MANAGER, get their team's goals
        List<Goal> goals;
        if (role.equals("ADMIN")) {
            goals = goalRepository.findAll();
        } else {
            // Get all employees managed by this manager
            // This would require additional endpoint in employee-service
            // For now, just get all goals
            goals = goalRepository.findAll();
        }

        List<GoalResponse> responses = goals.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new TeamGoalResponse(responses);
    }

    public GoalResponse updateGoalStatus(Long userId, Long goalId, GoalStatusUpdateRequest request) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));

        // Verify this is the employee's own goal
        EmployeeDto employee = employeeServiceClient.getEmployeeByUserId(userId);
        if (employee == null || !employee.getId().equals(goal.getEmployeeId())) {
            throw new UnauthorizedException("You can only update your own goals");
        }

        goal.setStatus(request.getStatus());
        Goal updatedGoal = goalRepository.save(goal);

        return mapToResponse(updatedGoal);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "addManagerCommentFallback")
    public GoalResponse addManagerComment(Long userId, Long goalId, GoalCommentRequest request, String role) {
        // Only manager/admin can add comments
        if (!role.equals("MANAGER") && !role.equals("ADMIN")) {
            throw new UnauthorizedException("Only managers and admins can add comments to goals");
        }

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + goalId));

        goal.setManagerComment(request.getManagerComment());
        Goal updatedGoal = goalRepository.save(goal);

        // Notify employee
        notificationService.notifyGoalCommented(updatedGoal);

        return mapToResponse(updatedGoal);
    }

    @CircuitBreaker(name = "employee-service", fallbackMethod = "mapToResponseFallback")
    private GoalResponse mapToResponse(Goal goal) {
        GoalResponse response = new GoalResponse();
        response.setId(goal.getId());
        response.setEmployeeId(goal.getEmployeeId());
        response.setDescription(goal.getDescription());
        response.setDeadline(goal.getDeadline());
        response.setPriority(goal.getPriority());
        response.setStatus(goal.getStatus());
        response.setManagerComment(goal.getManagerComment());
        response.setCreatedAt(goal.getCreatedAt());
        response.setUpdatedAt(goal.getUpdatedAt());

        // Fetch employee name
        try {
            EmployeeDto employee = employeeServiceClient.getEmployee(goal.getEmployeeId());
            if (employee != null) {
                response.setEmployeeName(employee.getFullName());
            }
        } catch (Exception e) {
            // If employee service is down, still return the response without name
            response.setEmployeeName("Unknown");
        }

        return response;
    }

    // Fallback methods
    private GoalResponse createGoalFallback(Long userId, GoalRequest request, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private List<GoalResponse> getMyGoalsFallback(Long userId, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private TeamGoalResponse getTeamGoalsFallback(Long userId, String role, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private GoalResponse addManagerCommentFallback(Long userId, Long goalId, GoalCommentRequest request, String role, Exception ex) {
        throw new RuntimeException("Employee service is currently unavailable. Please try again later.");
    }

    private GoalResponse mapToResponseFallback(Goal goal, Exception ex) {
        GoalResponse response = new GoalResponse();
        response.setId(goal.getId());
        response.setEmployeeId(goal.getEmployeeId());
        response.setDescription(goal.getDescription());
        response.setDeadline(goal.getDeadline());
        response.setPriority(goal.getPriority());
        response.setStatus(goal.getStatus());
        response.setManagerComment(goal.getManagerComment());
        response.setCreatedAt(goal.getCreatedAt());
        response.setUpdatedAt(goal.getUpdatedAt());
        response.setEmployeeName("Unknown");
        return response;
    }
}
