package com.revworkforce.performance.service;

import com.revworkforce.performance.entity.Goal;
import com.revworkforce.performance.entity.PerformanceNotification;
import com.revworkforce.performance.entity.PerformanceReview;
import com.revworkforce.performance.exception.ResourceNotFoundException;
import com.revworkforce.performance.repository.PerformanceNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PerformanceNotificationService {

    private final PerformanceNotificationRepository notificationRepository;

    @Autowired
    public PerformanceNotificationService(PerformanceNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Long userId, String message) {
        PerformanceNotification notification = new PerformanceNotification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<PerformanceNotification> getNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        PerformanceNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notification.setReadFlag(true);
        notificationRepository.save(notification);
    }

    public void notifyReviewCreated(PerformanceReview review) {
        String message = "A new performance review has been created for you.";
        createNotification(review.getEmployeeId(), message);
    }

    public void notifyFeedbackProvided(PerformanceReview review) {
        String message = "Your manager has provided feedback on your performance review.";
        createNotification(review.getEmployeeId(), message);
    }

    public void notifyGoalCommented(Goal goal) {
        String message = "Your manager has added a comment to one of your goals.";
        createNotification(goal.getEmployeeId(), message);
    }
}
