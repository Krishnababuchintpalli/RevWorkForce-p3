package com.revworkforce.leave.controller;

import com.revworkforce.leave.dto.LeaveNotificationResponse;
import com.revworkforce.leave.service.LeaveNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves/notifications")
public class LeaveNotificationController {

    private final LeaveNotificationService notificationService;

    public LeaveNotificationController(LeaveNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<LeaveNotificationResponse>> getNotifications(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        List<LeaveNotificationResponse> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            userIdStr = request.getHeader("X-User-Id");
        }
        if (userIdStr == null) {
            throw new RuntimeException("User ID not found in request");
        }
        return Long.parseLong(userIdStr);
    }
}
