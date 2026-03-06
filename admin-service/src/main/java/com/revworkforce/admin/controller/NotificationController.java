package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.AdminNotificationResponse;
import com.revworkforce.admin.dto.NotificationSendRequest;
import com.revworkforce.admin.service.AdminNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationController {

    private final AdminNotificationService notificationService;

    public NotificationController(AdminNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<AdminNotificationResponse>> getNotifications(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @PostMapping("/send")
    public ResponseEntity<AdminNotificationResponse> sendNotification(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @Valid @RequestBody NotificationSendRequest request) {
        AdminNotificationResponse response = notificationService.sendNotification(userId, role, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/broadcast")
    public ResponseEntity<List<AdminNotificationResponse>> broadcastNotification(
            @RequestHeader("X-User-Role") String role,
            @RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message is required for broadcast");
        }
        List<AdminNotificationResponse> responses = notificationService.broadcastNotification(role, message);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        notificationService.markAsRead(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestHeader("X-User-Id") Long userId) {
        long count = notificationService.getUnreadCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
