package com.revworkforce.leave.service;

import com.revworkforce.leave.client.EmployeeServiceClient;
import com.revworkforce.leave.dto.EmployeeDto;
import com.revworkforce.leave.dto.LeaveNotificationResponse;
import com.revworkforce.leave.entity.LeaveNotification;
import com.revworkforce.leave.entity.LeaveRequest;
import com.revworkforce.leave.exception.ResourceNotFoundException;
import com.revworkforce.leave.repository.LeaveNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveNotificationService {

    private final LeaveNotificationRepository notificationRepository;
    private final EmployeeServiceClient employeeServiceClient;

    public LeaveNotificationService(LeaveNotificationRepository notificationRepository,
                                   EmployeeServiceClient employeeServiceClient) {
        this.notificationRepository = notificationRepository;
        this.employeeServiceClient = employeeServiceClient;
    }

    @Transactional
    public void createNotification(Long userId, String message) {
        LeaveNotification notification = new LeaveNotification(userId, message);
        notificationRepository.save(notification);
    }

    public List<LeaveNotificationResponse> getNotifications(Long userId) {
        List<LeaveNotification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        LeaveNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setReadFlag(true);
        notificationRepository.save(notification);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFlagFalse(userId);
    }

    @Transactional
    public void notifyLeaveSubmitted(LeaveRequest leave) {
        try {
            // Get employee details
            EmployeeDto employee = employeeServiceClient.getEmployee(leave.getEmployeeId());

            if (employee.getManagerId() != null) {
                // Notify manager about new leave request
                String message = String.format("New leave request from %s for %s (%s to %s)",
                        employee.getFullName(),
                        leave.getLeaveType(),
                        leave.getStartDate(),
                        leave.getEndDate());

                // Get manager's user ID
                EmployeeDto manager = employeeServiceClient.getEmployee(employee.getManagerId());
                createNotification(manager.getId(), message);
            }
        } catch (Exception e) {
            // Log error but don't fail the transaction
            System.err.println("Failed to send leave submission notification: " + e.getMessage());
        }
    }

    @Transactional
    public void notifyLeaveApproved(LeaveRequest leave) {
        try {
            EmployeeDto employee = employeeServiceClient.getEmployee(leave.getEmployeeId());

            String message = String.format("Your leave request for %s (%s to %s) has been approved",
                    leave.getLeaveType(),
                    leave.getStartDate(),
                    leave.getEndDate());

            createNotification(employee.getId(), message);
        } catch (Exception e) {
            System.err.println("Failed to send leave approval notification: " + e.getMessage());
        }
    }

    @Transactional
    public void notifyLeaveRejected(LeaveRequest leave) {
        try {
            EmployeeDto employee = employeeServiceClient.getEmployee(leave.getEmployeeId());

            String message = String.format("Your leave request for %s (%s to %s) has been rejected. Reason: %s",
                    leave.getLeaveType(),
                    leave.getStartDate(),
                    leave.getEndDate(),
                    leave.getManagerComment() != null ? leave.getManagerComment() : "No reason provided");

            createNotification(employee.getId(), message);
        } catch (Exception e) {
            System.err.println("Failed to send leave rejection notification: " + e.getMessage());
        }
    }

    private LeaveNotificationResponse mapToResponse(LeaveNotification notification) {
        return new LeaveNotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isReadFlag(),
                notification.getCreatedAt()
        );
    }
}
